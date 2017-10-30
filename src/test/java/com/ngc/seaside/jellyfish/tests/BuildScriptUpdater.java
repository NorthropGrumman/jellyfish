package com.ngc.seaside.jellyfish.tests;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class BuildScriptUpdater {

   private static final String BACKUP_FILENAME_SUFFIX = ".bk";

   public void updateJellyFishGradlePluginsVersion(Path scriptFile, String version) throws IOException {
      Preconditions.checkNotNull(scriptFile, "scriptFile may not be null!");
      Preconditions.checkNotNull(version, "version may not be null!");
      Preconditions.checkArgument(!version.trim().isEmpty(), "version may not be an empty string!");
      Preconditions.checkArgument(Files.isRegularFile(scriptFile), "%s is not a file!", scriptFile);

      String backupFileName = scriptFile.getFileName().toString() + BACKUP_FILENAME_SUFFIX;
      Path backupFile = scriptFile.getParent().resolve(backupFileName);

      // Create a copy of the file.
      Files.copy(scriptFile, backupFile, StandardCopyOption.REPLACE_EXISTING);

      List<String> lines = Files.readAllLines(scriptFile);
      // Replace the offending lines.
      lines = lines.stream()
            .map(line -> {
               if (line.trim().startsWith("classpath 'com.ngc.seaside:jellyfish.cli.gradle.plugins:")) {
                  line = String.format("      classpath 'com.ngc.seaside:jellyfish.cli.gradle.plugins:%s'", version);
               }
               return line;
            })
            .collect(Collectors.toList());

      // Update the file.
      try (PrintStream os = new PrintStream(Files.newOutputStream(scriptFile,
                                                                  StandardOpenOption.WRITE,
                                                                  StandardOpenOption.TRUNCATE_EXISTING))) {
         lines.forEach(os::println);
      }
   }

   public void restoreScriptFile(Path scriptFile) throws IOException {
      Preconditions.checkNotNull(scriptFile, "scriptFile may not be null!");

      String backupFileName = scriptFile.getFileName().toString() + BACKUP_FILENAME_SUFFIX;
      Path backupFile = scriptFile.getParent().resolve(backupFileName);
      Preconditions.checkState(Files.isRegularFile(backupFile), "no backup file found for %s!", scriptFile);

      // Remove the modified file.
      Files.deleteIfExists(scriptFile);
      // Move the unmodified file back.
      Files.move(backupFile, scriptFile);
   }

}
