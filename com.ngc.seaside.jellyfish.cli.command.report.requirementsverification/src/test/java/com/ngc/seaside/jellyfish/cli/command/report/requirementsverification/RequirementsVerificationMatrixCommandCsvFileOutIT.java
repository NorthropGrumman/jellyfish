package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

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

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RequirementsVerificationMatrixCommandCsvFileOutIT {

   private static final PrintStreamLogService logger = new PrintStreamLogService();
   private static final Injector injector = Guice.createInjector(getModules());
   private static final String TESTFOLDER = "build/test/verification-matrix/results/";
   private static final String TESTREGEX = "([\\n\\r]+\\s*)*$";
   private RequirementsVerificationMatrixCommand cmd;
   private DefaultParameterCollection parameters;
   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;
   private StringTable<Requirement> table;
   private String csv;

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
      Mockito.when(jellyFishCommandOptions.getSystemDescriptorProjectPath())
               .thenReturn(Paths.get("src/test/resources"));

      // Setup class under test
      cmd = new RequirementsVerificationMatrixCommand() {
         @Override
         protected Path evaluateOutput(IJellyFishCommandOptions commandOptions) {
            Path output;
            if (commandOptions.getParameters().containsParameter(OUTPUT_PROPERTY)) {
               String outputUri = commandOptions.getParameters().getParameter(OUTPUT_PROPERTY).getStringValue();
               output = Paths.get(outputUri);
               if (!output.isAbsolute()) {
                  output = Paths.get("build/test/verification-matrix/results/").resolve(output.getFileName());
               }
               return output;
            } else {
               return null;
            }
         }

         @Override
         protected String generateCsvVerificationMatrix(Collection<Requirement> requirements,
                                                        Collection<String> features) {
            csv = super.generateCsvVerificationMatrix(requirements, features);
            return csv;
         }

         @Override
         protected StringTable<Requirement> createStringTable(Collection<String> features) {
            table = super.createStringTable(features);
            return table;
         }
      };
      cmd.setLogService(logger);

      // Setup mock system descriptor
      Path sdDir = Paths.get("src", "test", "resources");
      PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.sd");
      Collection<Path> sdFiles = Files.walk(sdDir).filter(matcher::matches).collect(Collectors.toSet());
      ISystemDescriptorService sdService = injector.getInstance(ISystemDescriptorService.class);
      IParsingResult result = sdService.parseFiles(sdFiles);
      Assert.assertTrue(result.getIssues().toString(), result.isSuccessful());
      ISystemDescriptor sd = result.getSystemDescriptor();
      Mockito.when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(sd);
   }

   @Test
   public void testCsvOutputWithAbsolutePathAndWithDefaultStereotypes() throws IOException {
      Path outputDir = Paths.get("build/test/verification-matrix/results/my/test/test1_csv");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     "Csv"));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY,
                                      outputDir.toAbsolutePath().toString()));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify no tables were created
      assertEquals(null, table);

      // Verify output file
      File result = Paths.get(outputDir.toAbsolutePath().toString()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = csv.replaceAll(TESTREGEX, "");
      String actual = test.replaceAll(TESTREGEX, "");

      assertEquals(expected, actual);
   }

   @Test
   public void testCsvOutputWithRelativePathAndWithDefaultStereotypes() throws IOException {
      Path outputDir = Paths.get("src/main/sd/test/results/test2_csv");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     "CSv"));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY, outputDir.toString()));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify no tables were created
      assertEquals(null, table);

      // Verify output file
      File result = Paths.get(TESTFOLDER).resolve(outputDir.getFileName()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = csv.replaceAll(TESTREGEX, "");
      String actual = test.replaceAll(TESTREGEX, "");

      assertEquals(expected, actual);
   }

   @Test
   public void testCsvOutputWithRelativePathAndWithAdditionalStereotype() throws IOException {
      Path outputDir = Paths.get("src/main/sd/test/results/a/test3_csv.txt");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     "CSV"));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY, outputDir.toString()));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY, "service,system"));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify no tables were created
      assertEquals(null, table);

      // Verify output file
      File result = Paths.get(TESTFOLDER).resolve(outputDir.getFileName()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = csv.replaceAll(TESTREGEX, "");
      String actual = test.replaceAll(TESTREGEX, "");

      assertEquals(expected, actual);
   }

   @Test
   public void testCsvOutputWithRelativePathAndWithAbsentStereotype() throws IOException {
      Path outputDir = Paths.get("src/main/sd/test/results/test4_csv");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     "csv"));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY, outputDir.toString()));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY, "model"));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify no tables were created
      assertEquals(null, table);

      // Verify output file
      File result = Paths.get(TESTFOLDER).resolve(outputDir.getFileName()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = csv.replaceAll(TESTREGEX, "");
      String actual = test.replaceAll(TESTREGEX, "");

      assertEquals(expected, actual);
   }

   @Test
   public void testCsvOutputWithRelavtivePathButWithoutDefaultStereotype() throws IOException {
      Path outputDir = Paths.get("src/main/sd/test/results/test5_csv");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     "csv"));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY, outputDir.toString()));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY, "NOT"));

      cmd.run(jellyFishCommandOptions);

      // Verify no tables were created
      assertEquals(null, table);

      // Verify output file
      File result = Paths.get(TESTFOLDER).resolve(outputDir.getFileName()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = csv.replaceAll(TESTREGEX, "");
      String actual = test.replaceAll(TESTREGEX, "");

      assertEquals(expected, actual);
   }
}
