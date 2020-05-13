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
package com.ngc.seaside.jellyfish.utilities.console.impl.stringtable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a methods to wrap a String based on a given length.
 *
 * @author justan.provence@ngc.com
 */
public class StringWrap {

   /**
    * Wrap the string based on the length and return the results in a list.
    *
    * @param str           the value to wrap.
    * @param wrapLength    the number of characters in each line.
    * @param wrapLongWords if the word is longer than the wrapLength it will wrap the word.
    * @return The str value broken up in to a list of strings with each string in the list being at
    * most wrapLength.
    */
   public static List<String> wrap(String str, int wrapLength, boolean wrapLongWords) {
      List<String> lines = new ArrayList<>();
      if (str == null) {
         return null;
      }

      if (wrapLength < 1) {
         wrapLength = 1;
      }
      int inputLineLength = str.length();
      int offset = 0;

      while ((inputLineLength - offset) > wrapLength) {
         if (str.charAt(offset) == ' ') {
            offset++;
            continue;
         }
         int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

         if (spaceToWrapAt >= offset) {
            // normal case
            lines.add(str.substring(offset, spaceToWrapAt));

            offset = spaceToWrapAt + 1;

         } else {
            // really long word or URL
            if (wrapLongWords) {
               // wrap really long word one line at a time
               lines.add(str.substring(offset, wrapLength + offset));

               offset += wrapLength;
            } else {
               // do not wrap really long word, just extend beyond limit
               spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
               if (spaceToWrapAt >= 0) {
                  lines.add(str.substring(offset, spaceToWrapAt));

                  offset = spaceToWrapAt + 1;
               } else {
                  lines.add(str.substring(offset));
                  offset = inputLineLength;
               }
            }
         }
      }

      // Whatever is left in line is short enough to just pass through
      lines.add(str.substring(offset));

      return lines;
   }

}
