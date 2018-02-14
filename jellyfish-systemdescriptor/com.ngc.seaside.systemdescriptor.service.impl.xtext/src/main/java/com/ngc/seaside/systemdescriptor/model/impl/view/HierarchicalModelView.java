package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;
import java.util.Optional;

public class HierarchicalModelView implements IModel {

   private final IModel wrapped;

   public HierarchicalModelView(IModel wrapped) {
      this.wrapped = Preconditions.checkNotNull(wrapped, "wrapped may not be null!");
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
      // TODO TH:
      return wrapped.getInputs();
   }

   @Override
   public INamedChildCollection<IModel, IDataReferenceField> getOutputs() {
      // TODO TH:
      return wrapped.getOutputs();
   }

   @Override
   public INamedChildCollection<IModel, IModelReferenceField> getRequiredModels() {
      // TODO TH:
      return wrapped.getRequiredModels();
   }

   @Override
   public INamedChildCollection<IModel, IModelReferenceField> getParts() {
      // TODO TH:
      return wrapped.getParts();
   }

   @Override
   public INamedChildCollection<IModel, IScenario> getScenarios() {
      // TODO TH:
      return wrapped.getScenarios();
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
}
