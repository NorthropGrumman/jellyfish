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
