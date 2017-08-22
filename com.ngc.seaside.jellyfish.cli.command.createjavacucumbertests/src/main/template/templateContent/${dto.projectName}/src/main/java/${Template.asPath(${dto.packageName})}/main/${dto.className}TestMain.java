package ${dto.packageName}.main;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This application runs the {@link ${dto.packageName}.steps.${dto.className}Steps ${dto.className}Steps}.
 */
public class ${dto.className}TestMain {

   public static final String APP_HOME_SYS_PROPERTY = "appHome";

   public static void main(String[] args) throws Throwable {
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
