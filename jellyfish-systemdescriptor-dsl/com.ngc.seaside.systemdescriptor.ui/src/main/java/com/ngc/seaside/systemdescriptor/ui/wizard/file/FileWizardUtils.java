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
