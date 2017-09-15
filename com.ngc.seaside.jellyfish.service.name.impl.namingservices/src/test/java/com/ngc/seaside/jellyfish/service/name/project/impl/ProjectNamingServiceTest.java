package com.ngc.seaside.jellyfish.service.name.project.impl;

import static org.junit.Assert.assertEquals;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProjectNamingServiceTest {

   private ProjectNamingService service;

   private Model model;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandOptions options;

   @Before
   public void setup() throws Throwable {
      service = new ProjectNamingService();
      service.setLogService(logService);
      service.activate();
   }

   @Test
   public void testDoesGenerateMessageProjectName() throws Throwable {
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");

      IProjectInformation name = service.getMessageProjectName(options, model);
      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval",
                   name.getGroupId());
      assertEquals("artifactId not correct!",
                   "threatevaluation.messages",
                   name.getArtifactId());
      assertEquals("directoryName not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.messages",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationMessagesVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.messages:$threatEvaluationMessagesVersion",
                   name.getGavFormattedString());
   }

   @Test
   public void testDoesGenerateDomainProjectName() throws Throwable {
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");

      IProjectInformation name = service.getDomainProjectName(options, model);
      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval",
                   name.getGroupId());
      assertEquals("artifactId not correct!",
                   "threatevaluation.domain",
                   name.getArtifactId());
      assertEquals("directoryName not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.domain",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationDomainVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.domain:$threatEvaluationDomainVersion",
                   name.getGavFormattedString());
   }

   @Test
   public void testDoesGenerateEventsProjectName() throws Throwable {
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");

      IProjectInformation name = service.getEventsProjectName(options, model);
      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval",
                   name.getGroupId());
      assertEquals("artifactId not correct!",
                   "threatevaluation.events",
                   name.getArtifactId());
      assertEquals("directoryName not correct!",
                   ProjectNamingService.DEFAULT_GENERATED_PROJECTS_DIRECTORY_NAME +
                   "/com.ngc.seaside.threateval.threatevaluation.events",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationEventsVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.events:$threatEvaluationEventsVersion",
                   name.getGavFormattedString());
   }

   @Test
   public void testDoesGenerateServiceProjectName() throws Throwable {
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");

      IProjectInformation name = service.getServiceProjectName(options, model);
      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval",
                   name.getGroupId());
      assertEquals("artifactId not correct!",
                   "threatevaluation.impl",
                   name.getArtifactId());
      assertEquals("directoryName not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.impl",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationImplVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.impl:$threatEvaluationImplVersion",
                   name.getGavFormattedString());
   }

   @Test
   public void testDoesGenerateBaseServiceProjectName() throws Throwable {
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");

      IProjectInformation name = service.getBaseServiceProjectName(options, model);
      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval",
                   name.getGroupId());
      assertEquals("artifactId not correct!",
                   "threatevaluation.base",
                   name.getArtifactId());
      assertEquals("directoryName not correct!",
                   ProjectNamingService.DEFAULT_GENERATED_PROJECTS_DIRECTORY_NAME +
                   "/com.ngc.seaside.threateval.threatevaluation.base",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationBaseServiceVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.base:$threatEvaluationBaseServiceVersion",
                   name.getGavFormattedString());
   }

   @Test
   public void testDoesGenerateRootProjectName() throws Throwable {
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");

      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval.threatevaluation",
                   service.getRootProjectName(options, model));
   }

   private static Model newModel(String modelPackageName, String modelName) {
      Model model = new Model(modelName);
      Package p = new Package(modelPackageName);
      p.addModel(model);
      return model;
   }
}
