package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.file.FileUtilitiesException;
import com.ngc.seaside.jellyfish.utilities.file.GradleSettingsUtilities;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaevents.dto.EventsDataDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaevents.dto.EventsDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component(service = IJellyFishCommand.class)
public class CreateJavaEventsCommand implements IJellyFishCommand {

   private static final IUsage USAGE = createUsage();

   static final String EVENTS_BUILD_TEMPLATE_SUFFIX = "-build";
   static final String EVENTS_JAVA_TEMPLATE_SUFFIX = "-java";
   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String MODEL_PROPERTY = CommonParameters.MODEL.getName();
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   /**
    * The name of the command.
    */
   public static final String NAME = "create-java-events";

   private ILogService logService;
   private IProjectNamingService projectNamingService;
   private IPackageNamingService packageNamingService;
   private IDataFieldGenerationService dataFieldGenerationService;
   private IDataService dataService;
   private ITemplateService templateService;

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
   }

   @Override
   public void run(IJellyFishCommandOptions options) {
      IModel model = evaluateModelParameter(options);

      Path outputDirectory = Paths.get(
         options.getParameters().getParameter(OUTPUT_DIRECTORY_PROPERTY).getStringValue());
      boolean clean = CommonParameters.evaluateBooleanParameter(options.getParameters(), CLEAN_PROPERTY);

      IProjectInformation projectInfo = projectNamingService.getEventsProjectName(options, model);

      Path projectDirectory = outputDirectory.resolve(projectInfo.getDirectoryName());
      
      Map<INamedChild<IPackage>, Boolean> map = dataService.aggregateNestedFields(model);

      EventsDto eventsDto = new EventsDto();
      eventsDto.setProjectName(projectInfo.getDirectoryName());
      eventsDto.setExportedPackages(map.keySet()
                                       .stream()
                                       .map(child -> packageNamingService.getEventPackageName(options, child))
                                       .collect(Collectors.toCollection(TreeSet::new)));
      
      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", eventsDto));
      templateService.unpack(CreateJavaEventsCommand.class.getPackage().getName() + EVENTS_BUILD_TEMPLATE_SUFFIX,
         parameters,
         outputDirectory,
         clean);

      map.forEach((child, normal) -> {
         EventsDataDto dataDto = new EventsDataDto();
         dataDto.setPackageName(packageNamingService.getEventPackageName(options, child));
         dataDto.setClassName(child.getName());
         dataDto.setData(child);
         dataDto.setAbstract(!normal);
         if (child instanceof IData) {
            ((IData) child).getSuperDataType().ifPresent(superData -> {
               String pkg = packageNamingService.getEventPackageName(options, superData);
               dataDto.setExtendedClass(pkg + '.' + superData.getName());
            });
         }
         dataDto.setDataService(field -> dataFieldGenerationService.getEventsField(options, field));
         DefaultParameterCollection dataParameters = new DefaultParameterCollection();
         dataParameters.addParameter(new DefaultParameter<>("dto", dataDto));
         templateService.unpack(CreateJavaEventsCommand.class.getPackage().getName() + EVENTS_JAVA_TEMPLATE_SUFFIX, dataParameters, projectDirectory, false);
      });
      
      updateGradleDotSettings(outputDirectory, projectInfo);
   }
   
   private void updateGradleDotSettings(Path outputDir, IProjectInformation info) {
      DefaultParameterCollection updatedParameters = new DefaultParameterCollection();
      updatedParameters.addParameter(new DefaultParameter<>(OUTPUT_DIRECTORY_PROPERTY,
         outputDir.resolve(info.getDirectoryName()).getParent().toString()));
      updatedParameters.addParameter(new DefaultParameter<>(CommonParameters.GROUP_ID.getName(), info.getGroupId()));
      updatedParameters.addParameter(new DefaultParameter<>(CommonParameters.ARTIFACT_ID.getName(), info.getArtifactId()));
      try {
         if (!GradleSettingsUtilities.tryAddProject(updatedParameters)) {
            logService.warn(getClass(), "Unable to add the new project to settings.gradle.");
         }
      } catch (FileUtilitiesException e) {
         throw new CommandException("failed to update settings.gradle!", e);
      }
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setProjectNamingService(IProjectNamingService ref) {
      this.projectNamingService = ref;
   }

   public void removeProjectNamingService(IProjectNamingService ref) {
      setProjectNamingService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;

   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setDataFieldGenerationService(IDataFieldGenerationService ref) {
      this.dataFieldGenerationService = ref;

   }

   public void removeDataFieldGenerationService(IDataFieldGenerationService ref) {
      setDataFieldGenerationService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setDataService(IDataService ref) {
      this.dataService = ref;

   }

   public void removeDataService(IDataService ref) {
      setDataService(null);
   }
   
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setTemplateService(ITemplateService ref) {
      this.templateService = ref;

   }

   public void removeTemplateService(ITemplateService ref) {
      setTemplateService(null);
   }

   private IModel evaluateModelParameter(IJellyFishCommandOptions commandOptions) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();
      IParameterCollection parameters = commandOptions.getParameters();
      final String modelName = parameters.getParameter(MODEL_PROPERTY).getStringValue();
      return sd.findModel(modelName).orElseThrow(() -> new CommandException("Unknown model: " + modelName));
   }

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   private static IUsage createUsage() {
      return new DefaultUsage(
         "Generate a Gradle project that can generate the event sources as Java types.",
         CommonParameters.OUTPUT_DIRECTORY.required(),
         CommonParameters.MODEL.required(),
         CommonParameters.CLEAN);
   }
}
