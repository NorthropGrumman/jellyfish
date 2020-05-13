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
 * Construct for managing variable names in a template.
 */
public interface IVariableManager {

   /**
    * Adds the given variable name to this manager. The manager will ensure that no other variables will have the same
    * name as this.
    * 
    * @param variableName variable name
    * @throws IllegalStateException if a variable has already been assigned this name
    */
   void add(String variableName);

   /**
    * Adds a variable to be managed, referenced by the given key with the given requested name.
    * 
    * @param key key to reference the variable name
    * @param requestedVariableName requested variable name
    * @throws IllegalStateException if the key has already been added
    */
   void add(Object key, String requestedVariableName);

   /**
    * Returns the variable name associated with the given key.
    * 
    * @param key key
    * @return the variable name associated with the given key
    */
   String get(Object key);

   /**
    * Enters a new scope. New variables created within the new scope are deleted upon calling {@link #exitScope()}.
    * It is up to implementations to determine if and how conflicts between variable names within different scopes
    * should be resolved.
    */
   void enterScope();

   /**
    * Exits the current. New variables created within the scope are deleted should be deleted.
    */
   void exitScope();

   /**
    * Clears all variables from this manager.
    */
   void clear();

}
