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
package com.ngc.seaside.jellyfish.service.name.project.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.MetadataNames;
import com.ngc.seaside.jellyfish.service.name.api.IProjectInformation;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.Model;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;

@RunWith(MockitoJUnitRunner.class)
public class ProjectNamingServiceTest {

   private ProjectNamingService service;

   private Model model;

   private DefaultParameterCollection parameters;

   @Mock
   private ILogService logService;

   @Mock
   private IJellyFishCommandOptions options;

   @Before
   public void setup() throws Throwable {
      parameters = new DefaultParameterCollection();
      when(options.getParameters()).thenReturn(parameters);

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
                   ProjectNamingService.DEFAULT_GENERATED_PROJECTS_DIRECTORY_NAME
                         + "/com.ngc.seaside.threateval.threatevaluation.messages",
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
   public void testDoesGenerateDistributeProjectName() throws Throwable {
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");

      IProjectInformation name = service.getDistributionProjectName(options, model);
      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval",
                   name.getGroupId());
      assertEquals("artifactId not correct!",
                   "threatevaluation.distribution",
                   name.getArtifactId());
      assertEquals("directoryName not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.distribution",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationDistributionVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.distribution:$threatEvaluationDistributionVersion",
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
                   ProjectNamingService.DEFAULT_GENERATED_PROJECTS_DIRECTORY_NAME
                         + "/com.ngc.seaside.threateval.threatevaluation.events",
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
                   "threatEvaluationServiceVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.impl:$threatEvaluationServiceVersion",
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
                   ProjectNamingService.DEFAULT_GENERATED_PROJECTS_DIRECTORY_NAME
                         + "/com.ngc.seaside.threateval.threatevaluation.base",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationBaseServiceVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.base:$threatEvaluationBaseServiceVersion",
                   name.getGavFormattedString());
   }

   @Test
   public void testDoesGenerateCucumberTestsProjectName() throws Throwable {
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");

      IProjectInformation name = service.getCucumberTestsProjectName(options, model);
      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval",
                   name.getGroupId());
      assertEquals("artifactId not correct!",
                   "threatevaluation.tests",
                   name.getArtifactId());
      assertEquals("directoryName not correct!",
                   "com.ngc.seaside.threateval.threatevaluation.tests",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationCucumberTestsVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:threatevaluation.tests:$threatEvaluationCucumberTestsVersion",
                   name.getGavFormattedString());
   }

   @Test
   public void testDoesUseCodeGenMetadataToConstructName() throws Throwable {
      JsonObject codegen = Json.createObjectBuilder()
            .add(MetadataNames.CODEGEN_ALIAS, Json.createValue("te"))
            .build();
      model = newModel("com.ngc.seaside.threateval", "ThreatEvaluation");
      model.setMetadata(new Metadata().setJson(Json.createObjectBuilder()
                                                     .add(MetadataNames.CODEGEN, codegen)
                                                     .build()));

      IProjectInformation name = service.getBaseServiceProjectName(options, model);
      assertEquals("groupId not correct!",
                   "com.ngc.seaside.threateval",
                   name.getGroupId());
      assertEquals("artifactId not correct!",
                   "te.base",
                   name.getArtifactId());
      assertEquals("directoryName not correct!",
                   ProjectNamingService.DEFAULT_GENERATED_PROJECTS_DIRECTORY_NAME
                         + "/com.ngc.seaside.threateval.te.base",
                   name.getDirectoryName());
      assertEquals("versionPropertyName not correct!",
                   "threatEvaluationBaseServiceVersion",
                   name.getVersionPropertyName());
      assertEquals("gavFormattedString not correct!",
                   "com.ngc.seaside.threateval:te.base:$threatEvaluationBaseServiceVersion",
                   name.getGavFormattedString());
   }

   private static Model newModel(String modelPackageName, String modelName) {
      Model model = new Model(modelName);
      Package p = new Package(modelPackageName);
      p.addModel(model);
      return model;
   }
}
