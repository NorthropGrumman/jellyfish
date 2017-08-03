package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.utilities.console.impl.stringtable.MultiLineRow;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RequirementsVerificationMatrixCommandStdFileOutIT {

   private static final PrintStreamLogService logger = new PrintStreamLogService();
   private static final Injector injector = Guice.createInjector(getModules());
   private static final String TESTFOLDER = "build/test/verification-matrix/results/";
   private RequirementsVerificationMatrixCommand cmd;
   private DefaultParameterCollection parameters;
   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;
   private StringTable<Requirement> table;

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
   public void testOutputWithAbsolutePathAndWithDefaultStereotypes() throws IOException {
      Path outputDir = Paths.get("build/matrix-verification/tests/results/my/test/test1_default");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY,
                                      outputDir.toAbsolutePath().toString()));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify table structure
      List<MultiLineRow> rows = table.getRows();
      assertEquals(4, rows.size());
      rows.forEach(row -> {
         assertEquals(1, row.getNumberOfLines());
         assertEquals(10, row.getCells().size());
      });

      // Verify output string
      File result = Paths.get(outputDir.toAbsolutePath().toString()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = table.toString().replaceAll("([\\n\\r]+\\s*)*$", "");
      String actual = test.replaceAll("([\\n\\r]+\\s*)*$", "");

      assertEquals(expected, actual);
      // Uncomment to view files
      Files.delete(result.toPath());
   }

   @Test
   public void testOutputWithRelativePathAndWithDefaultStereotypes() throws IOException {
      Path outputDir = Paths.get("src/main/sd/test/results/test2_default");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY, outputDir.toString()));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify table structure
      List<MultiLineRow> rows = table.getRows();
      assertEquals(4, rows.size());
      rows.forEach(row -> {
         assertEquals(1, row.getNumberOfLines());
         assertEquals(10, row.getCells().size());
      });

      // Verify output string
      File result = Paths.get(TESTFOLDER).resolve(outputDir.getFileName()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = table.toString().replaceAll("([\\n\\r]+\\s*)*$", "");
      String actual = test.replaceAll("([\\n\\r]+\\s*)*$", "");

      assertEquals(expected, actual);
      // Uncomment to view files
      Files.delete(result.toPath());
   }

   @Test
   public void testOutputWithRelativePathAndWithAdditionalStereotype() throws IOException {
      Path outputDir = Paths.get("src/main/sd/test/results/a/test3_default.txt");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY, outputDir.toString()));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY, "service,system"));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify table structure
      List<MultiLineRow> rows = table.getRows();
      assertEquals(5, rows.size());
      rows.forEach(row -> {
         assertEquals(1, row.getNumberOfLines());
         assertEquals(10, row.getCells().size());
      });

      // Verify output string
      File result = Paths.get(TESTFOLDER).resolve(outputDir.getFileName()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = table.toString().replaceAll("([\\n\\r]+\\s*)*$", "");
      String actual = test.replaceAll("([\\n\\r]+\\s*)*$", "");

      assertEquals(expected, actual);
      // Uncomment to view files
      Files.delete(result.toPath());
   }

   @Test
   public void testOutputWithRelativePathAndWithAbsentStereotype() throws IOException {
      Path outputDir = Paths.get("src/main/sd/test/results/test4_default");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY, outputDir.toString()));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY, "model"));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify table structure
      List<MultiLineRow> rows = table.getRows();
      assertEquals(0, rows.size());

      // Verify output string
      File result = Paths.get(TESTFOLDER).resolve(outputDir.getFileName()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = table.toString().replaceAll("([\\n\\r]+\\s*)*$", "");
      String actual = test.replaceAll("([\\n\\r]+\\s*)*$", "");

      assertEquals(expected, actual);
      // Uncomment to view files
      Files.delete(result.toPath());
   }

   @Test
   public void testOutputWithRelavtivePathButWithoutDefaultStereotype() throws IOException {
      Path outputDir = Paths.get("src/main/sd/test/results/test5_default");
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(
               new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY, outputDir.toString()));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY, "NOT"));

      cmd.run(jellyFishCommandOptions);

      // Verify table structure
      List<MultiLineRow> rows = table.getRows();
      assertEquals(1, rows.size());

      // Verify output string
      File result = Paths.get(TESTFOLDER).resolve(outputDir.getFileName()).toFile();
      String test = FileUtils.readFileToString(result, Charset.defaultCharset());

      String expected = table.toString().replaceAll("([\\n\\r]+\\s*)*$", "");
      String actual = test.replaceAll("([\\n\\r]+\\s*)*$", "");

      assertEquals(expected, actual);
   }
}
