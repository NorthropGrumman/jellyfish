package com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix.utilities;

import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;
import com.ngc.seaside.bootstrap.utilities.console.impl.stringtable.StringTable;
import com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix.Requirement;
import com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix.RequirementItemFormat;
import com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix.RequirementsAllocationMatrixCommand;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.io.File;
import java.io.FileWriter;
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
    * Generates a requirements allocation matrix given a Collection of requirements and models
    *
    * @param requirements satisfied requirements
    * @param models models satisfying requirements
    * @return a {@link StringTable} containing verification matrix
    */
   public static StringTable<Requirement> generateDefaultAllocationMatrix(Collection<Requirement> requirements, Collection<IModel> models) {
      int reqNameWidth = 0;
      for (Requirement eachRequirement : requirements) {
         if (eachRequirement.getID().length() > reqNameWidth) {
            reqNameWidth = eachRequirement.getID().length();
         }
      }
      StringTable<Requirement> stringTable = createStringTable(models, reqNameWidth);

      requirements.forEach(eachRequirement -> stringTable.getModel().addItem(eachRequirement));

      stringTable.setRowSpacer("");
      stringTable.setColumnSpacer("|");
      stringTable.setShowHeader(true);

      return stringTable;
   }

   /**
    * Creates a {@link StringTable} for {@link Requirement} objects
    *
    * @param models models to compare against each {@link Requirement}
    */
   private static StringTable<Requirement> createStringTable(Collection<IModel> models, int reqNameWidth) {
      return new StringTable<>(createTableFormat(models, reqNameWidth));
   }

   /**
    * Creates a {@link ITableFormat} for {@link Requirement} objects
    *
    * @param models models to compare against each {@link Requirement}
    */
   private static ITableFormat<Requirement> createTableFormat(Collection<IModel> models, int reqNameWidth) {
      return new RequirementItemFormat(models, reqNameWidth);
   }
   
   /**
    * Generates a comma delimited requirements allocation matrix given a Collection of requirements and models
    *
    * @param requirements satisfied requirements
    * @param models       models satisfying requirements
    * @return a {@link StringTable} containing verification matrix
    */
   public static String generateCsvAllocationMatrix(Collection<Requirement> requirements,
                                                    Collection<IModel> models) {
      String commaSeparator = ",";

      StringJoiner sj = new StringJoiner(",");
      StringBuilder sb = new StringBuilder();

      // Process header information
      if (!models.isEmpty()) {
         sb.append("\"Req\"").append(commaSeparator);
         models.forEach(model -> sj.add("\"" + model.getName() + "\""));
         sb.append(sj.toString()).append("\n");

         requirements.forEach(req -> sb.append(req.createRequirementAllocationCsvString(models)).append("\n"));

         return sb.toString();
      }

      return "";
   }

   /**
    * Prints the verification matrix report to the file provided by the output
    *
    * @param report     allocation matrix to be printed
    * @param outputPath file path to output
    */
   public static void printAllocationMatrixToFile(String report, Path outputPath) throws IOException {
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
    * Prints the allocation matrix report to the console
    *
    * @param report allocation matrix to be printed
    */
   public static void printAllocationConsole(String report) {
      System.out.println(report);
   }
}
