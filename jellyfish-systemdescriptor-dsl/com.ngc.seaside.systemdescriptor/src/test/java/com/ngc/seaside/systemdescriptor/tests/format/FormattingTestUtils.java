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
package com.ngc.seaside.systemdescriptor.tests.format;

import com.google.inject.Inject;

import org.eclipse.xtext.formatting2.FormatterRequest;
import org.eclipse.xtext.testing.formatter.FormatterTestHelper;
import org.eclipse.xtext.testing.formatter.FormatterTestRequest;

import java.util.Random;
import java.util.function.Function;

public class FormattingTestUtils {

   /**
    * Adds a single space character between tokens.
    */
   public static final Function<String, String> ONE_LINE_MALFORMATTER = code -> code.trim().replaceAll("\\s+", " ");

   /**
    * Adds arbitrary whitespace between tokens.
    */
   public static final Function<String, String> RANDOM_SPACING_MALFORMATTER = code -> {
      Random random = new Random(code.length());
      StringBuilder builder = new StringBuilder(" \t\n\r \t\n");
      for (String token : code.split("\\s+")) {
         builder.append(token);
         for (int i = 0; i < 3; i++) {
            if (random.nextBoolean()) {
               builder.append(' ');
            }
            if (random.nextBoolean()) {
               builder.append('\t');
            }
            if (random.nextBoolean()) {
               builder.append('\n');
            }
            if (random.nextBoolean()) {
               builder.append('\r');
            }
         }
      }
      return builder.toString();
   };

   /**
    * Does not change the format of the tokens.
    */
   public static final Function<String, String> PASSTHROUGH_MALFORMATTER = Function.identity();

   @Inject
   private FormatterTestHelper formatterTester;

   /**
    * Tests the formatter given correctly-formatted code. This method transform the correctly-formatted code into
    * several mal-formatted code strings, apply the formatter to the mal-formatted code and check that it has been
    * formatted the same as the original correctly-formatted code.
    *
    * @param correctFormat a correctly-formatted sd file as a string
    */
   public void testFormatter(String correctFormat) {
      testFormat(correctFormat, ONE_LINE_MALFORMATTER, RANDOM_SPACING_MALFORMATTER, PASSTHROUGH_MALFORMATTER);
   }

   /**
    * Tests the formatter given correctly formatted code by applying the given functions that mess up the formatting.
    *
    * @param correctFormat the correctly formatted code
    * @param malformatters the functions to corrupt the formatting
    */
   @SafeVarargs
   public final void testFormat(String correctFormat, Function<String, String>... malformatters) {
      for (Function<String, String> malformatter : malformatters) {
         String badFormat = malformatter.apply(correctFormat);
         FormatterTestRequest testRequest = new FormatterTestRequest();
         testRequest.setExpectation(correctFormat);
         testRequest.setToBeFormatted(badFormat);
         FormatterRequest request = new FormatterRequest();
         testRequest.setRequest(request);
         formatterTester.assertFormatted(testRequest);
      }
   }

}
