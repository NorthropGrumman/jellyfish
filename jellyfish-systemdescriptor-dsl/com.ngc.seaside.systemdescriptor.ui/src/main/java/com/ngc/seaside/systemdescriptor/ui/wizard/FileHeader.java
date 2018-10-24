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
package com.ngc.seaside.systemdescriptor.ui.wizard;

import com.google.common.base.Preconditions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A type that is added as a parameter when unpacking a template.
 * Note: this file is a copy from jellyfish.utilities project since Eclipse bundling is difficult.
 */
public class FileHeader {

   /**
    * The header that uses Jellyfish's default license.
    */
   public static final FileHeader DEFAULT_HEADER = new FileHeader();

   /**
    * The default license or header that Jellyfish will use when generated files if no other file has been configured.
    */
   private static final String DEFAULT_HEADER_FILE_NAME = "com.ngc.seaside.jellyfish.defaultLicense.txt";

   /**
    * Contains all the headers, keyed by the type of file or language.
    */
   private final Map<HeaderType, String> headers = new HashMap<>();

   /**
    * Creates a new file header by using the given file.
    *
    * @param headerFile the file to use as the header
    */
   FileHeader(Path headerFile) {
      Preconditions.checkNotNull(headerFile, "headerFile may not be null!");
      Preconditions.checkArgument(Files.isReadable(headerFile),
                                  "the file %s does not exists or is not readable!",
                                  headerFile.toAbsolutePath());
      try {
         populateHeaders(Files.lines(headerFile));
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
   }

   /**
    * Creates a default file header by loading the default file from the classpath.
    */
   private FileHeader() {
      InputStream is = FileHeader.class.getClassLoader().getResourceAsStream(DEFAULT_HEADER_FILE_NAME);
      Preconditions.checkState(is != null,
                               "failed to load the default header file from the classpath using the name %s!",
                               DEFAULT_HEADER_FILE_NAME);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
         populateHeaders(reader.lines());
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
   }

   /**
    * Gets the header as plain text.
    */
   public String getPlain() {
      return headers.get(HeaderType.PLAIN);
   }

   /**
    * Gets the header for Java files.
    */
   public String getJava() {
      return headers.get(HeaderType.JAVA);
   }

   /**
    * Gets the header for Gradle files.
    */
   public String getGradle() {
      return headers.get(HeaderType.GRADLE);
   }

   /**
    * Gets the header for Java properties files.
    */
   public String getProperties() {
      return headers.get(HeaderType.PROPERTIES);
   }

   private void populateHeaders(Stream<String> lines) {
      // Can only drain the stream once so do that now.
      Collection<String> drained = lines.collect(Collectors.toList());

      for (HeaderType type : HeaderType.values()) {
         if (drained.isEmpty()) {
            headers.put(type, "");
         } else {
            String prefix = type.getPrefix();
            String header = drained.stream()
                     .map(line -> prefix + line)
                     .collect(Collectors.joining(System.lineSeparator(), type.getStartBlock(), type.getEndBlock()));
            headers.put(type, header);
         }
      }
   }

   /**
    * Defines the different types of comments/languages/files the header can be included in.
    */
   private enum HeaderType {
      PLAIN("", "", ""),
      JAVA("/**\n", " * ", "\n */"),
      GRADLE("/*\n", " * ", "\n */"),
      PROPERTIES("#\n", "# ", "\n#");

      private final String startBlock;
      private final String prefix;
      private final String endBlock;

      HeaderType(String startBlock, String prefix, String endBlock) {
         this.startBlock = startBlock;
         this.prefix = prefix;
         this.endBlock = endBlock;
      }

      /**
       * Gets the string to start a comment block for this type.
       */
      String getStartBlock() {
         return startBlock;
      }

      /**
       * Gets the string to begin a comment for this type.
       */
      String getPrefix() {
         return prefix;
      }

      /**
       * Gets the string to end a comment block for this type.
       */
      String getEndBlock() {
         return endBlock;
      }
   }

}
