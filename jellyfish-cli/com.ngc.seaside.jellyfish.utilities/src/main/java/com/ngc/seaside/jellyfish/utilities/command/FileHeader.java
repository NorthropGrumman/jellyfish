package com.ngc.seaside.jellyfish.utilities.command;

class FileHeader {

   public static final FileHeader DEFAULT_HEADER = null;

   private enum CommentType {
      JAVA("/**", "*", "*/"),
      GRADLE("/*", "*", "*/"),
      PROPERTIES("#", "#", "#");

      private final String startBlock;
      private final String prefix;
      private final String endBlock;

      CommentType(String startBlock, String prefix, String endBlock) {
         this.startBlock = startBlock;
         this.prefix = prefix;
         this.endBlock = endBlock;
      }

      String getStartBlock() {
         return startBlock;
      }

      String getPrefix() {
         return prefix;
      }

      String getEndBlock() {
         return endBlock;
      }
   }

}
