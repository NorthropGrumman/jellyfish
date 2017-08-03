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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RequirementsVerificationMatrixCommandStdOutIT {

   private static final PrintStreamLogService logger = new PrintStreamLogService();
   private static final Injector injector = Guice.createInjector(getModules());
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
   public void testStringTableWithDefaultStereotypes() {
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
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

      // Validate First Requirement
      MultiLineRow firstRow = rows.get(0);
      assertEquals("", firstRow.getCells().get(1).getLine(0));
      assertEquals("X", firstRow.getCells().get(2).getLine(0));
      assertEquals("", firstRow.getCells().get(3).getLine(0));
      assertEquals("X", firstRow.getCells().get(4).getLine(0));
      assertEquals("", firstRow.getCells().get(5).getLine(0));
      assertEquals("X", firstRow.getCells().get(6).getLine(0));
      assertEquals("", firstRow.getCells().get(7).getLine(0));
      assertEquals("", firstRow.getCells().get(8).getLine(0));
      assertEquals("X", firstRow.getCells().get(9).getLine(0));

      // Validate Second Requirement
      MultiLineRow secondRow = rows.get(1);
      assertEquals("", secondRow.getCells().get(1).getLine(0));
      assertEquals("", secondRow.getCells().get(2).getLine(0));
      assertEquals("X", secondRow.getCells().get(3).getLine(0));
      assertEquals("", secondRow.getCells().get(4).getLine(0));
      assertEquals("", secondRow.getCells().get(5).getLine(0));
      assertEquals("", secondRow.getCells().get(6).getLine(0));
      assertEquals("", secondRow.getCells().get(7).getLine(0));
      assertEquals("", secondRow.getCells().get(8).getLine(0));
      assertEquals("X", secondRow.getCells().get(9).getLine(0));

      // Validate Third Requirement
      MultiLineRow thirdRow = rows.get(2);
      assertEquals("X", thirdRow.getCells().get(1).getLine(0));
      assertEquals("", thirdRow.getCells().get(2).getLine(0));
      assertEquals("", thirdRow.getCells().get(3).getLine(0));
      assertEquals("", thirdRow.getCells().get(4).getLine(0));
      assertEquals("", thirdRow.getCells().get(5).getLine(0));
      assertEquals("", thirdRow.getCells().get(6).getLine(0));
      assertEquals("", thirdRow.getCells().get(7).getLine(0));
      assertEquals("", thirdRow.getCells().get(8).getLine(0));
      assertEquals("X", thirdRow.getCells().get(9).getLine(0));

      // Validate Fourth Requirement
      MultiLineRow fourthRow = rows.get(3);
      assertEquals("", fourthRow.getCells().get(1).getLine(0));
      assertEquals("", fourthRow.getCells().get(2).getLine(0));
      assertEquals("", fourthRow.getCells().get(3).getLine(0));
      assertEquals("", fourthRow.getCells().get(4).getLine(0));
      assertEquals("", fourthRow.getCells().get(5).getLine(0));
      assertEquals("X", fourthRow.getCells().get(6).getLine(0));
      assertEquals("", fourthRow.getCells().get(7).getLine(0));
      assertEquals("", fourthRow.getCells().get(8).getLine(0));
      assertEquals("", fourthRow.getCells().get(9).getLine(0));
   }

   @Test
   public void testStringTableWithAdditionalStereotype() {
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
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

      // First Requirement
      MultiLineRow firstRow = rows.get(0);
      assertEquals("", firstRow.getCells().get(1).getLine(0));
      assertEquals("X", firstRow.getCells().get(2).getLine(0));
      assertEquals("", firstRow.getCells().get(3).getLine(0));
      assertEquals("X", firstRow.getCells().get(4).getLine(0));
      assertEquals("", firstRow.getCells().get(5).getLine(0));
      assertEquals("X", firstRow.getCells().get(6).getLine(0));
      assertEquals("", firstRow.getCells().get(7).getLine(0));
      assertEquals("", firstRow.getCells().get(8).getLine(0));
      assertEquals("X", firstRow.getCells().get(9).getLine(0));

      // Second Requirement
      MultiLineRow secondRow = rows.get(1);
      assertEquals("", secondRow.getCells().get(1).getLine(0));
      assertEquals("", secondRow.getCells().get(2).getLine(0));
      assertEquals("X", secondRow.getCells().get(3).getLine(0));
      assertEquals("", secondRow.getCells().get(4).getLine(0));
      assertEquals("", secondRow.getCells().get(5).getLine(0));
      assertEquals("", secondRow.getCells().get(6).getLine(0));
      assertEquals("", secondRow.getCells().get(7).getLine(0));
      assertEquals("", secondRow.getCells().get(8).getLine(0));
      assertEquals("X", secondRow.getCells().get(9).getLine(0));

      // Third Requirement
      MultiLineRow thirdRow = rows.get(2);
      assertEquals("X", thirdRow.getCells().get(1).getLine(0));
      assertEquals("", thirdRow.getCells().get(2).getLine(0));
      assertEquals("", thirdRow.getCells().get(3).getLine(0));
      assertEquals("", thirdRow.getCells().get(4).getLine(0));
      assertEquals("", thirdRow.getCells().get(5).getLine(0));
      assertEquals("", thirdRow.getCells().get(6).getLine(0));
      assertEquals("", thirdRow.getCells().get(7).getLine(0));
      assertEquals("", thirdRow.getCells().get(8).getLine(0));
      assertEquals("X", thirdRow.getCells().get(9).getLine(0));

      // Fourth Requirement
      MultiLineRow fourthRow = rows.get(3);
      assertEquals("", fourthRow.getCells().get(1).getLine(0));
      assertEquals("", fourthRow.getCells().get(2).getLine(0));
      assertEquals("", fourthRow.getCells().get(3).getLine(0));
      assertEquals("", fourthRow.getCells().get(4).getLine(0));
      assertEquals("", fourthRow.getCells().get(5).getLine(0));
      assertEquals("X", fourthRow.getCells().get(6).getLine(0));
      assertEquals("", fourthRow.getCells().get(7).getLine(0));
      assertEquals("", fourthRow.getCells().get(8).getLine(0));
      assertEquals("", fourthRow.getCells().get(9).getLine(0));

      // Fifth Requirement
      MultiLineRow fifthRow = rows.get(4);
      assertEquals("", fifthRow.getCells().get(1).getLine(0));
      assertEquals("", fifthRow.getCells().get(2).getLine(0));
      assertEquals("", fifthRow.getCells().get(3).getLine(0));
      assertEquals("", fifthRow.getCells().get(4).getLine(0));
      assertEquals("", fifthRow.getCells().get(5).getLine(0));
      assertEquals("", fifthRow.getCells().get(6).getLine(0));
      assertEquals("X", fifthRow.getCells().get(7).getLine(0));
      assertEquals("", fifthRow.getCells().get(8).getLine(0));
      assertEquals("", fifthRow.getCells().get(9).getLine(0));
   }

   @Test
   public void testStringTableWithAbsentStereotype() {
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY, "model"));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify table structure
      List<MultiLineRow> rows = table.getRows();
      assertEquals(0, rows.size());
   }

   @Test
   public void testStringTableWithoutDefaultStereotype() {
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY, "NOT"));

      cmd.run(jellyFishCommandOptions);

      // Verify table structure
      List<MultiLineRow> rows = table.getRows();
      assertEquals(1, rows.size());
      rows.forEach(row -> {
         assertEquals(1, row.getNumberOfLines());
         assertEquals(10, row.getCells().size());
      });

      // First Requirement
      MultiLineRow firstRow = rows.get(0);
      assertEquals("", firstRow.getCells().get(1).getLine(0));
      assertEquals("X", firstRow.getCells().get(2).getLine(0));
      assertEquals("", firstRow.getCells().get(3).getLine(0));
      assertEquals("X", firstRow.getCells().get(4).getLine(0));
      assertEquals("", firstRow.getCells().get(5).getLine(0));
      assertEquals("X", firstRow.getCells().get(6).getLine(0));
      assertEquals("", firstRow.getCells().get(7).getLine(0));
      assertEquals("", firstRow.getCells().get(8).getLine(0));
      assertEquals("X", firstRow.getCells().get(9).getLine(0));

   }
}
