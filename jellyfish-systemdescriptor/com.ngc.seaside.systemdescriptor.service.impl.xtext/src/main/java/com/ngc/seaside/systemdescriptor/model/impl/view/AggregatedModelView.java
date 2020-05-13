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

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

/**
 * Provides an aggregated view of a model by taking into account the model's refinement hierarchy.
 */
public class AggregatedModelView implements IModel {

   private final IModel wrapped;
   private final INamedChildCollection<IModel, IDataReferenceField> aggregatedInputs;
   private final INamedChildCollection<IModel, IDataReferenceField> aggregatedOutputs;
   private final INamedChildCollection<IModel, IModelReferenceField> aggregatedParts;
   private final INamedChildCollection<IModel, IModelReferenceField> aggregatedRequirements;
   private final INamedChildCollection<IModel, IScenario> aggregatedScenarios;
   private final Collection<IModelLink<?>> aggregatedLinks;
   private IMetadata aggregatedMetadata;
   private IProperties aggregatedProperties;

   /**
    * Creates a new view that wraps the given model.
    */
   public AggregatedModelView(IModel wrapped) {
      this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
      this.aggregatedInputs = getAggregatedFields(IModel::getInputs, Function.identity());
      this.aggregatedOutputs = getAggregatedFields(IModel::getOutputs, Function.identity());
      this.aggregatedParts = getAggregatedFields(IModel::getParts, AggregatedModelFieldView::new);
      this.aggregatedRequirements = getAggregatedFields(IModel::getRequiredModels, AggregatedModelFieldView::new);
      this.aggregatedScenarios = getAggregatedFields(IModel::getScenarios, Function.identity());
      this.aggregatedLinks = getAggregatedLinks();
      this.aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      this.aggregatedProperties = AggregatedPropertiesView.getAggregatedProperties(wrapped);
   }

   @Override
   public IMetadata getMetadata() {
      return aggregatedMetadata;
   }

   @Override
   public IModel setMetadata(IMetadata metadata) {
      wrapped.setMetadata(metadata);
      aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      return this;
   }

   @Override
   public IProperties getProperties() {
      return aggregatedProperties;
   }

   @Override
   public IModel setProperties(IProperties properties) {
      wrapped.setProperties(properties);
      aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      return this;
   }

   @Override
   public INamedChildCollection<IModel, IDataReferenceField> getInputs() {
      return aggregatedInputs;
   }

   @Override
   public INamedChildCollection<IModel, IDataReferenceField> getOutputs() {
      return aggregatedOutputs;
   }

   @Override
   public INamedChildCollection<IModel, IModelReferenceField> getRequiredModels() {
      return aggregatedRequirements;
   }

   @Override
   public INamedChildCollection<IModel, IModelReferenceField> getParts() {
      return aggregatedParts;
   }

   @Override
   public INamedChildCollection<IModel, IScenario> getScenarios() {
      return aggregatedScenarios;
   }

   @Override
   public Collection<IModelLink<?>> getLinks() {
      return aggregatedLinks;
   }

   @Override
   public Optional<IModelLink<?>> getLinkByName(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkState(!name.trim().isEmpty(), "name may not be empty!");
      return aggregatedLinks
            .stream()
            .filter(link -> name.equals(link.getName().orElse(null)))
            .findFirst();
   }

   @Override
   public Optional<IModel> getRefinedModel() {
      return wrapped.getRefinedModel();
   }

   @Override
   public IModel setRefinedModel(IModel refinedModel) {
      wrapped.setRefinedModel(refinedModel);
      return this;
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
      if (o instanceof AggregatedModelView) {
         return wrapped.equals(((AggregatedModelView) o).wrapped);
      }
      return o instanceof IModel && wrapped.equals(o);
   }

   @Override
   public int hashCode() {
      return wrapped.hashCode();
   }

   private <T extends INamedChild<IModel>> INamedChildCollection<IModel, T> getAggregatedFields(
         Function<IModel, INamedChildCollection<IModel, T>> fieldFinder,
         Function<T, T> fieldAggregatorTransformer) {
      NamedChildCollection<IModel, T> collection = new NamedChildCollection<>();
      IModel model = wrapped;
      while (model != null) {
         fieldFinder.apply(model)
               .stream()
               // Give the function a chance to apply an aggregated view on top of the field.
               .map(fieldAggregatorTransformer)
               // Prevent fields that has already been added to the collection from being added again.  This is needed
               // to avoid adding duplicates for refined fields.
               .filter(f -> !collection.getByName(f.getName()).isPresent())
               // Add each field to the collection.
               .forEach(collection::add);
         model = model.getRefinedModel().orElse(null);
      }
      return collection;
   }

   private Collection<IModelLink<?>> getAggregatedLinks() {
      Collection<IModelLink<?>> collection = new ArrayList<>();
      IModel model = wrapped;
      while (model != null) {
         for (IModelLink<?> link : model.getLinks()) {
            collection.add(new AggregatedLinkView<>(link));
         }
         model = model.getRefinedModel().orElse(null);
      }
      return collection;
   }
}
