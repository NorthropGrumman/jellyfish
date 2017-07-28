package com.ngc.seaside.jellyfish.cli.command.createjavaservice;

import com.google.common.base.Preconditions;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao.ITemplateDaoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao.TemplateDaoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dao.TemplateDaoFactoryTest;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaServiceCommandIT {

   //private final static String OUTPUT_DIRECTORY_NAME = "build/tests/CreateJavaServiceCommandIT";

   private CreateJavaServiceCommand command;

   private MockedTemplateService templateService;

   private ITemplateDaoFactory templateDaoFactory;

   private DefaultParameterCollection parameters;

   @Rule
   public final TemporaryFolder outputDirectory = new TemporaryFolder();

   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   @Mock
   private ILogService logService;

   @Mock
   private IPromptUserService promptUserService;

   @Before
   public void setup() throws Throwable {
      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(CreateJavaServiceCommand.class.getPackage().getName(),
                                  Paths.get("src", "main", "template"));

      templateDaoFactory = new TemplateDaoFactory();

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService"))
            .thenReturn(Optional.of(TemplateDaoFactoryTest.newModelForTesting()));

      parameters = new DefaultParameterCollection();
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      command = new CreateJavaServiceCommand();
      command.setLogService(logService);
      command.setPromptService(promptUserService);
      command.setTemplateDaoFactory(templateDaoFactory);
      command.setTemplateService(templateService);
   }

   @Test
   public void testDoesGenerateServiceWithSuppliedCommands() throws Throwable {
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceCommand.MODEL_PROPERTY,
                                                     "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     outputDirectory.getRoot().getAbsolutePath()));

      command.run(jellyFishCommandOptions);

      assertFilesEquals(
            "build.gradle not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-build.gradle.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice", "build.gradle"));

      assertFilesEquals(
            "service unit test not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-test.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "src/test/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/impl/EngagementTrackPriorityServiceTest.java"));

      assertFilesEquals(
            "service not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/impl/EngagementTrackPriorityService.java"));
   }

   @Test
   public void testDoesGenerateServiceWithNoCommands() throws Throwable {
      when(promptUserService.prompt(eq(CreateJavaServiceCommand.MODEL_PROPERTY), eq(null), any()))
            .thenReturn("com.ngc.seaside.threateval.EngagementTrackPriorityService");
      when(promptUserService.prompt(eq(CreateJavaServiceCommand.OUTPUT_DIRECTORY_PROPERTY),
                                    eq(CreateJavaServiceCommand.DEFAULT_OUTPUT_DIRECTORY),
                                    any()))
            .thenReturn(outputDirectory.getRoot().getAbsolutePath());

      command.run(jellyFishCommandOptions);

      assertFilesEquals(
            "build.gradle not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-build.gradle.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice", "build.gradle"));

      assertFilesEquals(
            "service unit test not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-test.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "src/test/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/impl/EngagementTrackPriorityServiceTest.java"));

      assertFilesEquals(
            "service not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/impl/EngagementTrackPriorityService.java"));
   }

   public static void assertFilesEquals(String message, Path expected, Path actual) throws IOException {
      File expectedFile = expected.toFile();
      File actualFile = actual.toFile();
      Preconditions.checkArgument(expectedFile.isFile(), "expected file %s is not a file!", expected);
      assertTrue("expected a file at " + actual,
                 actualFile.isFile());
      assertEquals(message,
                   new String(Files.readAllBytes(expected)),
                   new String(Files.readAllBytes(actual)));
   }
}
