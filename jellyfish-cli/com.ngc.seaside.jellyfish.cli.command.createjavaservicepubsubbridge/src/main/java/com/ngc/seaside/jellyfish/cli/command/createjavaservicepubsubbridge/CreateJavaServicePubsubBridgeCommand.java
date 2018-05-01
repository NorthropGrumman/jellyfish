package com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge;

import java.nio.file.Path;

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
import com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto.PubSubBridgeDto;
import com.ngc.seaside.jellyfish.service.buildmgmt.api.IBuildManagementService;
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
   static final String PUBSUB_BRIDGE_TEMPLATE_SUFFIX = "java";
   
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
      
      //TODO Update projectNamingService so this will work
//      IProjectInformation projectInfo = projectNamingService.getPubSubBridgeProjectName(getOptions(), model);
//      PubSubBridgeDto pubSubBridgeDto = new PubSubBridgeDto();
//      pubSubBridgeDto.setProjectName(projectInfo.getDirectoryName());
//
//      DefaultParameterCollection parameters = new DefaultParameterCollection();
//      parameters.addParameter(new DefaultParameter<>("dto", pubSubBridgeDto));
//      unpackSuffixedTemplate(PUBSUB_BRIDGE_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
//      registerProject(projectInfo);
     
   }

   @Override
   protected void runDeferredPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CommonParameters.CLEAN.getName());
      
      //TODO update project naming service for this to work
//      IProjectInformation projectInfo = projectNamingService.getPubSubBridgeProjectName(getOptions(), model);
//      Path projectDirectory = outputDirectory.resolve(projectInfo.getDirectoryName());
      
      //TODO Retrieve a list of each type of input the service subscribes to
       
      //TODO Do logic here

      //TODO iterate over list and populate DTO ending with an unpack to create multiple templates
//      PubSubBridgeDto pubSubBridgeDto = new PubSubBridgeDto();
      
//      unpackSuffixedTemplate(PUBSUB_BRIDGE_TEMPLATE_SUFFIX,
//         dataParameters,
//         projectDirectory,
//         false);
   }
   

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      super.setLogService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeBuildManagementService")
   public void setBuildManagementService(IBuildManagementService ref) {
      super.setBuildManagementService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeTemplateService")
   public void setTemplateService(ITemplateService ref) {
      super.setTemplateService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeProjectNamingService")
   public void setProjectNamingService(IProjectNamingService ref) {
      super.setProjectNamingService(ref);
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removePackageNamingService")
   public void setPackageNamingService(IPackageNamingService ref) {
      super.setPackageNamingService(ref);
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
