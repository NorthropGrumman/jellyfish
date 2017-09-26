package com.ngc.seaside.jellyfish.cli.gradle.tasks;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.nio.file.Paths;

public abstract class AbstractJellyFishCliCommandTask extends DefaultTask {

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

   protected abstract void doExecuteTask();
}
