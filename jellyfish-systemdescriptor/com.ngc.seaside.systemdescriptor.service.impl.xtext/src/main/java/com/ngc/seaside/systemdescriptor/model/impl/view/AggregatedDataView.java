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
package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import java.util.Optional;

/**
 * Provides an aggregated view of a data object by taking into account the data's extension hierarchy.
 */
public class AggregatedDataView implements IData {

   private final IData wrapped;
   private final INamedChildCollection<IData, IDataField> aggregatedFields;
   private IMetadata aggregatedMetadata;

   /**
    * Creates a new aggregated view that wraps the given data object.
    */
   public AggregatedDataView(IData wrapped) {
      this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
      this.aggregatedFields = getAggregatedFields();
      this.aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
   }

   @Override
   public IMetadata getMetadata() {
      return aggregatedMetadata;
   }

   @Override
   public IData setMetadata(IMetadata metadata) {
      wrapped.setMetadata(metadata);
      aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      return this;
   }

   @Override
   public Optional<IData> getExtendedDataType() {
      return wrapped.getExtendedDataType();
   }

   @Override
   public IData setExtendedDataType(IData dataType) {
      wrapped.setExtendedDataType(dataType);
      return this;
   }

   @Override
   public INamedChildCollection<IData, IDataField> getFields() {
      return aggregatedFields;
   }

   @Override
   public String getFullyQualifiedName() {
      return wrapped.getFullyQualifiedName();
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IPackage getParent() {
      return wrapped.getParent();
   }

   @Override
   public String toString() {
      return wrapped.toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o instanceof AggregatedDataView) {
         return wrapped.equals(((AggregatedDataView) o).wrapped);
      }
      return o instanceof IData && wrapped.equals(o);
   }

   @Override
   public int hashCode() {
      return wrapped.hashCode();
   }

   private INamedChildCollection<IData, IDataField> getAggregatedFields() {
      NamedChildCollection<IData, IDataField> collection = new NamedChildCollection<>();
      IData data = wrapped;
      while (data != null) {
         collection.addAll(data.getFields());
         data = data.getExtendedDataType().orElse(null);
      }
      return collection;
   }
}
