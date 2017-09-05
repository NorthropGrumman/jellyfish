package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests;

import java.nio.file.Path;

public class FeatureFile {
   private Path relativePath;
   private Path absolutePath;

   public FeatureFile(Path absolutePath, Path relativePath) {
      this.absolutePath = absolutePath;
      this.relativePath = relativePath;
   }

   /**
    * File name of feature file
    *
    * @return file name of feature file
    */
   Path getRelativePath() {
      return relativePath;
   }

   /**
    * A qualified name is equal to "modelName.featureName"
    *
    * @return fully qualified name of feature
    */
   Path getAbsolutePath() {
      return absolutePath;
   }

}

