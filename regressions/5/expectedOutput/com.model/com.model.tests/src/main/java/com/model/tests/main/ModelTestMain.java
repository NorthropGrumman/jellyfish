package com.model.tests.main;

import com.model.tests.steps.ModelSteps;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This application runs the {@link ModelSteps}.
 */
public class ModelTestMain {

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
            args = new String[] { "--glue", ModelSteps.class.getPackage().getName(),
                                  featurePath };
         }

      }
      cucumber.api.cli.Main.main(args);
   }
}
