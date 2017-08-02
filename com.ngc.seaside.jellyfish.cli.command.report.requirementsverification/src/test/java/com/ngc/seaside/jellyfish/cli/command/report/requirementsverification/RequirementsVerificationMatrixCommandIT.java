package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.utilities.console.impl.stringtable.MultiLineCell;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RequirementsVerificationMatrixCommandIT {

   private static final PrintStreamLogService logger = new PrintStreamLogService();
   private static final Injector injector = Guice.createInjector(getModules());
   private RequirementsVerificationMatrixCommand cmd;
   private DefaultParameterCollection parameters;
   StringTable<Requirement> table;
   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

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

      // Setup class under test
      cmd = new RequirementsVerificationMatrixCommand(){
         @Override
         protected Path getFeatureFilesDirectory(IJellyFishCommandOptions commandOptions) {
            return Paths.get("src/test/resources/").toAbsolutePath().resolve("src/test/gherkin/");
         }

         @Override
         protected StringTable<Requirement> createStringTable(Set<String> features) {
            table = super.createStringTable(features);
            return table;
         }

         @Override
         protected String getGherkinPathPrefix() {
            return "/src/test/gherkin/";
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

//   @Test
//   public void testCommandWithoutOptionalParams() {
//
//   }

   @Test
   public void testStringTableWithDefaultParams() {
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_FORMAT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_FORMAT_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OUTPUT_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OUTPUT_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.SCOPE_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_SCOPE_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.VALUES_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_VALUES_PROPERTY));
      parameters.addParameter(new DefaultParameter<>(RequirementsVerificationMatrixCommand.OPERATOR_PROPERTY,
                                                     RequirementsVerificationMatrixCommand.DEFAULT_OPERATOR_PROPERTY));

      cmd.run(jellyFishCommandOptions);

      // Verify outputs
      List<MultiLineRow> rows = table.getRows();
      assertEquals(4, rows.size());
      MultiLineRow firstRow = rows.get(0);
      assertEquals(1, firstRow.getNumberOfLines());
      assertEquals(10, firstRow.getCells().size());
//      MultiLineCell firstRowFirstCell = firstRow.getCells().get(0);
//      assertEquals(3, firstRowFirstCell.getLines().size());
   }
}
