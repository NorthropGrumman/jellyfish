package com.ngc.seaside.jellyfish.service.analysis.api;

import java.nio.file.Path;

public class SystemDescriptorFinding {

   
   public String getName() {
      return null;
   }
   
   public Location getLocation() {
      return null;
   }
   
   public String getMessage() {
      return null;
   }
   
   public static interface Location {

      /**
       * Returns the path to the System Descriptor file.
       * 
       * @return the path to the System Descriptor file
       */
      public Path getFile();

      /**
       * Returns the line number where the System Descriptor element starts. Line numbers start at {@code 1}.
       * 
       * @return the line number where the System Descriptor element starts
       */
      public int startLine();

      /**
       * Returns the line number where the System Descriptor element ends. Line numbers start at {@code 1}.
       * 
       * @return the line number where the System Descriptor element ends
       */
      public int endLine();

      /**
       * Returns the character offset from the line of the file where the System Descriptor element
       * {@code #startLine() starts}.
       * 
       * @return the character offset from the line of the file where the System Descriptor element
       *         {@code #startLine() starts}
       */
      public int getStartOffset();

      /**
       * Returns the character offset from the line of the file where the System Descriptor element
       * {@code #endLine() ends}.
       * 
       * @return the character offset from the line of the file where the System Descriptor element
       *         {@code #endLine() ends}
       */
      public int getEndOffset();

   }
   
   public static enum Severity {
      INFO,
      WARNING,
      ERROR,
   }
   
}
