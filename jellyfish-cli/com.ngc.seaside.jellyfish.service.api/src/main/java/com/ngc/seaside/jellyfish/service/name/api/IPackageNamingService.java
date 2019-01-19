/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
