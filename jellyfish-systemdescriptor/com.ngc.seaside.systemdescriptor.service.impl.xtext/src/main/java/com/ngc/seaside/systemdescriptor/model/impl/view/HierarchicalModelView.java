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
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class HierarchicalModelView implements IModel {

   private final IModel wrapped;
   private final INamedChildCollection<IModel, IDataReferenceField> aggregatedInputs;
   private final INamedChildCollection<IModel, IDataReferenceField> aggregatedOutputs;
   private final INamedChildCollection<IModel, IModelReferenceField> aggregatedParts;
   private final INamedChildCollection<IModel, IModelReferenceField> aggregatedRequirements;
   private final INamedChildCollection<IModel, IScenario> aggregatedScenarios;

   public HierarchicalModelView(IModel wrapped) {
      this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
      this.aggregatedInputs = getAggregatedFields(IModel::getInputs);
      this.aggregatedOutputs = getAggregatedFields(IModel::getOutputs);
      this.aggregatedParts = getAggregatedFields(IModel::getParts);
      this.aggregatedRequirements = getAggregatedFields(IModel::getRequiredModels);
      this.aggregatedScenarios = getAggregatedFields(IModel::getScenarios);
   }

   @Override
   public IMetadata getMetadata() {
      return wrapped.getMetadata();
   }

   @Override
   public IModel setMetadata(IMetadata metadata) {
      return wrapped.setMetadata(metadata);
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
      // TODO TH:
      return wrapped.getLinks();
   }

   @Override
   public Optional<IModelLink<?>> getLink(String name) {
      // TODO TH:
      return wrapped.getLink(name);
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
      if (o instanceof HierarchicalModelView) {
         return wrapped.equals(((HierarchicalModelView) o).wrapped);
      }
      return o instanceof IModel && wrapped.equals(o);
   }

   @Override
   public int hashCode() {
      return wrapped.hashCode();
   }

   private <T extends INamedChild<IModel>> INamedChildCollection<IModel, T> getAggregatedFields(
         Function<IModel, INamedChildCollection<IModel, T>> fieldFinder) {
      NamedChildCollection<IModel, T> collection = new NamedChildCollection<>();
      IModel model = wrapped;
      while (model != null) {
         collection.addAll(fieldFinder.apply(model));
         // TODO TH: implement this and remove the null assignment.
         //model = model.getRefinedModel().orElse(null);
         model = null;
      }
      return collection;
   }
}
