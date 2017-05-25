package com.ngc.seaside.systemdescriptor.model.impl.xtext.store;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import java.util.Optional;

public interface IWrapperResolver {

  IData getWrapperFor(Data data);

  IModel getWrapperFor(Model model);

  IPackage getWrapperFor(Package systemDescriptorPackage);

  Optional<Data> findXTextData(String name, String packageName);

  Optional<Model> findXTextModel(String name, String packageName);
}

