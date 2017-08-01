package com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RequirementsAllocationMatrixCommandTest {
   private RequirementsAllocationMatrixCommand cmd = new RequirementsAllocationMatrixCommand();
   private PrintStreamLogService logger = new PrintStreamLogService();
   private Path outputDir;
   private final String outputFilename = "requirements-allocation-matrix.txt";
   private Path outputFilePath;
   private List<String> expectedOutput;
   private Path expectedOutputFilesDir = Paths.get("src", "test", "resources", "expectedOutputs");
   private Path sampleModelFilesDir = Paths.get("src", "test", "resources", "sampleModels");

   @Before
   public void setup() throws IOException {
       outputDir = Files.createTempDirectory(null);
       outputDir.toFile().deleteOnExit();
       outputFilePath = Paths.get(outputDir.toString(), outputFilename);
      
       cmd.setLogService(logger);
   }

   @Test
   public void testCommandWithNoRequirements() throws IOException {
	   String modelName = "emptyModelAndNoRequirements";
	   
	   Path sampleModelPath = sampleModelFilesDir.resolve(modelName + ".sd");
       Path expectedOutputFilePath = expectedOutputFilesDir.resolve(modelName + "_ReqAllocationMatrix.txt");
      
       expectedOutput = Files.readAllLines(expectedOutputFilePath);
	      
       runCommand();
       
       checkCommandOutput(expectedOutput);
   }

//   @Test
//   public void testCommandWithRequirementInMetadata() {
//	   throw new AssertionError("Test not implemented!"); 
//   }
//
//   @Test
//   public void testCommandWithRequirementInMetadataOfInputFields () {
//	   throw new AssertionError("Test not implemented!"); 
//   }
//
//   @Test
//   public void testCommandWithRequirementInMetadataOfOutputFields () {
//	   throw new AssertionError("Test not implemented!"); 
//   }
//
//   @Test
//   public void testCommandWithRequirementInMetadataOfPartsFields () {
//	   throw new AssertionError("Test not implemented!"); 
//   }
//
//   @Test
//   public void testCommandWithRequirementInMetadataOfScenarioFields () {
//	   throw new AssertionError("Test not implemented!"); 
//   }

   private void runCommand(String... keyValues) {
      IJellyFishCommandOptions options = Mockito.mock(IJellyFishCommandOptions.class);
      DefaultParameterCollection collection = new DefaultParameterCollection();

      for (int n = 0; n + 1 < keyValues.length; n += 2) {
         collection.addParameter(new DefaultParameter<>(keyValues[n], keyValues[n + 1]));
      }

      Mockito.when(options.getParameters()).thenReturn(collection);

      cmd.run(options);
   }
   
   private void checkCommandOutput(final List<String> expectedOutput)
		   throws IOException {
     // check file existence
     Assert.assertTrue(outputFilename + " output file not created", outputFilePath.toFile().exists());
     
     // Check file contents
     Assert.assertTrue("Output file content is incorrect", 
    		           Files.readAllLines(outputFilePath).equals(expectedOutput));
  }
}
