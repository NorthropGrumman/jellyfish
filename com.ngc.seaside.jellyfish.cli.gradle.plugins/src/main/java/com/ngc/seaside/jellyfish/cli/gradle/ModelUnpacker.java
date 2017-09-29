package com.ngc.seaside.jellyfish.cli.gradle;

import com.google.common.base.Preconditions;

import org.apache.commons.io.IOUtils;
import org.gradle.api.GradleException;
import org.gradle.api.artifacts.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModelUnpacker {

   private Path destinationDirectory;

   private Configuration configuration;

   private Supplier<Boolean> executeCondition = () -> true;

   public Collection<Path> unpack() {
      Preconditions.checkNotNull(destinationDirectory, "destinationDirectory may not be null!");
      Preconditions.checkNotNull(configuration, "configuration may not be null!");
      Collection<Path> unpackedFiles = new ArrayList<>();

      if (executeCondition.get()) {
         File dest = destinationDirectory.toFile();
         Preconditions.checkArgument(!dest.exists() || !dest.isDirectory(),
                                     "%s exists but is not a directory!",
                                     destinationDirectory);

         if (!dest.isDirectory()) {
            dest.mkdirs();
         }

         for (File f : configuration) {
            // Strip the extension.
            File destForZip = new File(dest, f.getName().substring(0, f.getName().lastIndexOf('.')));
            unpackedFiles.add(destForZip.toPath());
            unpackFile(f, destForZip);
         }
      }

      return unpackedFiles;
   }

   public Path getDestinationDirectory() {
      return destinationDirectory;
   }

   public ModelUnpacker setDestinationDirectory(Path destinationDirectory) {
      this.destinationDirectory = destinationDirectory;
      return this;
   }

   public ModelUnpacker setDestinationDirectory(File destinationDirectory) {
      this.destinationDirectory = destinationDirectory.toPath();
      return this;
   }

   public Configuration getConfiguration() {
      return configuration;
   }

   public ModelUnpacker setConfiguration(Configuration configuration) {
      this.configuration = configuration;
      return this;
   }

   public Supplier<Boolean> getExecuteCondition() {
      return executeCondition;
   }

   public ModelUnpacker setExecuteCondition(Supplier<Boolean> executeCondition) {
      this.executeCondition = executeCondition;
      return this;
   }

   private void unpackFile(File file, File dest) {
      // Treat the file like a zip.
      try (ZipFile zip = new ZipFile(file)) {
         Enumeration<? extends ZipEntry> entries = zip.entries();
         while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File entryDestination = new File(dest, entry.getName());
            if (entry.isDirectory()) {
               entryDestination.mkdirs();
            } else {
               entryDestination.getParentFile().mkdirs();
               try (OutputStream out = new FileOutputStream(entryDestination)) {
                  IOUtils.copy(zip.getInputStream(entry), out);
               }
            }
         }
      } catch (IOException e) {
         throw new GradleException("failed to unzip " + file + "!", e);
      }
   }
}
