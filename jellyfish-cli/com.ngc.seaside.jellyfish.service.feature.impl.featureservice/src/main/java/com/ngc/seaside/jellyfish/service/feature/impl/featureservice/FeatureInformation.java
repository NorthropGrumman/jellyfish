package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;

import java.nio.file.Path;

import static org.apache.commons.lang.ArrayUtils.INDEX_NOT_FOUND;

public class FeatureInformation implements IFeatureInformation {

   private String fileName;
   private String fullyQualifiedName;
   private String name;
   private Path absolutePath;
   private Path relativePath;

   /**
    * Constructor
    * @param absolutePath for feature information
    * @param relativePath for feature information
    */
   public FeatureInformation(Path absolutePath, Path relativePath) {
      fullyQualifiedName = substringBetween(absolutePath.getFileName().toString(), "", ".feature");
      fileName = fullyQualifiedName.concat(".feature");
      this.name = substringBetween(absolutePath.getFileName().toString(), ".", ".");
      this.setAbsolutePath(absolutePath);
      this.setRelativePath(relativePath);
   }

   @Override
   public String getFileName() {
      return fileName;
   }

   /**
    * Set the file name of feature file
    *
    * @param fileName the file name
    * @return the feature information
    */
   public FeatureInformation setFileName(String fileName) {
      this.fileName = fileName;
      return this;
   }

   @Override
   public String getFullyQualifiedName() {
      return fullyQualifiedName;
   }

   /**
    * Set the fully qualified name
    *
    * @param fullyQualifiedName the fully qualified name
    * @return the feature information
    */
   public FeatureInformation setFullyQualifiedName(String fullyQualifiedName) {
      this.fullyQualifiedName = fullyQualifiedName;
      return this;
   }

   @Override
   public String getName() {
      return name;
   }

   /**
    * Set the name of this feature
    *
    * @param name the name
    * @return the feature information
    */
   public FeatureInformation setName(String name) {
      this.name = name;
      return this;
   }

   @Override
   public Path getAbsolutePath() {
      return absolutePath;
   }

   /**
    * Sets the absolute path
    *
    * @param absolutePath the absolute path
    */
   public void setAbsolutePath(Path absolutePath) {
      this.absolutePath = absolutePath;
   }

   @Override
   public Path getRelativePath() {
      return relativePath;
   }

   /**
    * Sets the relative path
    */
   public void setRelativePath(Path relativePath) {
      this.relativePath = relativePath;
   }

   /**
    * <p>Gets the String that is nested in between two Strings. Only the first match is returned.</p>
    * <p>A {@code null} input String returns {@code null}. A {@code null} open/close returns {@code null} (no match). An
    * empty ("") open and close returns an empty string.</p>
    *
    * @param str   the String containing the substring, may be null
    * @param open  the String before the substring, may be null
    * @param close the String after the substring, may be null
    * @return the substring, {@code null} if no match
    */
   private static String substringBetween(final String str, final String open, final String close) {
      if (str == null || open == null || close == null) {
         return null;
      }
      final int start = str.indexOf(open);
      if (start != INDEX_NOT_FOUND) {
         final int end = str.indexOf(close, start + open.length());
         if (end != INDEX_NOT_FOUND) {
            return str.substring(start + open.length(), end);
         }
      }
      return null;
   }


   @Override
   public String toString() {
      return "FeatureInformation [fileName=" + fileName + ", fullyQualifiedName=" + fullyQualifiedName + ", name="
            + name + ", absolutePath=" + absolutePath.toString() + ", relativePath=" + relativePath.toString() + "]";
   }
}
