package com.ngc.seaside.jellyfish.cli.gradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Paths;

/**
 * Base class for JellyFish Gradle tasks.  All this class does is set the "NG_FW_HOME" system property if the property
 * is not already set.  This gets rid of the warnings about the property not be set when executing some services.
 */
public abstract class AbstractJellyFishCliCommandTask extends DefaultTask {

   /**
    * execute the Jellyfish command task
    */
   @TaskAction
   public void executeTask() {
      String previousProperty = System.getProperty("NG_FW_HOME");
      boolean isPropertySet = previousProperty != null && previousProperty.trim().equals("");
      if (!isPropertySet) {
         System.setProperty("NG_FW_HOME", Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString());
      }
      try {
         doExecuteTask();
      } finally {
         if (!isPropertySet) {
            System.clearProperty("NG_FW_HOME");
         }
      }
   }

   /**
    * Invoked to perform the actual execution of the task.
    */
   protected abstract void doExecuteTask();
}
