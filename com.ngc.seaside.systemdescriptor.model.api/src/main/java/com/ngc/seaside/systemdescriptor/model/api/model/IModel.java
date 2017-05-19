package com.ngc.seaside.systemdescriptor.model.api.model;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.List;

public interface IModel {

  String getName();

  IMetadata getMetadata();

  List<IDataReferenceField> getInputs();

  List<IDataReferenceField> getOutputs();

  List<IModelReferenceField> getRequiredModels();

  List<IModelReferenceField> getParts();

  List<IModelLink<?>> getLinks();

  List<IScenario> getScenarios();

  IPackage getParentPackage();
}
