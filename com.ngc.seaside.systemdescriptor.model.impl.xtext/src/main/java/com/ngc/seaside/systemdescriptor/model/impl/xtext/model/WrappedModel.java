package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.AutoWrappingCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.SelfInitializingCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.Collection;

public class WrappedModel extends AbstractWrappedXtext<Model> implements IModel {

  private IMetadata metadata;
  private Collection<IDataReferenceField> inputs;

  public WrappedModel(IWrapperResolver resolver, Model wrapped) {
    super(resolver, wrapped);

    if (wrapped.getInput() == null) {
      inputs = new SelfInitializingCollection<>(
          d -> new WrappedDataReferenceField(resolver, d),
          d -> WrappedDataReferenceField.toXTextInputDeclaration(resolver, d),
          () -> {
            wrapped.setInput(SystemDescriptorFactory.eINSTANCE.createInput());
            return wrapped.getInput().getDeclarations();
          });
    } else {
      // Otherwise, just wrap the steps that are in the existing declaration.
      inputs = new AutoWrappingCollection<>(
          wrapped.getInput().getDeclarations(),
          d -> new WrappedDataReferenceField(resolver, d),
          d -> WrappedDataReferenceField.toXTextInputDeclaration(resolver, d));
    }
  }

  @Override
  public IMetadata getMetadata() {
    return metadata;
  }

  @Override
  public IModel setMetadata(IMetadata metadata) {
    Preconditions.checkNotNull(metadata, "metadata may not be null!");
    this.metadata = metadata;
    wrapped.setMetadata(WrappedMetadata.toXtext(metadata));
    return this;
  }

  @Override
  public INamedChildCollection<IModel, IDataReferenceField> getInputs() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public INamedChildCollection<IModel, IDataReferenceField> getOutputs() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public INamedChildCollection<IModel, IModelReferenceField> getRequiredModels() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public INamedChildCollection<IModel, IModelReferenceField> getParts() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public INamedChildCollection<IModel, IScenario> getScenarios() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Collection<IModelLink<?>> getLinks() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public String getFullyQualifiedName() {
    Package p = (Package) wrapped.eContainer();
    return String.format("%s%s%s",
                         p == null ? "" : p.getName(),
                         p == null ? "" : ".",
                         wrapped.getName());
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public IPackage getParent() {
    return resolver.getWrapperFor((Package) wrapped.eContainer());
  }
}
