/*
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.examples.tasks;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildScriptUpdater {

   private static final String BACKUP_FILENAME_SUFFIX = ".bk";

   private final Map<Path, Path> scriptsUpdated = new HashMap<>();

   public void updateJellyFishGradlePluginsVersion(Path scriptFile, String version) throws IOException {
      Preconditions.checkNotNull(scriptFile, "scriptFile may not be null!");
      Preconditions.checkNotNull(version, "version may not be null!");
      Preconditions.checkArgument(!version.trim().isEmpty(), "version may not be an empty string!");
      Preconditions.checkArgument(Files.isRegularFile(scriptFile), "%s is not a file!", scriptFile);

      Path backupFile = Files.createTempFile(scriptFile.getFileName().toString(),
                                             BACKUP_FILENAME_SUFFIX);
      backupFile.toFile().deleteOnExit();

      // Create a copy of the file.
      Files.copy(scriptFile, backupFile, StandardCopyOption.REPLACE_EXISTING);

      List<String> lines = Files.readAllLines(scriptFile);
      // Replace the offending lines.
      lines = lines.stream()
            .map(line -> {
               if (line.trim().startsWith("classpath \"com.ngc.seaside:jellyfish.cli.gradle.plugins:")) {
                  line = String.format("      classpath \"com.ngc.seaside:jellyfish.cli.gradle.plugins:%s\"", version);
               }
               else if (line.trim().startsWith("jellyfishVersion = ")) {
                  line = String.format("      jellyfishVersion = '%s'", version);
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
      scriptsUpdated.put(scriptFile, backupFile);
   }

   public void restoreScriptFile(Path scriptFile) throws IOException {
      Preconditions.checkNotNull(scriptFile, "scriptFile may not be null!");

      Path backupFile = scriptsUpdated.get(scriptFile);
      Preconditions.checkState(backupFile != null, "%s was not modified, no backup was created!", scriptFile);
      Preconditions.checkState(Files.isRegularFile(backupFile), "no backup file found for %s!", scriptFile);

      // Remove the modified file.
      Files.deleteIfExists(scriptFile);
      // Move the unmodified file back.
      Files.move(backupFile, scriptFile);
   }

   public void restoreAllScripts() throws IOException {
      scriptsUpdated.keySet().forEach(s -> {
         try {
            restoreScriptFile(s);
         } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
         }
      });
   }
}
