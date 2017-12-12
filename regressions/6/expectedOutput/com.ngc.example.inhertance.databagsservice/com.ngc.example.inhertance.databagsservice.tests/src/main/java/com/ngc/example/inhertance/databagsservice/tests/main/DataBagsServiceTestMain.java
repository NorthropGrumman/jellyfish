package com.ngc.example.inhertance.databagsservice.tests.main;

import com.ngc.example.inhertance.databagsservice.tests.steps.DataBagsServiceSteps;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This application runs the {@link DataBagsServiceSteps}.
 */
public class DataBagsServiceTestMain {

   public static final String APP_HOME_SYS_PROPERTY = "appHome";

   public static void main(String[] args) throws Throwable {
      String appHome = System.getProperty(APP_HOME_SYS_PROPERTY);

      if (args.length == 0) {
         Path folder;
         if (appHome != null && !appHome.trim().equals("")) {
            folder = Paths.get(appHome, "resources");
         } else {
            folder = Paths.get("build", "features");
            System.setProperty("NG_FW_HOME", Paths.get("src", "main").toAbsolutePath().toString());
         }
         if (folder != null) {
            String featurePath = folder.toString();

            String cucumberResultsDir = "build/test-results/cucumber/";

            args = new String[] {
              "--glue", DataBagsServiceSteps.class.getPackage().getName(),
              "--plugin", "pretty",
              "--plugin", "html:" + cucumberResultsDir + "html",
              "--plugin", "junit:" + cucumberResultsDir + "cucumber-results.xml",
              "--plugin", "json:" + cucumberResultsDir + "cucumber-results.json",
              featurePath };
         }

      }
      cucumber.api.cli.Main.main(args);
   }
}