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

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailedSourceLocation implements IDetailedSourceLocation {

   private static final Pattern LINE_PATTERN = Pattern.compile("^.*?(?:\\n|\\r\\n?|\\z)", Pattern.MULTILINE);

   private final Path path;
   private final int lineNumber;
   private final int column;
   private final int length;
   private final int offset;

   /**
    * Constructor
    * 
    * @param path path of location
    * @param lineNumber 1-based line number of location
    * @param column 1-based column of start of location
    * @param length length of location
    * @param offset character offset of location from beginning of file
    */
   public DetailedSourceLocation(Path path, int lineNumber, int column, int length, int offset) {
      this.path = Preconditions.checkNotNull(path, "path cannot be null");
      Preconditions.checkArgument(lineNumber >= 1, "lineNumber must be positive");
      Preconditions.checkArgument(column >= 1, "column must be positive");
      Preconditions.checkArgument(offset >= 0, "offset cannot be negative");
      this.lineNumber = lineNumber;
      this.column = column;
      this.length = length;
      this.offset = offset;
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
   public int getOffset() {
      return offset;
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + "[path=" + getPath() + ",offset=" + getOffset() + ",length=" + getLength()
               + ",line=" + getLineNumber() + ",column=" + getColumn() + "]";
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof IDetailedSourceLocation)) {
         return false;
      }
      IDetailedSourceLocation that = (IDetailedSourceLocation) o;
      return Objects.equals(this.getPath(), that.getPath())
               && this.getOffset() == that.getOffset()
               && this.getLength() == that.getLength()
               && this.getLineNumber() == that.getLineNumber()
               && this.getColumn() == that.getColumn();
   }

   @Override
   public int hashCode() {
      return Objects.hash(getPath(), getOffset(), getLength(), getLineNumber(), getColumn());
   }

   @Override
   public DetailedSourceLocation getSubLocation(int offset, int length) {
      return of(getPath(), getOffset() + offset, length);
   }

   /**
    * Merges the two source locations. Both locations must have the same path.
    * 
    * @param other source location
    * @return merged source location
    */
   public DetailedSourceLocation merge(IDetailedSourceLocation other) {
      if (!Objects.equals(this.getPath(), other.getPath())) {
         throw new IllegalArgumentException(
                  "Cannot merge locations with different paths: " + this.getPath() + " and " + other.getPath());
      }
      Builder builder;
      if (this.getOffset() < other.getOffset()) {
         builder = new Builder(this);
      } else {
         builder = new Builder(other);
      }
      int endOffset = Math.max(this.getOffset() + this.getLength(), other.getOffset() + other.getLength());
      builder.setLength(endOffset - builder.getOffset());
      return builder.build();
   }

   /**
    * Returns a new detailed source location.
    * 
    * @param path path
    * @param lineNumber 1-based line number of the start of the location
    * @param column 1-based column of the start of the location
    * @param length length of location
    * @return detailed source location
    */
   public static DetailedSourceLocation of(Path path, int lineNumber, int column, int length) {
      Builder builder = new Builder().setPath(path).setLineNumber(lineNumber).setColumn(column).setLength(length)
               .setOffset(getFileOffset(path, lineNumber, column));
      return builder.build();
   }

   /**
    * Returns a new detailed source location.
    * 
    * @param path path
    * @param offset character offset from start of file to start of location
    * @param length length of location
    * @return detailed source location
    */
   public static DetailedSourceLocation of(Path path, int offset, int length) {
      Builder builder = new Builder().setPath(path).setOffset(offset).setLength(length);
      try {
         CharSequence fileContent = getContentsFromFile(path);
         Matcher matcher = LINE_PATTERN.matcher(fileContent);
         int currentOffset = 0;
         int lineNumber = 1;
         while (matcher.find()) {
            String line = matcher.group();
            currentOffset += line.length();
            if (currentOffset > builder.getOffset()) {
               builder.setLineNumber(lineNumber);
               builder.setColumn(builder.getOffset() - currentOffset + line.length() + 1);
               break;
            }
            lineNumber++;
         }
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
      return builder.build();
   }

   private static int getFileOffset(Path path, int lineNumber, int column) {
      try {
         CharSequence fileContent = getContentsFromFile(path);
         Matcher matcher = LINE_PATTERN.matcher(fileContent);
         int offset = 0;
         for (int i = 1; i < lineNumber; i++) {
            if (!matcher.find()) {
               throw new IllegalStateException(path + " does not have " + lineNumber + "lines");
            }
            int lineLength = matcher.end() - matcher.start();
            offset += lineLength;
         }
         if (!matcher.find()) {
            throw new IllegalStateException(path + " does not have " + lineNumber + "lines");
         }
         String line = matcher.group();
         if (column > line.length()) {
            throw new IllegalStateException(path + " does not have " + column + " columns on line " + lineNumber);
         }
         offset += column - 1;
         return offset;
      } catch (IOException e) {
         throw new UncheckedIOException(e);
      }
   }

   private static CharSequence getContentsFromFile(Path file) throws IOException {
      FileChannel channel = FileChannel.open(file);
      ByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
      CharBuffer buffer = StandardCharsets.UTF_8.newDecoder().decode(byteBuffer);
      return buffer;
   }

   /**
    * Builder for detailed source locations.
    */
   public static class Builder {
      private Path path;
      private int lineNumber = -1;
      private int column = -1;
      private int length = -1;
      private int offset = -1;

      public Builder() {
      }

      /**
       * Constructs a new builder, initializing its values to the given location.
       * 
       * @param location source location
       */
      public Builder(ISourceLocation location) {
         this.path = location.getPath();
         this.lineNumber = location.getLineNumber();
         this.length = location.getLength();
         this.column = location.getColumn();
         if (location instanceof IDetailedSourceLocation) {
            this.offset = ((IDetailedSourceLocation) location).getOffset();
         } else {
            this.offset = DetailedSourceLocation.getFileOffset(this.path, this.lineNumber, this.column);
         }
      }

      public Path getPath() {
         return path;
      }

      public Builder setPath(Path path) {
         this.path = path;
         return this;
      }

      public int getLineNumber() {
         return lineNumber;
      }

      public Builder setLineNumber(int lineNumber) {
         this.lineNumber = lineNumber;
         return this;
      }

      public int getColumn() {
         return column;
      }

      public Builder setColumn(int column) {
         this.column = column;
         return this;
      }

      public int getLength() {
         return length;
      }

      public Builder setLength(int length) {
         this.length = length;
         return this;
      }

      public int getOffset() {
         return offset;
      }

      public Builder setOffset(int offset) {
         this.offset = offset;
         return this;
      }

      /**
       * Builds the source location.
       * 
       * @return detailed source location
       */
      public DetailedSourceLocation build() {
         return new DetailedSourceLocation(path, lineNumber, column, length, offset);
      }

   }
}
