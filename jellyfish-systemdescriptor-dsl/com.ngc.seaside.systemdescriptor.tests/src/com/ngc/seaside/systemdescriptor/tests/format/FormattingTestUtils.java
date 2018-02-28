package com.ngc.seaside.systemdescriptor.tests.format;

import java.util.Random;
import java.util.function.Function;

import org.eclipse.xtext.formatting2.FormatterRequest;
import org.eclipse.xtext.junit4.formatter.FormatterTestRequest;
import org.eclipse.xtext.junit4.formatter.FormatterTester;

import com.google.inject.Inject;

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
   private FormatterTester formatterTester;

   /**
    * Tests the formatter given correctly-formatted code. This method transform the correctly-formatted code into several mal-formatted code strings, apply the formatter to the mal-formatted code
    * and check that it has been formatted the same as the original correctly-formatted code.
    * 
    * @param correctFormat a correctly-formatted sd file as a string
    */
   public void testFormatter(String correctFormat) {
      testFormat(correctFormat, ONE_LINE_MALFORMATTER, RANDOM_SPACING_MALFORMATTER, PASSTHROUGH_MALFORMATTER);
   }

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
