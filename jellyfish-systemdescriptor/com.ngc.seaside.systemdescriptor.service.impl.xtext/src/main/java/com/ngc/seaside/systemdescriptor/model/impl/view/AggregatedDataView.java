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
