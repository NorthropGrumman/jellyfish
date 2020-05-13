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
package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.validation.Check;

import java.util.Arrays;
import java.util.List;

public class PackageValidator extends AbstractUnregisteredSystemDescriptorValidator {

   /**
    * Validates that the user did not try to escape a keyword with ^ in the
    * name of the package.
    *
    * @param pkg is the package to evaluate
    */
   @Check
   public void checkUsageOfEscapeHatCharacter(Package pkg) {
      // Verify the data name doesn't not have the escape hat
      if (pkg.getName().indexOf('^') >= 0) {
         String msg = String.format(
               "Cannot use '^' to escape the package name %s.",
               pkg.getName());
         error(msg, pkg, SystemDescriptorPackage.Literals.PACKAGE__NAME);
      }

   }


   /**
    * Validates that the package properly matches the path of the file.
    *
    * @param pkg is the provided Package to check
    */
   @Check
   public void validatePackageMatchesFilePath(Package pkg) {

      List<String> resourceUriPath = pkg.eResource().getURI().segmentsList();
      List<String> packageElements = Arrays.asList(pkg.getName().split("\\."));

      int indexLast = resourceUriPath.size() - 1;

      // Continue if the package is part of an .sd file and if the 'file' has a valid scheme. Scheme is null if the URI
      // is synthetic (ie. the URI was created as part of a unit test.
      if (resourceUriPath.get(indexLast).indexOf(".sd") > 0 && !pkg.eResource().getURI().scheme().equals("null")) {

         int numElements = packageElements.size();

         // Error out if the package name is longer than the URI allows
         if (indexLast - numElements < 0) {
            StringBuffer sb = new StringBuffer();
            sb.append("Array Size mismatch error:\n");
            sb.append("Last Index of resourceUriPath: " + indexLast + "\n");
            sb.append("Number of Elements in the package elements: " + numElements);
            error(sb.toString(), pkg, SystemDescriptorPackage.Literals.PACKAGE__NAME, SdIssueCodes.MISMATCHED_PACKAGE);
         }

         List<String> uriSublist = resourceUriPath.subList(indexLast - numElements, indexLast);

         // Error out if the package elements are not present in the URI directly infront of the .sd file
         if (!uriSublist.equals(packageElements)) {
            StringBuffer sb = new StringBuffer();
            sb.append("The System Descriptor package and file path do not match.\n\n");
            error(sb.toString(), pkg, SystemDescriptorPackage.Literals.PACKAGE__NAME, SdIssueCodes.MISMATCHED_PACKAGE);
         }
      }
   }
}
