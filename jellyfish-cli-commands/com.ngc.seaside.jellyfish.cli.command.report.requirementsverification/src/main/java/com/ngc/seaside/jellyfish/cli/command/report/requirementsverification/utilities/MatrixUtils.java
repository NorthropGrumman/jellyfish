/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
