package com.ngc.seaside.systemdescriptor.service.impl.xtext.source;

import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import org.eclipse.emf.common.util.URI;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

public class XTextSourceLocation implements ISourceLocation {

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
   public XTextSourceLocation(Path path, int lineNumber, int column, int length) {
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
   public XTextSourceLocation(URI uri, int lineNumber, int column, int length) {
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
      if (!(o instanceof XTextSourceLocation)) {
         return false;
      }
      XTextSourceLocation that = (XTextSourceLocation) o;
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
         }
      }
      return path;
   }

}
