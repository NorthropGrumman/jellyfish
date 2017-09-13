package com.ngc.seaside.jellyfish.service.name.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

/**
 * Generates package names for various components of a model.
 */
public interface IPackageNamingService {

   /**
    * Gets the package name of the domain class to use for the given data type.
    *
    * @param options the options the current command is being executed with
    * @param data    the data type to get the package name for
    * @return the package name of the domain class of the data type
    */
   String getDomainPackageName(IJellyFishCommandOptions options, IData data);

   /**
    * Gets the package name of the event class to use for the given data type.
    *
    * @param options the options the current command is being executed with
    * @param data    the data type to get the package name for
    * @return the package name of the event class of the data type
    */
   String getEventPackageName(IJellyFishCommandOptions options, IData data);

   /**
    * Gets the package name of the message class to use for the given data type.
    *
    * @param options the options the current command is being executed with
    * @param data    the data type to get the package name for
    * @return the package name of the message class of the data type
    */
   String getMessagePackageName(IJellyFishCommandOptions options, IData data);

   /**
    * Gets the package name of the interface class of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name of the interface class of the data type
    */
   String getServiceInterfacePackageName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the package name of the implementation class of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name of the implementation class of the data type
    */
   String getServiceImplementationPackageName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the package name of the implementation base class of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name of the implementation base class of the data type
    */
   String getServiceBaseImplementationPackageName(IJellyFishCommandOptions options, IModel model);
}
