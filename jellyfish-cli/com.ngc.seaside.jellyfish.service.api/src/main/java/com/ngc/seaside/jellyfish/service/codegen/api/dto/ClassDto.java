/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
