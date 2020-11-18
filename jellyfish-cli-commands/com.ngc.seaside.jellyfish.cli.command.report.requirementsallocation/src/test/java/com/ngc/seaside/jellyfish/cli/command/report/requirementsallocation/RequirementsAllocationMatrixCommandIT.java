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
package com.ngc.seaside.jellyfish.cli.command.report.requirementsallocation;

import static com.ngc.seaside.jellyfish.cli.command.test.files.TestingFiles.assertFileLinesEquals;
import static com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils.getMockNamedChild;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import javax.json.Json;
import javax.json.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.requirements.impl.requirementsservice.RequirementsService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.test.systemdescriptor.ModelUtils.PubSubModel;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RequirementsAllocationMatrixCommandIT {

   private static final AtomicInteger COUNTER = new AtomicInteger();
   private RequirementsAllocationMatrixCommand cmd = new RequirementsAllocationMatrixCommand();

   private DefaultParameterCollection parameters;

   @Mock
   private IJellyFishCommandOptions jellyFishCommandOptions;

   private RequirementsService requirementsService;

   private ByteArrayOutputStream baos = new ByteArrayOutputStream();
   private PrintStream originalPrintStream = System.out;
   private Path outputDir;
   private Path expectedOutputFilesDir = Paths.get("src", "test", "resources", "expectedOutputs");

   @Before
   public void setup() throws IOException {
      System.setOut(new PrintStream(baos));

      parameters = new DefaultParameterCollection();
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);

      outputDir = Files.createTempDirectory(null);
      outputDir.toFile().deleteOnExit();

      requirementsService = new RequirementsService();
      requirementsService.setLogService(mock(ILogService.class));

      // Setup class under test
      cmd.setLogService(mock(ILogService.class));
      cmd.setRequirementsService(requirementsService);

      // Setup mock system descriptor
      ISystemDescriptor sd = mock(ISystemDescriptor.class);
      IModel[] models = {
            new ModelBuilder("com.ngc.seaside.ModelNoRequirements", "model", "service").build(),
            new ModelBuilder("com.ngc.seaside.ModelReqsInMetadata", "model", "service", "virtual")
                  .requirements(Section.MODEL, "REQ001", "REQ002").build(),
            new ModelBuilder("com.ngc.seaside.ModelReqsInMetadataOfInputField", "model", "service", "virtual", "input")
                  .requirements(Section.INPUT, "REQ002", "REQ003").build(),
            new ModelBuilder("com.ngc.seaside.ModelReqsInMetadataOfOutputField", "model", "service", "virtual",
                             "output").requirements(Section.OUTPUT, "REQ001", "REQ003", "REQ004").build(),
            new ModelBuilder("com.ngc.seaside.ModelReqsInMetadataOfPartsField", "model", "service")
                  .requirements(Section.PART, "REQ002", "REQ004", "REQ006").build(),
            new ModelBuilder("com.ngc.seaside.ModelReqsInMetadataOfScenario", "model", "virtual")
                  .requirements(Section.SCENARIO, "REQ001", "REQ003", "REQ007").build(),
            };
      IPackage pkg = mock(IPackage.class);
      when(pkg.getName()).thenReturn("com.ngc.seaside");
      when(pkg.getModels()).thenReturn(namedChildCollectionOf(models));
      when(sd.getPackages()).thenReturn(namedChildCollectionOf(pkg));

      when(jellyFishCommandOptions.getSystemDescriptor()).thenReturn(sd);
   }

   @After
   public void cleanup() {
      System.setOut(originalPrintStream);
   }

   @SafeVarargs
   private static <P, T extends INamedChild<P>> INamedChildCollection<P, T> namedChildCollectionOf(T... values) {
      class NamedChildCollection extends AbstractList<T> implements INamedChildCollection<P, T> {

         @Override
         public Optional<T> getByName(String name) {
            return Stream.of(values).filter(value -> value.getName().equals(name)).findAny();
         }

         @Override
         public T get(int index) {
            return values[index];
         }

         @Override
         public int size() {
            return values.length;
         }
      }

      return new NamedChildCollection();
   }

   private enum Section {
      MODEL,
      INPUT,
      OUTPUT,
      PART,
      SCENARIO
   }

   private static class ModelBuilder {

      private final PubSubModel model;
      private final Metadata modelMetadata;
      private final List<IModelReferenceField> parts = new ArrayList<>();

      public ModelBuilder(String name, String... stereotypes) {
         this.modelMetadata = new Metadata();
         model = new PubSubModel(name) {
            @Override
            public IMetadata getMetadata() {
               return modelMetadata;
            }

            @Override
            public INamedChildCollection<IModel, IModelReferenceField> getParts() {
               return namedChildCollectionOf(parts.toArray(new IModelReferenceField[parts.size()]));
            }
         };

         addJsonArray(modelMetadata, "stereotypes", stereotypes);
      }

      private void addJsonArray(Metadata metadata, String key, String... values) {
         JsonObject object = metadata.getJson();
         if (object == null) {
            object = Json.createObjectBuilder().add(key, Json.createArrayBuilder(Arrays.asList(values))).build();
         } else {
            object = Json.createObjectBuilder(object).add(key, Json.createArrayBuilder(Arrays.asList(values))).build();
         }
         metadata.setJson(object);
      }

      private void addValues(Section section, String key, String... values) {
         final Metadata metadata;
         switch (section) {
            case INPUT:
               IData input = getMockNamedChild(IData.class, "com.ngc.Data" + COUNTER.incrementAndGet());
               IDataReferenceField inputField = model.addOutput("output" + COUNTER.incrementAndGet(), input);
               when(inputField.getMetadata()).thenReturn(metadata = new Metadata());
               break;
            case MODEL:
               metadata = modelMetadata;
               break;
            case OUTPUT:
               IData output = getMockNamedChild(IData.class, "com.ngc.Data" + COUNTER.incrementAndGet());
               IDataReferenceField outputField = model.addOutput("output" + COUNTER.incrementAndGet(), output);
               when(outputField.getMetadata()).thenReturn(metadata = new Metadata());
               break;
            case PART:
               IModel part = getMockNamedChild(IModel.class, "com.ngc.Model" + COUNTER.incrementAndGet());
               IModelReferenceField modelField = mock(IModelReferenceField.class);
               when(modelField.getParent()).thenReturn(part);
               when(modelField.getName()).thenReturn("part" + COUNTER.incrementAndGet());
               when(modelField.getMetadata()).thenReturn(metadata = new Metadata());
               parts.add(modelField);
               break;
            case SCENARIO:
               IScenario scenario = mock(IScenario.class);
               when(scenario.getName()).thenReturn("scenario" + COUNTER.incrementAndGet());
               when(scenario.getMetadata()).thenReturn(metadata = new Metadata());
               model.addScenario(scenario);
               break;
            default:
               fail("Invalid section: " + section);
               return;
         }
         addJsonArray(metadata, key, values);
      }

      public ModelBuilder requirements(Section section, String... requirements) {
         addValues(section, "satisfies", requirements);
         return this;
      }

      public IModel build() {
         return model;
      }
   }

   @Test
   public void testCommand() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrix.txt");

      runCommand();

      String output = new String(baos.toByteArray());
      List<String> consoleOutputContent = Arrays.asList(output.split("\\v+"));

      assertFileLinesEquals("Stdout content does not equal expected output", expectedOutputFilePath,
                            consoleOutputContent);
   }

   @Test
   public void testCommandWithCsv() throws IOException {
      Path expectedOutputFilePath = expectedOutputFilesDir.resolve("expectedReqAllocationMatrix.csv");

      parameters
            .addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OUTPUT_FORMAT_PROPERTY, "csv"));

      runCommand();

      String output = new String(baos.toByteArray());
      List<String> consoleOutputContent = Arrays.asList(output.split("\\v+"));

      assertFileLinesEquals("Stdout content does not equal expected output", expectedOutputFilePath,
                            consoleOutputContent);
   }

   @Test
   public void testCommandOrOperator() throws IOException {
      Path
            expectedOutputFilePath =
            expectedOutputFilesDir.resolve("expectedReqAllocationMatrixWithInputOrOutputStereotype.txt");

      parameters
            .addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.VALUES_PROPERTY, "input,output"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OPERATOR_PROPERTY, "OR"));

      runCommand();

      String output = new String(baos.toByteArray());
      List<String> consoleOutputContent = Arrays.asList(output.split("\\v+"));

      assertFileLinesEquals("Stdout content does not equal expected output", expectedOutputFilePath,
                            consoleOutputContent);
   }

   @Test
   public void testCommandAndOperator() throws IOException {
      Path
            expectedOutputFilePath =
            expectedOutputFilesDir.resolve("expectedReqAllocationMatrixWithServiceAndVirtualStereotype.txt");

      parameters.addParameter(
            new DefaultParameter<>(RequirementsAllocationMatrixCommand.VALUES_PROPERTY, "service,virtual"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OPERATOR_PROPERTY, "AND"));

      runCommand();

      String output = new String(baos.toByteArray());
      List<String> consoleOutputContent = Arrays.asList(output.split("\\v+"));

      assertFileLinesEquals("Stdout content does not equal expected output", expectedOutputFilePath,
                            consoleOutputContent);
   }

   @Test
   public void testCommandNotOperator() throws IOException {
      Path
            expectedOutputFilePath =
            expectedOutputFilesDir.resolve("expectedReqAllocationMatrixWithNotServiceStereotype.txt");

      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.VALUES_PROPERTY, "service"));
      parameters.addParameter(new DefaultParameter<>(RequirementsAllocationMatrixCommand.OPERATOR_PROPERTY, "NOT"));

      runCommand();

      String output = new String(baos.toByteArray());
      List<String> consoleOutputContent = Arrays.asList(output.split("\\v+"));

      assertFileLinesEquals("Stdout content does not equal expected output", expectedOutputFilePath,
                            consoleOutputContent);
   }

   private void runCommand() {
      when(jellyFishCommandOptions.getParameters()).thenReturn(parameters);

      cmd.run(jellyFishCommandOptions);
   }

}
