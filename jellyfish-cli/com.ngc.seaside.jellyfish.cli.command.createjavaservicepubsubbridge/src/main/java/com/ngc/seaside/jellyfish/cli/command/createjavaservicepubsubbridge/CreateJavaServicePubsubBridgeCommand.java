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
import com.ngc.seaside.jellyfish.cli.command.createjavaservicepubsubbridge.dto.IPubSubBridgeDtoFactory;
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
   
   private IPubSubBridgeDtoFactory templateDaoFactory;

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
      
      IProjectInformation projectInfo = projectNamingService.getBaseServiceProjectName(getOptions(), model);
      PubSubBridgeDto dto = templateDaoFactory.newDto(getOptions(), model);

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(PUBSUB_BRIDGE_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
      registerProject(projectInfo);
     
   }

   @Override
   protected void runDeferredPhase() {
      IModel model = getModel();
      Path outputDirectory = getOutputDirectory();
      boolean clean = getBooleanParameter(CommonParameters.CLEAN.getName());

      PubSubBridgeDto dto = templateDaoFactory.newDto(getOptions(), model);

      DefaultParameterCollection parameters = new DefaultParameterCollection();
      parameters.addParameter(new DefaultParameter<>("dto", dto));
      unpackSuffixedTemplate(PUBSUB_BRIDGE_GENERATED_BUILD_TEMPLATE_SUFFIX, parameters, outputDirectory, clean);
   }
   
   public void setTemplateDaoFactory(IPubSubBridgeDtoFactory ref) {
      this.templateDaoFactory = ref;
   }

   public void removeTemplateDaoFactory(IPubSubBridgeDtoFactory ref) {
      setTemplateDaoFactory(null);
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
