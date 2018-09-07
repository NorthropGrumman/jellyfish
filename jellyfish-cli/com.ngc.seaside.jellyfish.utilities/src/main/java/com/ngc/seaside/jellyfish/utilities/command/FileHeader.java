package com.ngc.seaside.jellyfish.utilities.command;

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

class FileHeader {

   public static final FileHeader DEFAULT_HEADER = new FileHeader();

   private static final String DEFAULT_HEADER_FILE_NAME = "com.ngc.seaside.jellyfish.defaultLicense.txt";

   private final Map<HeaderType, String> headers = new HashMap<>();

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

   public String getJava() {
      return headers.get(HeaderType.JAVA);
   }

   public String getGradle() {
      return headers.get(HeaderType.GRADLE);
   }

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
            StringBuilder header = new StringBuilder(type.getStartBlock())
                  .append(System.lineSeparator());
            drained.forEach(line -> header.append(type.getPrefix()).append(line).append(System.lineSeparator()));
            header.append(type.getEndBlock())
                  .append(System.lineSeparator());
            headers.put(type, header.toString());
         }
      }
   }

   private enum HeaderType {
      JAVA("/**", " * ", " */"),
      GRADLE("/*", " * ", " */"),
      PROPERTIES("#", "# ", "#");

      private final String startBlock;
      private final String prefix;
      private final String endBlock;

      HeaderType(String startBlock, String prefix, String endBlock) {
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
