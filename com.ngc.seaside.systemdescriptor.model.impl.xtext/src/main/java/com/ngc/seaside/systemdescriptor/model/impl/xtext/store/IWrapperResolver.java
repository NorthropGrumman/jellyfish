package com.ngc.seaside.systemdescriptor.model.impl.xtext.store;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import java.util.Optional;

/**
 * An {@code IWrapperResolver} is responsible for locating existing instances of wrappers for XText data.  It may also
 * be used to find wrapped XText data directly.
 */
public interface IWrapperResolver {

   /**
    * Finds the {@code IData} wrapper for the given XTest data.
    *
    * @return the wrapper for the data (never {@code null})
    * @throws IllegalStateException if no wrapper for the given data could be found.
    */
   IData getWrapperFor(Data data);

   /**
    * Finds the {@code IModel} wrapper for the given XTest model.
    *
    * @return the wrapper for the model (never {@code null})
    * @throws IllegalStateException if no wrapper for the given model could be found.
    */
   IModel getWrapperFor(Model model);

   /**
    * Finds the {@code IPackage} wrapper for the given XTest package.
    *
    * @return the wrapper for the package (never {@code null})
    * @throws IllegalStateException if no wrapper for the given package could be found.
    */
   IPackage getWrapperFor(Package systemDescriptorPackage);

   /**
    * Attempts to find the XText {@link Data} with the given name and package.  If no data is found, an empty {@code
    * Optional} is returned.
    */
   Optional<Data> findXTextData(String name, String packageName);

   /**
    * Attempts to find the XText {@link Model} with the given name and package.  If no model is found, an empty {@code
    * Optional} is returned.
    */
   Optional<Model> findXTextModel(String name, String packageName);
}

