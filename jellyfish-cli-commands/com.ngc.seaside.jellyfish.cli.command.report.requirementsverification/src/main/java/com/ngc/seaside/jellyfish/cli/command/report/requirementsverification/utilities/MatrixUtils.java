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
package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities;

import com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.Requirement;
import com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.RequirementsVerificationItemFormat;
import com.ngc.seaside.jellyfish.utilities.console.api.ITableFormat;
import com.ngc.seaside.jellyfish.utilities.console.impl.stringtable.StringTable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

/**
 * Utility class for matrix related operations
 */
public class MatrixUtils {
   private MatrixUtils() {
   }

   /**
    * Generates a requirements verification matrix given a Collection of requirements and features
    *
    * @param requirements satisfied requirements
    * @param features     satisfied features
    * @return a {@link StringTable} containing verification matrix
    */
   public static StringTable<Requirement> generateDefaultVerificationMatrix(Collection<Requirement> requirements,
                                                                            Collection<String> features) {
      int reqNameWidth = 0;
      for (Requirement eachRequirement : requirements) {
         if (eachRequirement.getID().length() > reqNameWidth) {
            reqNameWidth = eachRequirement.getID().length();
         }
      }

      StringTable<Requirement> stringTable = createStringTable(features, reqNameWidth);

      requirements.forEach(eachRequirement -> stringTable.getModel().addItem(eachRequirement));

      stringTable.setRowSpacer("_");
      stringTable.setColumnSpacer("|");

      stringTable.setShowHeader(true);

      return stringTable;
   }

   /**
    * Creates a {@link StringTable} for {@link Requirement} objects
    *
    * @param features     features features to compare against each {@link Requirement}
    * @param reqNameWidth determines the column width. Calculated from reqId length.
    */
   protected static StringTable<Requirement> createStringTable(Collection<String> features, int reqNameWidth) {
      return new StringTable<>(createTableFormat(features, reqNameWidth));
   }

   /**
    * Creates a {@link ITableFormat} for {@link Requirement} objects
    *
    * @param features     features features to compare against each {@link Requirement}
    * @param reqNameWidth determines the column width. Calculated from reqId length.
    */
   private static ITableFormat<Requirement> createTableFormat(Collection<String> features, int reqNameWidth) {
      return new RequirementsVerificationItemFormat(features, reqNameWidth);
   }

   /**
    * Generates a comma delimited requirements verification matrix given a Collection of requirements and features
    *
    * @param requirements satisfied requirements
    * @param features     satisfied features
    * @return a {@link StringTable} containing verification matrix
    */
   public static String generateCsvVerificationMatrix(Collection<Requirement> requirements,
                                                      Collection<String> features) {
      String commaSeparator = ",";

      StringJoiner sj = new StringJoiner(",");

      StringBuilder sb = new StringBuilder();

      // Process header information
      if (!features.isEmpty()) {
         sb.append("\"Req\"").append(commaSeparator);
         features.forEach(feature -> sj.add("\"" + feature + "\""));
         sb.append(sj.toString()).append("\n");

         requirements.forEach(req -> sb.append(req.createFeatureVerificationCsvString(features)).append("\n"));

         return sb.toString();
      }

      return "";
   }

   /**
    * Prints the verification matrix report to the file provided by the output
    *
    * @param outputPath file path to output
    * @param report     verification matrix to be printed
    */
   public static void printVerificationMatrixToFile(Path outputPath, String report) throws IOException {
      File parent = outputPath.getParent().toAbsolutePath().toFile();
      boolean parentFolderCreationSuccessful = true;
      if (!parent.exists()) {
         parentFolderCreationSuccessful = parent.mkdirs();
      }

      if (parentFolderCreationSuccessful) {
         List<String> test = new ArrayList<>();
         test.add(report);
         Files.write(outputPath, test);
      }
   }

   /**
    * Prints the verification matrix report to the file provided by the output
    *
    * @param report verification matrix to be printed
    */
   public static void printVerificationConsole(String report) {
      System.out.println("\nOUTPUT:\n" + report);
   }
}
