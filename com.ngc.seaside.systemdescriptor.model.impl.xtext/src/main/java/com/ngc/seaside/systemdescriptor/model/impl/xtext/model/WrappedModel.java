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
import com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario.WrappedScenario;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.Collection;

/**
 * Adapts an XText {@link Model} to an {@link IModel}.
 *
 * This class is not threadsafe.
 */
public class WrappedModel extends AbstractWrappedXtext<Model> implements IModel {

  private IMetadata metadata;
  private WrappedNamedChildCollection<InputDeclaration, IModel, IDataReferenceField> inputs;
  private WrappedNamedChildCollection<OutputDeclaration, IModel, IDataReferenceField> outputs;
  private WrappedNamedChildCollection<RequireDeclaration, IModel, IModelReferenceField> requires;
  private WrappedNamedChildCollection<PartDeclaration, IModel, IModelReferenceField> parts;
  private WrappedNamedChildCollection<Scenario, IModel, IScenario> scenarios;

  public WrappedModel(IWrapperResolver resolver, Model wrapped) {
    super(resolver, wrapped);
    this.metadata = WrappedMetadata.fromXtext(wrapped.getMetadata());
    // See the comment in the constructor of WrappedScenario for why we do this style of initialization.
    initInputs();
    initOutputs();
    initRequires();
    initParts();
    initScenarios();
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
    return requires;
  }

  @Override
  public INamedChildCollection<IModel, IModelReferenceField> getParts() {
    return parts;
  }

  @Override
  public INamedChildCollection<IModel, IScenario> getScenarios() {
    return scenarios;
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

  /**
   * Converts an {@code IModel} to an  XText {@code Model} instance.
   */
  public static Model toXtextModel(IWrapperResolver resolver, IModel model) {
    Preconditions.checkNotNull(resolver, "resolver may not be null!");
    Preconditions.checkNotNull(model, "model may not be null!");
    Model m = SystemDescriptorFactory.eINSTANCE.createModel();
    m.setName(model.getName());
    m.setMetadata(WrappedMetadata.toXtext(model.getMetadata()));

    m.setInput(SystemDescriptorFactory.eINSTANCE.createInput());
    model.getInputs()
        .stream()
        .map(i -> WrappedInputDataReferenceField.toXTextInputDeclaration(resolver, i))
        .forEach(m.getInput().getDeclarations()::add);

    m.setOutput(SystemDescriptorFactory.eINSTANCE.createOutput());
    model.getOutputs()
        .stream()
        .map(i -> WrappedOutputDataReferenceField.toXTextOutputDeclaration(resolver, i))
        .forEach(m.getOutput().getDeclarations()::add);

    m.setRequires(SystemDescriptorFactory.eINSTANCE.createRequires());
    model.getRequiredModels()
        .stream()
        .map(i -> WrappedRequireModelReferenceField.toXTextRequireDeclaration(resolver, i))
        .forEach(m.getRequires().getDeclarations()::add);

    m.setParts(SystemDescriptorFactory.eINSTANCE.createParts());
    model.getParts()
        .stream()
        .map(i -> WrappedPartModelReferenceField.toXTextPartDeclaration(resolver, i))
        .forEach(m.getParts().getDeclarations()::add);

    model.getScenarios()
        .stream()
        .map(WrappedScenario::toXtextScenario)
        .forEach(m.getScenarios()::add);
    return m;
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
      outputs = new WrappedNamedChildCollection<>(
          wrapped.getOutput().getDeclarations(),
          d -> new WrappedOutputDataReferenceField(resolver, d),
          d -> WrappedOutputDataReferenceField.toXTextOutputDeclaration(resolver, d),
          FieldDeclaration::getName);
    }
  }

  private void initParts() {
    if (wrapped.getParts() == null) {
      parts = new SelfInitializingWrappedNamedChildCollection<>(
          d -> new WrappedPartModelReferenceField(resolver, d),
          d -> WrappedPartModelReferenceField.toXTextPartDeclaration(resolver, d),
          FieldDeclaration::getName,
          () -> {
            wrapped.setParts(SystemDescriptorFactory.eINSTANCE.createParts());
            return wrapped.getParts().getDeclarations();
          });
    } else {
      parts = new WrappedNamedChildCollection<>(
          wrapped.getParts().getDeclarations(),
          d -> new WrappedPartModelReferenceField(resolver, d),
          d -> WrappedPartModelReferenceField.toXTextPartDeclaration(resolver, d),
          FieldDeclaration::getName);
    }
  }

  private void initRequires() {
    if (wrapped.getRequires() == null) {
      requires = new SelfInitializingWrappedNamedChildCollection<>(
          d -> new WrappedRequireModelReferenceField(resolver, d),
          d -> WrappedRequireModelReferenceField.toXTextRequireDeclaration(resolver, d),
          FieldDeclaration::getName,
          () -> {
            wrapped.setRequires(SystemDescriptorFactory.eINSTANCE.createRequires());
            return wrapped.getRequires().getDeclarations();
          });
    } else {
      requires = new WrappedNamedChildCollection<>(
          wrapped.getRequires().getDeclarations(),
          d -> new WrappedRequireModelReferenceField(resolver, d),
          d -> WrappedRequireModelReferenceField.toXTextRequireDeclaration(resolver, d),
          FieldDeclaration::getName);
    }
  }

  private void initScenarios() {
    scenarios = new WrappedNamedChildCollection<>(
        wrapped.getScenarios(),
        s -> new WrappedScenario(resolver, s),
        WrappedScenario::toXtextScenario,
        Scenario::getName);
  }
}
