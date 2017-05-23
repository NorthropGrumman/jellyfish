package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Collection;

public interface IModel extends INamedChild<IPackage> {

  IMetadata getMetadata();

  IModel setMetadata(IMetadata metadata);

  INamedChildCollection<IModel, IDataReferenceField> getInputs();

  INamedChildCollection<IModel, IDataReferenceField> getOutputs();

  INamedChildCollection<IModel, IModelReferenceField> getRequiredModels();

  INamedChildCollection<IModel, IModelReferenceField> getParts();

  INamedChildCollection<IModel, IScenario> getScenarios();

  Collection<IModelLink<?>> getLinks();

  String getFullyQualifiedName();
}
