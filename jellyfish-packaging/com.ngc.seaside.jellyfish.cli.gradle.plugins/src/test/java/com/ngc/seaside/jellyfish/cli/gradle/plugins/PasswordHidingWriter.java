/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
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
