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
