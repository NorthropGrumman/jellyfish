/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source;

import com.ngc.seaside.systemdescriptor.service.api.ParsingException;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import org.eclipse.emf.common.util.URI;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;

public class SourceLocation implements ISourceLocation {

   private final Path path;
   private final int lineNumber;
   private final int column;
   private final int length;

   /**
    * Constructs a source location.
    * 
    * @param path path of location
    * @param lineNumber line number of location
    * @param column offset of location from start of line
    * @param length length of location
    */
   public SourceLocation(Path path, int lineNumber, int column, int length) {
      this.path = path;
      this.lineNumber = lineNumber;
      this.column = column;
      this.length = length;
   }

   /**
    * Constructs a source location.
    * 
    * @param uri uri of location
    * @param lineNumber line number of location
    * @param column offset of location from start of line
    * @param length length of location
    */
   public SourceLocation(URI uri, int lineNumber, int column, int length) {
      this.path = getPathFromUri(uri);
      this.lineNumber = lineNumber;
      this.column = column;
      this.length = length;
   }

   @Override
   public Path getPath() {
      return path;
   }

   @Override
   public int getLineNumber() {
      return lineNumber;
   }

   @Override
   public int getColumn() {
      return column;
   }

   @Override
   public int getLength() {
      return length;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof SourceLocation)) {
         return false;
      }
      SourceLocation that = (SourceLocation) o;
      return Objects.equals(this.path, that.path) && this.lineNumber == that.lineNumber && this.column == that.column
               && this.length == that.length;
   }

   @Override
   public int hashCode() {
      return Objects.hash(path, lineNumber, column, length);
   }

   @Override
   public String toString() {
      return String.format("%s [line %s, col %s, len %s]",
               getPath(),
               getLineNumber(),
               getColumn(),
               getLength());
   }

   private static Path getPathFromUri(URI uri) {
      Path path = null;
      if (uri != null) {
         String fileString = uri.toFileString();
         if (fileString != null) {
            path = new File(fileString).toPath();
         } else {
            String devicePath = uri.devicePath();
            String filePath;
            int index = devicePath.toLowerCase().indexOf(".zip!");
            if (index >= 0) {
               index += 4;
               filePath = devicePath.substring(index + 1);
               devicePath = "jar:" + devicePath.substring(0, index);
            } else {
               throw new ParsingException("Unable to get path for " + uri);
            }
            java.net.URI i;
            try {
               i = new java.net.URI(devicePath);
            } catch (URISyntaxException e) {
               throw new ParsingException(e);
            }
            FileSystem fileSystem;
            try {
               fileSystem = FileSystems.getFileSystem(i);
            } catch (FileSystemNotFoundException e) {
               try {
                  fileSystem = FileSystems.newFileSystem(i, Collections.emptyMap());
               } catch (IOException e2) {
                  throw new ParsingException(e);
               }
            }
            path = fileSystem.getPath(filePath);
         }
      }
      return path;
   }

}
