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
package com.ngc.seaside.jellyfish.service.codegen.api.dto;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a description of a Java class. May also be used to represent an interface.
 */
public class ClassDto implements TypeDto<ClassDto> {

   private Set<String> imports = new TreeSet<>();
   private String name;
   private String packageName;

   /**
    * Gets the list of imports needed by this class.
    */
   public Set<String> getImports() {
      return imports;
   }

   public ClassDto setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   /**
    * Gets the unqualified name of this class.
    */
   public String getName() {
      return name;
   }

   public ClassDto setName(String name) {
      this.name = name;
      return this;
   }

   /**
    * Gets the unqualified name of this class.
    */
   public String getTypeName() {
      return name;
   }

   public ClassDto setTypeName(String name) {
      this.name = name;
      return this;
   }

   /**
    * Gets the name of the package of this class.
    */
   public String getPackageName() {
      return packageName;
   }

   public ClassDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }

}
