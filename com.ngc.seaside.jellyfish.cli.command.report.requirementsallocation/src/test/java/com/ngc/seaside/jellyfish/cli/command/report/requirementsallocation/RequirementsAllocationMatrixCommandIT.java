package com.ngc.seaside.jellyfish.cli.command.report.requirementsallocation;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileLinesEquals;
import static org.mockito.Mockito.when;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;
import com.ngc.seaside.jellyfish.service.requirements.impl.requirementsservice.RequirementsService;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.scenario.impl.module.StepsSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class RequirementsAllocationMatrixCommandIT {
   private static final PrintStreamLogService logger = new PrintStreamLogService();
   private static final RequirementsService reqServiceImpl = new RequirementsService();
   private static final Injector injector = Guice.createInjector(getModules());

   private RequirementsAllocationMatrixCommand cmd = new RequirementsAllocationMatrixCommand();

   private DefaultParameterCollection parameters;

   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   private Path outputDir;
   private Path outputFilePath;
   private Path expectedOutputFilesDir = Paths.get("src", "test", "resources", "expectedOutputs");

   private static Collection<Module> getModules() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      modules.add(new StepsSystemDescriptorServiceModule());
      modules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(logger);
            bind(IRequirementsService.class).toInstance(reqServiceImpl);
         }
      });
      return modules;
   }

   @Before
   public void setup() throws IOException {
      parameters = new DefaultParameterCollection();
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);

      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();

      // Setup class under test
      cmd.setLogService(logger);
      cmd.setRequirementsService(reqServiceImpl);

      // Setup mock system descriptor
      Path sdDir = Paths.get("src", "test", "resources", "sd");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testCommandWithNoParameters() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrix.txt");

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      PrintStream oldOut = System.out;
      System.setOut(new PrintStream(baos));

      runCommand();

      System.setOut(oldOut);

      String output = new String(baos.toByteArray());
      List<String> consoleOutputContent = Arrays.asList(output.split(System.getProperty("line.separator")));

      assertFileLinesEquals("Stdout content does not equal expected output", expectedOutputFilePath, consoleOutputContent);
   }

   @Test
   public void testCommandWithOptionalParameter_Output() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrix.txt");

      outputFilePath = outputDir.resolve("requirements-allocation-matrix.txt");

      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OUTPUT_PROPERTY, outputFilePath.toString()));

      runCommand();

      checkCommandOutput(expectedOutputFilePath, outputFilePath);
   }

   @Test
   public void testCommandWithOptionalParameter_Output_OutputFormat() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrix.csv");

      outputFilePath = outputDir.resolve("requirements-allocation-matrix.csv");

      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OUTPUT_FORMAT_PROPERTY, "csv"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OUTPUT_PROPERTY, outputFilePath.toString()));

      runCommand();

      checkCommandOutput(expectedOutputFilePath, outputFilePath);
   }

   @Test
   public void testCommandWithOptionalParameter_Output_ValuesInputOutput_OperatorOr() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrixWithInputOrOutputStereotype.txt");

      outputFilePath = outputDir.resolve("requirements-allocation-matrix.txt");

      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.VALUES_PROPERTY, "input,output"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OPERATOR_PROPERTY, "OR"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OUTPUT_PROPERTY, outputFilePath.toString()));

      runCommand();

      checkCommandOutput(expectedOutputFilePath, outputFilePath);
   }

   @Test
   public void testCommandWithOptionalParameter_Output_ValuesServiceVirtual_OperatorAnd() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrixWithServiceAndVirtualStereotype.txt");

      outputFilePath = outputDir.resolve("requirements-allocation-matrix.txt");

      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.VALUES_PROPERTY, "service,virtual"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OPERATOR_PROPERTY, "AND"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OUTPUT_PROPERTY, outputFilePath.toString()));

      runCommand();

      checkCommandOutput(expectedOutputFilePath, outputFilePath);
   }

   @Test
   public void testCommandWithOptionalParameter_Output_ValuesService_OperatorNot() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrixWithNotServiceStereotype.txt");

      outputFilePath = outputDir.resolve("requirements-allocation-matrix.txt");

      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.VALUES_PROPERTY, "service"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OPERATOR_PROPERTY, "NOT"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OUTPUT_PROPERTY, outputFilePath.toString()));

      runCommand();

      checkCommandOutput(expectedOutputFilePath, outputFilePath);
   }

   private void runCommand(String... keyValues) {
      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         parameters.addParameter(new DefaultParameter<>(keyValues[n], keyValues[n + 1]));
      }

      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);

      cmd.run(jellyFishCommandOptions);
   }

   private void checkCommandOutput(final Path expectedOutputFilePath, final Path outputFilePath) throws IOException {
      // check file existence
      Assert.assertTrue(outputFilePath + " output file not created", outputFilePath.toFile().exists());

      // Check file contents
      assertFileLinesEquals("Output file content is incorrect", outputFilePath, expectedOutputFilePath);
   }
}
