package ${dto.packageName};

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This application runs the {@link ${dto.className}Steps} which
 * is the Java implementation steps derived from
 * ${dto.className}.calculateTrackPriority.feature.
 *
 * This main can be run from within your IDE as an application or it can run
 * from the command line by running "gradle run". Gradle also creates a run
 * script called ${model} (for linux) and
 * ${model}.bat (for Windows).
 *
 * IMPORTANT: For the tests to run, you must run "start.bat" which is located in
 *            ${dto.packageName}.distribution\
 *            build\distribution\
 *            ${dto.packageName}.distribution-1.1.1-SNAPSHOT\bin
 *
 * @author bperkins
 */
public class ${dto.className} {

   public static final String APP_HOME_SYS_PROPERTY = "appHome";

   public static void main(String args[]) throws Throwable {
      String appHome = System.getProperty(APP_HOME_SYS_PROPERTY);

      if (args.length == 0) {
         Path folder;
         if (appHome != null && !appHome.trim().equals("")) {
            folder = Paths.get(appHome, "resources");
         } else {
            folder = Paths.get("src", "main", "resources");
            System.setProperty("NG_FW_HOME", Paths.get("src", "main").toAbsolutePath().toString());
         }
         if (folder != null) {
            String featurePath = folder.toString();
            args = new String[] { "--glue", ${dto.className}TestMain.class.getPackage().getName(),
                                  featurePath };
         }

      }
      cucumber.api.cli.Main.main(args);
   }
}
