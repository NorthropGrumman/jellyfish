package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.ITemplateDtoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceTemplateDaoFactory;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto.BaseServiceTemplateDaoFactoryTest;
import com.ngc.seaside.jellyfish.cli.command.test.template.MockedTemplateService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFilesEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateJavaServiceBaseCommandIT {

   private CreateJavaServiceBaseCommand command;

   private MockedTemplateService templateService;

   private ITemplateDtoFactory templateDaoFactory;

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
      outputDirectory.newFile("settings.gradle");

      templateService = new MockedTemplateService()
            .useRealPropertyService()
            .setTemplateDirectory(CreateJavaServiceBaseCommand.class.getPackage().getName(),
                                  Paths.get("src", "main", "template"));

      templateDaoFactory = new BaseServiceTemplateDaoFactory();

      ISystemDescriptor systemDescriptor = mock(ISystemDescriptor.class);
      when(systemDescriptor.findModel("com.ngc.seaside.threateval.EngagementTrackPriorityService"))
            .thenReturn(Optional.of(BaseServiceTemplateDaoFactoryTest.newModelForTesting()));

      parameters = new DefaultParameterCollection();
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(systemDescriptor);

      command = new CreateJavaServiceBaseCommand();
      command.setLogService(logService);
      command.setPromptService(promptUserService);
      command.setTemplateDaoFactory(templateDaoFactory);
      command.setTemplateService(templateService);
   }

   @Test
   //@Ignore("not passing yet")
   public void testDoesGenerateServiceWithSuppliedCommands() throws Throwable {
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceBaseCommand.MODEL_PROPERTY,
                                                     "com.ngc.seaside.threateval.EngagementTrackPriorityService"));
      parameters.addParameter(new DefaultParameter<>(CreateJavaServiceBaseCommand.OUTPUT_DIRECTORY_PROPERTY,
                                                     outputDirectory.getRoot().getAbsolutePath()));

      command.run(jellyFishCommandOptions);

      assertFilesEquals(
            "build.gradle not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-base-build.gradle.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice.base", "build.gradle"));

      assertFilesEquals(
            "settings.gradle not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-base-settings.gradle.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(), "settings.gradle"));

//      Files.walk(outputDirectory.getRoot().toPath())
//            .forEach(p -> System.out.println(p.toFile().getAbsolutePath()));

      assertFilesEquals(
            "transport topics not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "transport-topics.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice.base",
                      "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/transport/topic/EngagementTrackPriorityServiceTransportTopics.java"));

      assertFilesEquals(
            "interface not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-base-interface.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice.base",
                      "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/api/IEngagementTrackPriorityService.java"));
   }

   @Test
   @Ignore("not passing yet")
   public void testDoesGenerateServiceWithNoCommands() throws Throwable {
      when(promptUserService.prompt(eq(CreateJavaServiceBaseCommand.MODEL_PROPERTY), eq(null), any()))
            .thenReturn("com.ngc.seaside.threateval.EngagementTrackPriorityService");
      when(promptUserService.prompt(eq(CreateJavaServiceBaseCommand.OUTPUT_DIRECTORY_PROPERTY),
                                    eq(CreateJavaServiceBaseCommand.DEFAULT_OUTPUT_DIRECTORY),
                                    any()))
            .thenReturn(outputDirectory.getRoot().getAbsolutePath());

      command.run(jellyFishCommandOptions);

      assertFilesEquals(
            "build.gradle not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-build.gradle.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice", "build.gradle"));

      assertFilesEquals(
            "settings.gradle not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-settings.gradle.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(), "settings.gradle"));

      assertFilesEquals(
            "service unit test not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service-test.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "src/test/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/impl/EngagementTrackPriorityServiceTest.java"));

      assertFilesEquals(
            "service not correct!",
            Paths.get("src", "test", "resources", "expectedfiles", "service.java.expected"),
            Paths.get(outputDirectory.getRoot().getAbsolutePath(),
                      "com.ngc.seaside.threateval.engagementtrackpriorityservice",
                      "src/main/java/com/ngc/seaside/threateval/engagementtrackpriorityservice/impl/EngagementTrackPriorityService.java"));
   }
}
