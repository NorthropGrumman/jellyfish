/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.cli.command.createjavaevents;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createjavaevents.dto.EventsDataDto;
import com.ngc.seaside.jellyfish.cli.command.createjavaevents.dto.EventsDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.utilities.command.AbstractMultiphaseJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CreateJavaEventsCommand extends AbstractMultiphaseJellyfishCommand {

   static final String EVENTS_GENERATED_BUILD_TEMPLATE_SUFFIX = "genbuild";
   static final String EVENTS_BUILD_TEMPLATE_SUFFIX = "build";
   static final String EVENTS_JAVA_TEMPLATE_SUFFIX = "java";
   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   /**
    * The name of the command.
    */
   public static final String NAME = "create-java-events";

   private IDataFieldGenerationService dataFieldGenerationService;
   private IDataService dataService;

   public CreateJavaEventsCommand() {
      super(NAME);
   }

   @Override
   protected void runDefaultPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);

      IProjectInformation projectInfo = projectNamingService.getEventsProjectName(getOptions(), model);
      EventsDto eventsDto = new EventsDto();
      eventsDto.setProjectName(projectInfo.getDirectoryName());

      DefaultParameterCollection parameters = new DefaultParameterCollection(getOptions().getParameters());
      parameters.addParameter(new DefaultParameter<>("dto", eventsDto));
      unpackSuffixedTemplate(EVENTS_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);

      registerProject(projectInfo);
   }

   @Override
   protected void runDeferredPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);

      IProjectInformation projectInfo = projectNamingService.getEventsProjectName(getOptions(), model);
      Path projectDirectory = outputDirectory.resolve(projectInfo.getDirectoryName());
      Map<INamedChild<IPackage>, Boolean> map = dataService.aggregateNestedFields(model);

      EventsDto eventsDto = new EventsDto();
      eventsDto.setProjectName(projectInfo.getDirectoryName());
      eventsDto.setExportedPackages(map.keySet()
                                          .stream()
                                          .map(child -> packageNamingService.getEventPackageName(getOptions(), child))
                                          .collect(Collectors.toCollection(TreeSet::new)));

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", eventsDto));
      unpackSuffixedTemplate(EVENTS_GENERATED_BUILD_TEMPLATE_SUFFIX,
                             parameters,
                             outputDirectory,
                             clean);

      map.forEach((child, normal) -> {
         EventsDataDto dataDto = new EventsDataDto();
         dataDto.setPackageName(packageNamingService.getEventPackageName(getOptions(), child));
         dataDto.setClassName(child.getName());
         dataDto.setData(child);
         dataDto.setAbstract(!normal);
         if (child instanceof IData) {
            ((IData) child).getExtendedDataType().ifPresent(superData -> {
               String pkg = packageNamingService.getEventPackageName(getOptions(), superData);
               dataDto.setExtendedClass(pkg + '.' + superData.getName());
            });
         }
         dataDto.setDataService(field -> dataFieldGenerationService.getEventsField(getOptions(), field));
         DefaultParameterCollection dataParameters = new DefaultParameterCollection(getOptions().getParameters());
         dataParameters.addParameter(new DefaultParameter<>("dto", dataDto));
         unpackSuffixedTemplate(EVENTS_JAVA_TEMPLATE_SUFFIX,
                                dataParameters,
                                projectDirectory,
                                false);
      });
   }

   @Override
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Override
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   public void setDataFieldGenerationService(IDataFieldGenerationService ref) {
      this.dataFieldGenerationService = ref;

   }

   public void removeDataFieldGenerationService(IDataFieldGenerationService ref) {
      setDataFieldGenerationService(null);
   }

   public void setDataService(IDataService ref) {
      this.dataService = ref;

   }

   public void removeDataService(IDataService ref) {
      setDataService(null);
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage(
               "Generates a Gradle project containing a service's "
                        + "input and output event sources as generated serialization-agnostic Java types",
               CommonParameters.OUTPUT_DIRECTORY.required(),
               CommonParameters.MODEL.required(),
               CommonParameters.HEADER_FILE.advanced(),
               CommonParameters.CLEAN.optional(),
               allPhasesParameter());
   }
}
