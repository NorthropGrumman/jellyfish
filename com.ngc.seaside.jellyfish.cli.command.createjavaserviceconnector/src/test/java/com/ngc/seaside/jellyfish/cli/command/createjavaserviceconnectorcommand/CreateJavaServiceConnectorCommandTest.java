package com.ngc.seaside.jellyfish.cli.command.createjavaserviceconnectorcommand;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.parameter.api.IParameterService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.ITemplateService;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateJavaServiceConnectorCommandTest {

   private CreateJavaServiceConnectorCommand cmd = new CreateJavaServiceConnectorCommand();

   private PrintStreamLogService logger = new PrintStreamLogService();

   private IPromptUserService mockPromptService = Mockito.mock(IPromptUserService.class);

   private Path outputDir;

   @Before
   public void setup() throws IOException {
      outputDir = Files.createTempDirectory(null);
      cmd.setLogService(logger);
      cmd.setPromptService(mockPromptService);
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand applies
   * the required parameter outputDirectory to the project.
   */
   @Test
   public void testRequiredParamOutputDirectoryApplied() {
      // TODO Auto-generated method stub
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand prompts
   * the user if the required parameter outputDirectory is empty.
   */
   @Test
   public void testRequiredParamOutputDirectoryPromptOnEmptyInput() {
      // TODO Auto-generated method stub
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand does not
   * prompt the user if the required parameter outputDirectory is empty,
   * but set in the jellyfish.properties file.
   */
   @Test
   public void testRequiredParamOutputDirectoryNoPromptOnEmptyInputWithJellyfishPropertySet() {
      // TODO Auto-generated method stub
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand applies
   * the optional parameter groupId to the project.
   */
   @Test
   public void testOptionalParamGroupIdIsApplied() {
      // TODO Auto-generated method stub
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand applies
   * the default value if the optional parameter groupId is not set.
   * Default value: package in the model
   */
   @Test
   public void testOptionalParamGroupIdNoInputDefaults() {
      // TODO Auto-generated method stub
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand applies
   * the optional parameter artifactId to the project.
   */
   @Test
   public void testOptionalParamArtifactIdIsApplied() {
      // TODO Auto-generated method stub
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand applies
   * the default value if the optional parameter artifactId is not set.
   * Default value: $modelName.connector
   */
   @Test
   public void testOptionalParamArtifactIdNoInputDefaults() {
      // TODO Auto-generated method stub
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand applies
    * the optional parameter useModelStructure to the project.
    */
   @Test
   public void testOptionalParamUseModelStructureIsApplied() {
      // TODO Auto-generated method stub
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand applies
   * the default value if the optional parameter useModelStructure is not set.
   * Default value: false
   */
   @Test
   public void testOptionalParamUseModelStructureNoInputDefaults() {
      // TODO Auto-generated method stub
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand applies
    * the optional parameter ignoreStereoTypes to the project.
    */
   @Test
   public void testOptionalParamIgnoreStereoTypesStructureIsApplied() {
      // TODO Auto-generated method stub
   }

   /*
    * This test tests that the CreateJavaServiceConnectorCommand applies
    * the optional parameter stereoTypes to the project.
    */
   @Test
   public void testOptionalParamStereoTypesStructureIsApplied() {
      // TODO Auto-generated method stub
   }


   /*
   * This test tests that the CreateJavaServiceConnectorCommand creates
   * the bundle directory's name based on a combination of the groupId and
   * artifactId $(groupId.artifactId).
   */
   @Test
   public void testBundleDirectoryNameIsCombinationGroupIdAndArtifactId() {
      // TODO Auto-generated method stub
   }

   /*
   * This test tests that the CreateJavaServiceConnectorCommand generates
   * a gradle build file that has the necessary blocs bundles
   */
   @Test
   public void testGradleFileContainsNecessaryBlocsFiles() {
      // TODO Auto-generated method stub
   }

   @After
   public void cleanup() throws IOException {
      FileUtils.deleteQuietly(outputDir.toFile());
   }

   private static Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
         IResourceService resourceService = Mockito.mock(IResourceService.class);
         Mockito.when(resourceService.getResourceRootPath()).thenReturn(Paths.get("src", "main", "resources"));

         bind(IResourceService.class).toInstance(resourceService);
         bind(ILogService.class).to(PrintStreamLogService.class);
//         bind(IParameterService.class).to(ParameterServiceGuiceWrapper.class);
//         bind(IPromptUserService.class).to(PromptUserServiceGuiceWrapper.class);
//         bind(ITemplateService.class).to(TemplateServiceGuiceWrapper.class);
//         bind(IPropertyService.class).to(PropertyServiceGuiceWrapper.class);
      }
   });
   
}
