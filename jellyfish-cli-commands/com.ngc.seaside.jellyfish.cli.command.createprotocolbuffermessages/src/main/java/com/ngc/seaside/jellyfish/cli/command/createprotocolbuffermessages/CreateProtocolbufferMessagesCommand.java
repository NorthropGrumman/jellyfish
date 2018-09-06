/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.dto.MessagesDataDto;
import com.ngc.seaside.jellyfish.cli.command.createprotocolbuffermessages.dto.MessagesDto;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.CommonDependencies;
import com.ngc.seaside.jellyfish.service.codegen.api.IDataFieldGenerationService;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.utilities.command.AbstractMultiphaseJellyfishCommand;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This command generates the message IDL and gradle project structure that will produce the protocol buffer message
 * bundle.
 */
public class CreateProtocolbufferMessagesCommand extends AbstractMultiphaseJellyfishCommand {

   static final String NAME = "create-protocolbuffer-messages";

   static final String MESSAGES_GENERATED_BUILD_TEMPLATE_SUFFIX = "genbuild";
   static final String MESSAGES_PROTO_TEMPLATE_SUFFIX = "proto";
   static final String MESSAGES_BUILD_TEMPLATE_SUFFIX = "build";
   public static final String OUTPUT_DIRECTORY_PROPERTY = CommonParameters.OUTPUT_DIRECTORY.getName();
   public static final String CLEAN_PROPERTY = CommonParameters.CLEAN.getName();

   private IDataFieldGenerationService dataFieldGenerationService;
   private IDataService dataService;

   public CreateProtocolbufferMessagesCommand() {
      super(NAME);
   }

   @Override
   protected void runDefaultPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);

      IProjectInformation projectInfo = projectNamingService.getMessageProjectName(getOptions(), model);
      MessagesDto messagesDto = new MessagesDto();
      messagesDto.setProjectName(projectInfo.getDirectoryName());

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", messagesDto));
      unpackSuffixedTemplate(MESSAGES_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);

      // The template does not need to reference this dependency.  However, we do this so the dependency for the
      // protobuf plugin is registered.
      buildManagementService.registerDependency(getOptions(), CommonDependencies.PROTOBUF_GRADLE_PLUGIN.getGropuId(),
               CommonDependencies.PROTOBUF_GRADLE_PLUGIN.getArtifactId());
      buildManagementService.registerDependency(getOptions(), CommonDependencies.PROTOBUF_JAVA.getGropuId(),
               CommonDependencies.PROTOBUF_JAVA.getArtifactId());
      registerProject(projectInfo);
   }

   @Override
   protected void runDeferredPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CLEAN_PROPERTY);

      IProjectInformation projectInfo = projectNamingService.getMessageProjectName(getOptions(), model);
      Path projectDirectory = outputDirectory.resolve(projectInfo.getDirectoryName());

      Map<INamedChild<IPackage>, Boolean> fields = dataService.aggregateNestedFields(model);
      MessagesDto messagesDto = new MessagesDto();
      messagesDto.setProjectName(projectInfo.getDirectoryName());
      messagesDto.setExportedPackages(fields.keySet()
                                            .stream()
                                            .map(f -> packageNamingService.getMessagePackageName(getOptions(), f))
                                            .collect(Collectors.toCollection(TreeSet::new)));

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", messagesDto));
      unpackSuffixedTemplate(MESSAGES_GENERATED_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);

      fields.forEach((child, normal) -> {
         if (normal) {
            MessagesDataDto dataDto = new MessagesDataDto();
            dataDto.setPackageName(packageNamingService.getMessagePackageName(getOptions(), child));
            dataDto.setClassName(child.getName());
            dataDto.setData(child);
            dataDto.setDataService(field -> dataFieldGenerationService.getMessagesField(getOptions(), field));
            DefaultParameterCollection dataParameters = new DefaultParameterCollection();
            dataParameters.addParameter(new DefaultParameter<>("dto", dataDto));
            unpackSuffixedTemplate(MESSAGES_PROTO_TEMPLATE_SUFFIX,
                                   dataParameters,
                                   projectDirectory,
                                   false);
         }
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
                     + "input and output IDL messages as generated serialization-dependent Java types",
            CommonParameters.GROUP_ID,
            CommonParameters.ARTIFACT_ID,
            CommonParameters.OUTPUT_DIRECTORY.required(),
            CommonParameters.MODEL.required(),
            CommonParameters.CLEAN,
            allPhasesParameter());
   }
}
