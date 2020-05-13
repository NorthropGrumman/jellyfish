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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.utils;

/**
 * Construct for managing java imports in a template.
 */
public interface IImportManager {

   /**
    * Sets the package for the java class containing the imports.
    * 
    * @param pkg package
    */
   void setPackage(String pkg);

   /**
    * Adds the given import to this manager. It is up to the implementation to determine how to handle conflicting
    * imports and wildcard imports.
    * 
    * @param imp the import to add
    */
   void add(String imp);

   /**
    * Adds the given static import to this manager. It is up to the implementation to determine how to handle
    * conflicting imports and wildcard imports.
    * 
    * @param imp the static import to add
    */
   void addStatic(String imp);

   /**
    * Clears the manager of all imports and static imports
    */
   void clear();

   /**
    * Returns a string for the imports section in a java class.
    * 
    * @return a string for the imports section in a java class
    */
   String generateJava();

   /**
    * Returns the type string given the fully qualified name. This method should return just the class name if there
    * is already an import for the given fully qualified name in this manager; otherwise it should return the fully
    * qualified name itself.
    * 
    * @return the type string given the fully qualified name
    */
   String getType(String fullyQualifiedName);

}
