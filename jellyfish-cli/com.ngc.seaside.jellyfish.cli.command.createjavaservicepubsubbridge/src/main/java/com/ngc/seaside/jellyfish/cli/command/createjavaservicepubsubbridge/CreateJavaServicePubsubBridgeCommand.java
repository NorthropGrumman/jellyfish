package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BasicPubSubDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.IBaseServiceDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.InputDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.PublishDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto.PubSubBridgeDto;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.jellyfish.service.template.api.ITemplateService;
import com.ngc.seaside.jellyfish.utilities.command.AbstractMultiphaseJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

@Component(service = IJellyFishCommand.class)
public class CreateJavaServicePubsubBridgeCommand extends AbstractMultiphaseJellyfishCommand {

   private static final String NAME = "create-java-service-pubsub-bridge";
   static final String PUBSUB_BRIDGE_GENERATED_BUILD_TEMPLATE_SUFFIX = "genbuild";
   static final String PUBSUB_BRIDGE_BUILD_TEMPLATE_SUFFIX = "build";
   static final String PUBSUB_BRIDGE_JAVA_TEMPLATE_SUFFIX = "java";
   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   
   private IBaseServiceDtoFactory baseServiceDtoFactory;
   private IJavaServiceGenerationService generatorService;
   
   public CreateJavaServicePubsubBridgeCommand() {
      super(NAME);
   }
   
   @Activate
   public void activate() {
      super.activate();
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      super.deactivate();
      logService.trace(getClass(), "Deactivated");
   }
   
   @Override
   protected void runDefaultPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CommonParameters.CLEAN.getName());
      
      IProjectInformation projectInfo = projectNamingService.getPubSubBridgeProjectName(getOptions(), model);
      PubSubBridgeDto pubSubBridgeDto = new PubSubBridgeDto(buildManagementService, getOptions());
      pubSubBridgeDto.setProjectName(projectInfo.getDirectoryName());

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", pubSubBridgeDto));
      unpackSuffixedTemplate(PUBSUB_BRIDGE_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
      registerProject(projectInfo);
     
   }

   @Override
   protected void runDeferredPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CommonParameters.CLEAN.getName());
      
      IProjectInformation projectInfo = projectNamingService.getPubSubBridgeProjectName(getOptions(), model);
      String packageInfo = packageNamingService.getPubSubBridgePackageName(getOptions(), model);
      Path projectDirectory = outputDirectory.resolve(projectInfo.getDirectoryName());
      
      

      //TODO Retrieve a list of each type of input the service subscribes to
      
      BaseServiceDto baseServiceDto = baseServiceDtoFactory.newDto(getOptions(), model);
      List<BasicPubSubDto> pubSubMethodDtos = baseServiceDto.getBasicPubSubMethods();
      
      PubSubBridgeDto pubSubBridgeDto = new PubSubBridgeDto(buildManagementService, getOptions());
      pubSubBridgeDto.setProjectName(projectInfo.getDirectoryName());
      pubSubBridgeDto.setPackageName(packageInfo);
      
      pubSubBridgeDto.setProjectDependencies(new LinkedHashSet<>(
               Arrays.asList(projectNamingService.getBaseServiceProjectName(getOptions(), model).getArtifactId(),
                             projectNamingService.getEventsProjectName(getOptions(), model).getArtifactId())));
  
      DefaultParameterCollection dataParameters = new DefaultParameterCollection();
      dataParameters.addParameter(new DefaultParameter<>("dto", pubSubBridgeDto));
       
      unpackSuffixedTemplate(PUBSUB_BRIDGE_GENERATED_BUILD_TEMPLATE_SUFFIX,
         dataParameters,
         outputDirectory,
         false);
      
      for (BasicPubSubDto pubSubMethodDto : pubSubMethodDtos) {
         pubSubBridgeDto = new PubSubBridgeDto(buildManagementService, getOptions());
         pubSubBridgeDto.setProjectName(projectInfo.getDirectoryName());
         pubSubBridgeDto.setPackageName(packageInfo);
         
         //TODO Figure out a different way to do this.  Gives us too many imports.
         //pubSubBridgeDto.setAbstractImports(baseServiceDto.getPubSubImports());
         
         
         //Set up inputs
         InputDto inputDto = pubSubMethodDto.getInput();
         pubSubBridgeDto.setSubscriberClassName(inputDto.getType());
         pubSubBridgeDto.setSubscriberDataType(inputDto.getType());
         pubSubBridgeDto.getImports().add(inputDto.getFullyQualifiedName());
         
         
         //Set up publishes
         PublishDto publishDto = pubSubMethodDto.getOutput();
         pubSubBridgeDto.setPublishDataType(publishDto.getType());
         pubSubBridgeDto.setScenarioMethod(pubSubMethodDto.getServiceMethod());

         pubSubBridgeDto.getImports().add(publishDto.getFullyQualifiedName());
        
         //Retrieve required services and bind/unbind them
         ClassDto classDto = generatorService.getServiceInterfaceDescription(getOptions(), model);
         pubSubBridgeDto.getImports().add(classDto.getFullyQualifiedName());
         pubSubBridgeDto.setService(classDto);
         pubSubBridgeDto.setServiceVarName(classDto.getTypeName());
         
         pubSubBridgeDto.setUnbinderSnippet(pubSubBridgeDto.getServiceVarName());
         pubSubBridgeDto.setBinderSnippet(pubSubBridgeDto.getServiceVarName());
         
         dataParameters = new DefaultParameterCollection();
         dataParameters.addParameter(new DefaultParameter<>("dto", pubSubBridgeDto));
         unpackSuffixedTemplate(PUBSUB_BRIDGE_JAVA_TEMPLATE_SUFFIX,
            dataParameters,
            projectDirectory,
            false);
      }
   }
   
   public void setTemplateDaoFactory(IBaseServiceDtoFactory ref) {
      this.baseServiceDtoFactory = ref;
   }

   public void removeTemplateDaoFactory(IBaseServiceDtoFactory ref) {
      setTemplateDaoFactory(null);
   }
   
   public void removeGenerateService(IJavaServiceGenerationService ref) {
      setJavaServiceGenerationService(null);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeBuildManagementService")
   public void setBuildManagementService(IBuildManagementService ref) {
      super.setBuildManagementService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      super.setTemplateService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
      super.setProjectNamingService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      super.setPackageNamingService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setJavaServiceGenerationService(IJavaServiceGenerationService ref) {
      this.generatorService = ref;
   }


   @Override
   protected IUsage createUsage() {
      return new DefaultUsage(
              "Generates the pubsub bridge which handles the receipt and send of all message types when a"
              + "pubsub event occurs.",
              CommonParameters.GROUP_ID,
              CommonParameters.ARTIFACT_ID,
              CommonParameters.OUTPUT_DIRECTORY.required(),
              CommonParameters.MODEL.required(),
              CommonParameters.CLEAN,
              allPhasesParameter());
   }

}
