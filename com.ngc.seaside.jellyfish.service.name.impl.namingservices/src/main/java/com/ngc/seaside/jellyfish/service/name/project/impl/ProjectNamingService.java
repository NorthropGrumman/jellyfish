package com.ngc.seaside.jellyfish.service.name.project.impl;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.jellyfish.service.name.api.IProjectNamingService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = IProjectNamingService.class)
public class ProjectNamingService implements IProjectNamingService {
   
   private static final String GROUP_ID_PROPERTY = "groupId";
   private static final String ARTIFACT_ID_PROPERTY = "artifactId";
   private static final String DOMAIN_ARTIFACT_ID_SUFFIX = "domain";
   private static final String EVENT_ARTIFACT_ID_SUFFIX = "events";

   /**
    * The default name of the directory that will contain generated-projects that should never be edited.
    */
   final static String DEFAULT_GENERATED_PROJECTS_DIRECTORY_NAME = "generated-projects";

   private ILogService logService;

   @Override
   public String getRootProjectName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      String modelPackageName = model.getParent().getName();
      String modelName = model.getName();
      return modelPackageName.toLowerCase() + "." + modelName.toLowerCase();
   }

   @Override
   public IProjectInformation getDomainProjectName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      String modelName = model.getName();
      String versionPropertyName = modelName + "DomainVersion";
      versionPropertyName = versionPropertyName.substring(0, 1).toLowerCase() + versionPropertyName.substring(1);
      String groupId = evaluateGroupId(options, model);
      String artifactId = evaluateArtifactId(options, model, DOMAIN_ARTIFACT_ID_SUFFIX);

      return new ProjectInformation()
            .setGroupId(groupId)
            .setArtifactId(artifactId)
            .setVersionPropertyName(versionPropertyName);
   }
   
   @Override
   public IProjectInformation getEventsProjectName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      String modelPackageName = model.getParent().getName();
      String modelName = model.getName();
      String versionPropertyName = modelName + "EventsVersion";
      versionPropertyName = versionPropertyName.substring(0, 1).toLowerCase() + versionPropertyName.substring(1);
      String artifactId = evaluateArtifactId(options, model, EVENT_ARTIFACT_ID_SUFFIX);

      return new ProjectInformation()
            .setGroupId(modelPackageName.toLowerCase())
            .setArtifactId(artifactId)
            .setVersionPropertyName(versionPropertyName)
            .setGenerated(true)
            .setGeneratedDirectoryName(DEFAULT_GENERATED_PROJECTS_DIRECTORY_NAME);
   }

   @Override
   public IProjectInformation getMessageProjectName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      String modelPackageName = model.getParent().getName();
      String modelName = model.getName();
      String versionPropertyName = modelName + "MessagesVersion";
      versionPropertyName = versionPropertyName.substring(0, 1).toLowerCase() + versionPropertyName.substring(1);

      return new ProjectInformation()
            .setGroupId(modelPackageName.toLowerCase())
            .setArtifactId(modelName.toLowerCase() + ".messages")
            .setVersionPropertyName(versionPropertyName);
   }

   @Activate
   public void activate() {
      logService.debug(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   private String evaluateArtifactId(IJellyFishCommandOptions options, IModel model, String suffix) {
      String artifactId;
      if (options.getParameters().containsParameter(ARTIFACT_ID_PROPERTY)) {
         artifactId = options.getParameters().getParameter(ARTIFACT_ID_PROPERTY).getStringValue();
      } else {
         artifactId = model.getName().toLowerCase() + "." + suffix;
      }
      return artifactId;
   }

   private String evaluateGroupId(IJellyFishCommandOptions options, IModel model) {
      String groupId;
      if (options.getParameters().containsParameter(GROUP_ID_PROPERTY)) {
         groupId = options.getParameters().getParameter(GROUP_ID_PROPERTY).getStringValue();
      } else {
         groupId = model.getParent().getName();
      }
      return groupId;
   }
}
