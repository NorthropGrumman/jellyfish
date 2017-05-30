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
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.SelfInitializingWrappedNamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.collection.WrappedNamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.Collection;

public class WrappedModel extends AbstractWrappedXtext<Model> implements IModel {

  private IMetadata metadata;
  private WrappedNamedChildCollection<InputDeclaration, IModel, IDataReferenceField> inputs;
  private WrappedNamedChildCollection<OutputDeclaration, IModel, IDataReferenceField> outputs;

  public WrappedModel(IWrapperResolver resolver, Model wrapped) {
    super(resolver, wrapped);
    this.metadata = WrappedMetadata.fromXtext(wrapped.getMetadata());
    // See the comment in the constructor of WrappedScenario for why we do this style of initialization.
    initInputs();
    initOutputs();
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
    return inputs;
  }

  @Override
  public INamedChildCollection<IModel, IDataReferenceField> getOutputs() {
    return outputs;
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

  private void initInputs() {
    if (wrapped.getInput() == null) {
      inputs = new SelfInitializingWrappedNamedChildCollection<>(
          d -> new WrappedInputDataReferenceField(resolver, d),
          d -> WrappedInputDataReferenceField.toXTextInputDeclaration(resolver, d),
          FieldDeclaration::getName,
          () -> {
            wrapped.setInput(SystemDescriptorFactory.eINSTANCE.createInput());
            return wrapped.getInput().getDeclarations();
          });
    } else {
      // Otherwise, just wrap the steps that are in the existing declaration.
      inputs = new WrappedNamedChildCollection<>(
          wrapped.getInput().getDeclarations(),
          d -> new WrappedInputDataReferenceField(resolver, d),
          d -> WrappedInputDataReferenceField.toXTextInputDeclaration(resolver, d),
          FieldDeclaration::getName);
    }
  }

  private void initOutputs() {
    if (wrapped.getOutput() == null) {
      outputs = new SelfInitializingWrappedNamedChildCollection<>(
          d -> new WrappedOutputDataReferenceField(resolver, d),
          d -> WrappedOutputDataReferenceField.toXTextOutputDeclaration(resolver, d),
          FieldDeclaration::getName,
          () -> {
            wrapped.setOutput(SystemDescriptorFactory.eINSTANCE.createOutput());
            return wrapped.getOutput().getDeclarations();
          });
    } else {
      // Otherwise, just wrap the steps that are in the existing declaration.
      outputs = new WrappedNamedChildCollection<>(
          wrapped.getOutput().getDeclarations(),
          d -> new WrappedOutputDataReferenceField(resolver, d),
          d -> WrappedOutputDataReferenceField.toXTextOutputDeclaration(resolver, d),
          FieldDeclaration::getName);
    }
  }
}
