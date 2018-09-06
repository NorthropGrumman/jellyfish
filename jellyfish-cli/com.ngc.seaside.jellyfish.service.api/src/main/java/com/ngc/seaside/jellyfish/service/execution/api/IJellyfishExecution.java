/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.execution.api;

import com.google.inject.Injector;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

/**
 * The results of a service execution of Jellyfish.
 */
public interface IJellyfishExecution {

   /**
    * Gets the injector that was used to run Jellyfish.  This injector will contain all the services and components
    * that made up Jellyfish.  Clients can use the injector to get a reference to any of these components.
    *
    * @return the injector used to run Jellyfish
    */
   Injector getInjector();

   /**
    * Gets the command options used to execute Jellyfish.
    *
    * @return the command options used to execute Jellyfish
    */
   IJellyFishCommandOptions getOptions();

   /**
    * Gets the number of milliseconds it took to execute Jellyfish.
    *
    * @return the number of milliseconds it took to execute Jellyfish
    */
   long getExecutionDuration();

   /**
    * Gets the results of parsing the system descriptor project.  This will contain any errors or warnings that were
    * discovered during parsing.
    *
    * @return the results of parsing the system descriptor project (never {@code null})
    */
   default IParsingResult getParsingResult() {
      return getOptions().getParsingResult();
   }

   /**
    * Get the system descriptor. Some commands may not require the system descriptor but most will. In the event that
    * the system descriptor is not needed it may be null.
    *
    * <p> Each command should determine if the system descriptor must be set, not the provider. In the event that the
    * system descriptor is null and it is required, the command should issue an error to the user.</p>
    *
    * @return the system descriptor model
    */
   default ISystemDescriptor getSystemDescriptor() {
      return getOptions().getSystemDescriptor();
   }

}
