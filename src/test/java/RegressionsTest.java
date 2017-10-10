
//import com.ngc.seaside.jellyfish.cli.gradle.JellyFishProjectGenerator;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintStream;
import java.lang.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RegressionsTest {

    String regressionTestsDir = "";
   
    @Before
    public void setup() throws Throwable {
        regressionTestsDir = System.getProperty("user.dir") + "\\regressions";
    }
   
    @Test
    public void testGenerationAndDiff() {
        String[] subs = new File(regressionTestsDir).list();

        if (subs.length == 0) {
            System.out.println("The directory is empty");
        } else {
            for (String aFile : subs) {
                String sub = "\\" + aFile;
                String subDir = regressionTestsDir + sub;
                generateJellyfishProject(subDir);
            }
        }
    }
   
    private void generateJellyfishProject(String directory) {
        System.out.println("Generating jelly fish project in directory: " + directory);
        
        
        String tempDirectory = directory + "\\tmp";
        runGradleCleanBuildOnGeneratedProject(tempDirectory);
        // Remove temp directory
    }
    
    private void runGradleCleanBuildOnGeneratedProject(String directory) {
       System.out.println("Running 'gradle clean build -x test' on newly generated project: " + directory);

       diffDefaultAndGeneratedProject(directory);
       
    }
  
    private void diffDefaultAndGeneratedProject(String tmp) {
       String defaultDir = tmp.replace("\\tmp", "");
       System.out.println("Diffing proj at '" + defaultDir + "' and '" + tmp + "'");
    }
}