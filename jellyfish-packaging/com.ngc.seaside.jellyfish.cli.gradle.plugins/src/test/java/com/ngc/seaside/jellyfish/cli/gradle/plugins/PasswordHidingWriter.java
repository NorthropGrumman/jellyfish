package com.ngc.seaside.jellyfish.cli.gradle.plugins;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class PasswordHidingWriter extends PrintWriter {

   private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("(password\\s*?=?)\\s*\\S+", Pattern.CASE_INSENSITIVE);

   public PasswordHidingWriter(OutputStream out) {
      super(out);
   }

   @Override
   public void write(char[] buf, int off, int len) {
      write(new String(buf), off, len);
   }

   @Override
   public void write(String s, int off, int len) {
      s = s.substring(off, len);
      s = PASSWORD_PATTERN.matcher(s).replaceAll("$1**********");
      super.write(s, 0, s.length());
   }

}
