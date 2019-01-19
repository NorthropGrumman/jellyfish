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
package com.ngc.seaside.systemdescriptor.ui.wizard.file;

import com.ngc.seaside.systemdescriptor.ui.wizard.WizardUtils;

import java.io.InputStream;

public class FileWizardUtils {

   /**
    * Creates a default system descriptor file as an {@link InputStream}.
    * 
    * @param packageName package name
    * @param name type name
    * @param elementType element type
    * @return a default system descriptor file as an {@link InputStream}
    */
   public static String createDefaultSd(String packageName, String name, String elementType) {
      StringBuilder file = new StringBuilder();
      file.append(WizardUtils.getFileHeader().getJava());
      file.append('\n');
      if (packageName != null && !packageName.isEmpty()) {
         file.append("package ").append(packageName).append("\n\n");
      }

      switch (elementType.toLowerCase()) {
         case "model":
            file.append("model ")
                     .append(name)
                     .append(" {\n"
                              + "\tmetadata {\n"
                              + "\t\t\"name\" : \"My Model\",\n"
                              + "\t\t\"description\" :  \"My Model description\",\n"
                              + "\t\t\"stereotypes\" : [\"model\", \"example\"]\n"
                              + "\t}\n"
                              + "}\n");
            break;
         case "data":
            file.append("data ").append(name).append(" {\n\n}\n");
            break;
         case "enum":
            file.append("enum ").append(name).append(" {\n\n}\n");
            break;
         default:
            throw new IllegalStateException("Unknown element type: " + elementType);
      }
      return file.toString();
   }

}
