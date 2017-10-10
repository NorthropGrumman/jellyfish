
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.cli.gradle.JellyFishProjectGenerator;
//import org.gradle.api.logging.Logger;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

//import org.mockito.Mock;

public class RegressionsIT {

    //@Mock
    //private ILogService logService;  // Is this the logger that is needed for JellyFishProjectGenerator?
    
   
    // Class variable to hold regressions directory
    String regressionTestsDir = "";
   
    @Before
    public void setup() throws Throwable {
        // Set the regressions directory variable
        regressionTestsDir = System.getProperty("user.dir") + "\\regressions";
    }
   
    /**
     * This is the start method for the regression test
     */
    @Test
    public void testGenerationAndDiff() {
        
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
     */
    private static void generateJellyfishProject(String directory) {
        System.out.println("Generating jelly fish project in directory: " + directory);
        
        // I suppose we could use a method to read the jellyfish.properties file, parse data, and return an map<String, String>
        //  that allows for easy reading of the data.... or just do the parsing here.
        
        // String command = <derived>;
        // String inputDir = <derived>;
        // String model = <derived>;
        // String version = <derived>;
        
        // Place the newly generated project into a temporary directory
        String tempOutputDirectory = directory + "\\tmp";

        // TODO: create the generation project object
        //Logger log = new Logger();//-------------? 
        //JellyFishProjectGenerator proj = new JellyFishProjectGenerator(log);
        //         .setCommand(command)
        //         .setInputDir(inputDir)
        //         .setArguments(['model'                : model,
        //                        'outputDirectory'      : tempOutputDirectory,
        //                        'updateGradleSettings  : false, 
        //                        'version'              : version]); 
        //proj.generate();
        
        
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
}