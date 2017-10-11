import com.ngc.seaside.jellyfish.cli.gradle.JellyFishProjectGenerator;
import org.gradle.api.logging.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RegressionsIT {  
   
    // Class variable to hold regressions directory
    String regressionTestsDir = "";
   
    @Before
    public void setup() throws Throwable {
        // Set the regressions directory variable
        regressionTestsDir = System.getProperty("user.dir") + "\\regressions";
    }
   
    /**
     * This is the start method for the regression test
    * @throws IOException 
     */
    @Test
    public void testGenerationAndDiff() throws IOException {
        
        String[] subs = new File(regressionTestsDir).list();

        if (subs.length == 0) {
            System.out.println("The directory is empty");
        } else {
            // Loop through the subdirectories under 'regressions' and perform operations
            for (String aFile : subs) {
                String sub = "\\" + aFile;
                String subDir = regressionTestsDir + sub;
                
                // Call to method that runs jellyfish
                generateJellyfishProject(subDir);
            }
        }
    }
   
    /**
     * Run Jellyfish using .properties file information
     * @param directory - the directory of the regression test (ex: 1, 2, etc)
    * @throws IOException 
     */
    private static void generateJellyfishProject(String directory) throws IOException {
        System.out.println("Generating jelly fish project in directory: " + directory);
        
        // Place the newly generated project into a temporary directory
        String tempOutputDirectory = directory + "\\tmp";
        
        // Parse the properties file and store the relevant data
        Map<String, String> propertiesValues = readJellyfishPropertiesFile(directory);
        String relPath = '\\' + propertiesValues.get("inputDir").replace('/', '\\');
        
        String fullInputDirPath = directory + relPath;
        System.out.println("Full input directory path is " + fullInputDirPath);
        System.out.println("Output directory is " + tempOutputDirectory);
        
        Map<String,String> generatorArguments = new HashMap<String, String>();
        System.out.println("Model is " + propertiesValues.get("model"));
        
        generatorArguments.put("outputDirectory", tempOutputDirectory);
        generatorArguments.put("updateGradleSettings", "false");
        generatorArguments.put("version", propertiesValues.get("version"));
        generatorArguments.put("model", propertiesValues.get("model"));
        
        // Create the generation project object
        Logger log = Mockito.mock(Logger.class); 
        JellyFishProjectGenerator proj = new JellyFishProjectGenerator(log)
                 .setCommand(propertiesValues.get("command"))
                 .setInputDir(fullInputDirPath)
                 .setArguments(generatorArguments);

        //System.out.println("generatorArguments include:\n\n" + generatorArguments);
        proj.generate();
        
        
        // At this point, the project has been generated. Now, run 'gradle clean build -x text' on each subproject
        runGradleCleanBuildOnGeneratedProject(tempOutputDirectory);
        
        // TODO: Remove temp directory?
    }
    
    /**
     * Run 'gradle clean build -x test' command for the generated subproject
     * @param directory - the directory of the newly generated regression test project
     */
    private static void runGradleCleanBuildOnGeneratedProject(String directory) {
       System.out.println("Running 'gradle clean build -x test' on newly generated project: " + directory);

       // TODO: Perform the 'gradle clean build -x test' command
       
       diffDefaultAndGeneratedProject(directory);
    }
  
    /**
     * Perform a diff on the generated project and the given project. They should match
     * @param tmp - the directory of the newly generated regression test project
     */
    private static void diffDefaultAndGeneratedProject(String tmp) {
       String defaultDir = tmp.replace("\\tmp", "");
       
       // TODO: Perform the recursive diff on the two directories. If different, throw an exception (fail the test)
       
       System.out.println("Diffing proj at '" + defaultDir + "' and '" + tmp + "'");
    }
    
    private static Map<String, String> readJellyfishPropertiesFile(String dir) throws IOException {
       Map<String, String> props = new HashMap<String, String>();
       File fin = new File(dir + "\\jellyfish.properties");
       System.out.println("FILE is " + fin);
       FileInputStream fis = new FileInputStream(fin);
       
       //Construct BufferedReader from InputStreamReader
       BufferedReader br = new BufferedReader(new InputStreamReader(fis));
     
       String line = null;
       while ((line = br.readLine()) != null) {
          String[] splitLine = line.split("=");
          for (int i = 0; i < splitLine.length; i++) {
             props.put(splitLine[0], splitLine[1]);
          }
       }
     
       br.close();
       return props;
    }
}