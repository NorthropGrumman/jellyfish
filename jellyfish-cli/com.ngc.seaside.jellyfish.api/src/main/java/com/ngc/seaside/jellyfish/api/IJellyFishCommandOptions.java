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
package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

/**
 * This interface provides the same information as the {@link ICommandOptions} plus the added System Descriptor model.
 * The commands will be passed this object upon the execution of their tasks by the {@link IJellyFishCommandProvider}.
 *
 * <p> Each command should determine if the system descriptor must be set, not the provider. In the event that
 * the system descriptor is null and it is required, the command should issue an error to the user.</p>
 */
public interface IJellyFishCommandOptions extends ICommandOptions {

   /**
    * Gets the results of parsing the system descriptor project.  This will contain any errors or warnings that were
    * discovered during parsing.
    *
    * @return the results of parsing the system descriptor project (never {@code null})
    */
   IParsingResult getParsingResult();

   /**
    * Get the system descriptor. Some commands may not require the system descriptor but most will. In the event that
    * the system descriptor is not needed it may be null.
    *
    * <p> Each command should determine if the system descriptor must be set, not the provider. In the event that the
    * system descriptor is null and it is required, the command should issue an error to the user.</p>
    *
    * @return the system descriptor model
    */
   ISystemDescriptor getSystemDescriptor();

}
