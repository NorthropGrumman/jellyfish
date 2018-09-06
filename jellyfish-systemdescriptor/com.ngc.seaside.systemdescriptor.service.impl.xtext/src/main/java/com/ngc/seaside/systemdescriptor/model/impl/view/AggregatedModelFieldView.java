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
package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;

import java.util.Optional;

public class AggregatedModelFieldView implements IModelReferenceField {

   private final IModelReferenceField wrapped;
   private IMetadata aggregatedMetadata;
   private IProperties aggregatedProperties;

   /**
    * Creates a new aggregated view that wraps the given field.
    */
   public AggregatedModelFieldView(IModelReferenceField wrapped) {
      this.wrapped = wrapped;
      aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      aggregatedProperties = AggregatedPropertiesView.getAggregatedProperties(wrapped);
   }

   @Override
   public IModel getType() {
      return wrapped.getType();
   }

   @Override
   public IModelReferenceField setType(IModel model) {
      wrapped.setType(model);
      return this;
   }

   @Override
   public Optional<IModelReferenceField> getRefinedField() {
      return wrapped.getRefinedField();
   }

   @Override
   public IProperties getProperties() {
      return aggregatedProperties;
   }

   @Override
   public IReferenceField setProperties(
         IProperties properties) {
      wrapped.setProperties(properties);
      aggregatedProperties = AggregatedPropertiesView.getAggregatedProperties(wrapped);
      return this;
   }

   @Override
   public IMetadata getMetadata() {
      return aggregatedMetadata;
   }

   @Override
   public IReferenceField setMetadata(
         IMetadata metadata) {
      wrapped.setMetadata(metadata);
      aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      return this;
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IModel getParent() {
      return wrapped.getParent();
   }
}
