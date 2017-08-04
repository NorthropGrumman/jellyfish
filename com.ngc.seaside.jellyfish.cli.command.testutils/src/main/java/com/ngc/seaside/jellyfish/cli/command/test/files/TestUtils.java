package com.ngc.seaside.jellyfish.cli.command.test.files;

import java.io.File;

public class TestUtils {
   private TestUtils() {
   }

   /**
    * Helper method to delete folder/files
    *
    * @param file file/folder to delete
    */
   public static void deleteDir(File file) {
      File[] contents = file.listFiles();
      if (contents != null) {
         for (File f : contents) {
            deleteDir(f);
         }
      }
      file.delete();
   }
}
