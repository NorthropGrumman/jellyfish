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
package com.ngc.seaside.systemdescriptor.model.impl.basic.data;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Enumeration implements IEnumeration {

   protected IPackage parent;
   protected final String name;
   protected final List<String> values;
   protected IMetadata metadata;

   /**
    * Creates a new enumeration with the given name.
    */
   public Enumeration(String name) {
      this.name = name;
      this.values = new ArrayList<>();
   }

   /**
    * Creates a new enumeration with the given name and values.
    */
   public Enumeration(String name, Collection<String> values) {
      this.name = name;
      this.values = new ArrayList<>(values);
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public IPackage getParent() {
      return parent;
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IEnumeration setMetadata(IMetadata metadata) {
      this.metadata = metadata;
      return this;
   }

   @Override
   public Collection<String> getValues() {
      return values;
   }

   @Override
   public String getFullyQualifiedName() {
      return String.format("%s%s%s", parent == null ? "" : parent.getName(), parent == null ? "" : ".", name);
   }

   public Enumeration setParent(IPackage parent) {
      this.parent = parent;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Enumeration)) {
         return false;
      }
      Enumeration that = (Enumeration) o;
      return Objects.equals(values, that.values)
             && Objects.equals(name, that.name)
             && parent == that.parent
             && Objects.equals(metadata, that.metadata);
   }

   @Override
   public int hashCode() {
      return Objects.hash(values,
                          name,
                          System.identityHashCode(parent),
                          metadata);
   }

   @Override
   public String toString() {
      return "Enumeration["
             + "parent=" + (parent == null ? "null" : parent.getName())
             + ", name='" + name + '\''
             + ", values=" + values
             + ", metadata=" + metadata
             + ']';
   }
}
