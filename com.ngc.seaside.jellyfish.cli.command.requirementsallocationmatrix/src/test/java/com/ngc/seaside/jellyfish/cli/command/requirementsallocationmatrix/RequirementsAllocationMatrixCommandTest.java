package com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.utilities.console.impl.stringtable.StringTable;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFilesEquals;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RequirementsAllocationMatrixCommandTest {
   private static final PrintStreamLogService logger = new PrintStreamLogService();
   private static final Injector injector = Guice.createInjector(getModules());

   private RequirementsAllocationMatrixCommand cmd = new RequirementsAllocationMatrixCommand();

   private DefaultParameterCollection parameters;
   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;
   // private StringTable<Requirement> table;

   private Path outputDir;
   private Path outputFilePath;
   private Path expectedOutputFilesDir = Paths.get("src", "test", "resources", "expectedOutputs");

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.removeIf(m -> m instanceof XTextSystemDescriptorServiceModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      modules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(logger);
         }
      });
      return modules;
   }

   @Before
   public void setup() throws IOException {
      parameters = new DefaultParameterCollection();
      Mockito.when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);

      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();

      // Setup class under test
      cmd.setLogService(logger);

      // Setup mock system descriptor
      Path sdDir = Paths.get("src", "test", "resources", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testCommandWithOptionalParameter_Output() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrix.txt");

      String outputFilename = "requirements-allocation-matrix.txt";
      outputFilePath = outputDir.resolve(outputFilename);

      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OUTPUT_PROPERTY, outputFilePath.toString()));

      runCommand();

      checkCommandOutput(expectedOutputFilePath, outputFilePath);
   }

   // @Test
   // public void testCommandWithRequirementInMetadata() {
   // throw new AssertionError("Test not implemented!");
   // }
   //
   // @Test
   // public void testCommandWithRequirementInMetadataOfInputFields () {
   // throw new AssertionError("Test not implemented!");
   // }
   //
   // @Test
   // public void testCommandWithRequirementInMetadataOfOutputFields () {
   // throw new AssertionError("Test not implemented!");
   // }
   //
   // @Test
   // public void testCommandWithRequirementInMetadataOfPartsFields () {
   // throw new AssertionError("Test not implemented!");
   // }
   //
   // @Test
   // public void testCommandWithRequirementInMetadataOfScenarioFields () {
   // throw new AssertionError("Test not implemented!");
   // }

   private void runCommand(String... keyValues) {
      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         parameters.addParameter(new DefaultParameter<>(keyValues[n], keyValues[n + 1]));
      }

      Mockito.when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);

      cmd.run(jellyFishCommandOptions);
   }

   private void checkCommandOutput(final Path expectedOutputFilePath, final Path outputFilePath) throws IOException {
      // check file existence
      Assert.assertTrue(outputFilePath + " output file not created", outputFilePath.toFile().exists());

      // Check file contents
      assertFilesEquals("Output file content is incorrect", outputFilePath, expectedOutputFilePath);
   }
}
