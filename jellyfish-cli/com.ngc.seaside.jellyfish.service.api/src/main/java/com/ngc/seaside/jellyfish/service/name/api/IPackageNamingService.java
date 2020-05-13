/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.service.name.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
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
   String getDomainPackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data);

   /**
    * Gets the package name of the event class to use for the given data type.
    *
    * @param options the options the current command is being executed with
    * @param data    the data type to get the package name for
    * @return the package name of the event class of the data type
    */
   String getEventPackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data);

   /**
    * Gets the package name of the message class to use for the given data type.
    *
    * @param options the options the current command is being executed with
    * @param data    the data type to get the package name for
    * @return the package name of the message class of the data type
    */
   String getMessagePackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data);

   /**
    * Gets the package name of the connector project classes.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name of the connector project classes
    */
   String getConnectorPackageName(IJellyFishCommandOptions options, IModel model);
   
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
   
   /**
    * Gets the package name for the transport topics of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name for the transport topics of a Java service
    */
   String getTransportTopicsPackageName(IJellyFishCommandOptions options, IModel model);
   
   /**
    * Gets the package name for the distribution project of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name for the distribution project of a Java service
    */
   String getDistributionPackageName(IJellyFishCommandOptions options, IModel model);
   
   /**
    * Gets the package name for the cucumber tests project of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name for the cucumber tests project of a Java service
    */
   String getCucumberTestsPackageName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the package name for the cucumber tests config project of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name for the cucumber tests config project of a Java service
    */
   String getCucumberTestsConfigPackageName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the package name for the config project of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name for the cucumber tests project of a Java service
    */
   String getConfigPackageName(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets the package name for the config project of a Java service.
    *
    * @param options the options the current command is being executed with
    * @param model   the model of a service to get the package name for
    * @return the package name for the Publish Subscribe Bridge project of a Java service
    */
   String getPubSubBridgePackageName(IJellyFishCommandOptions options, IModel model);
}
