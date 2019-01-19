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
package com.ngc.seaside.jellyfish.service.codegen.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.EnumDto;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

/**
 * Assists with the generation of Java service code.
 */
public interface IJavaServiceGenerationService {

   /**
    * Gets a description of a Java service interface for the given model.
    *
    * @param options options the options the current command is being executed with
    * @param model the model to get the interface description for
    * @return a description of a Java service interface
    */
   ClassDto getServiceInterfaceDescription(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets a description of the Java service base class for the given model.
    *
    * @param options options the options the current command is being executed with
    * @param model the model to get the interface description for
    * @return a description of the Java service base class
    */
   ClassDto getBaseServiceDescription(IJellyFishCommandOptions options, IModel model);

   /**
    * Gets a description of the Java transport topics class for the given model.
    * 
    * @param options options the options the current command is being executed with
    * @param model the model to get the interface description for
    * @return a description of the Java transport topics class
    */
   EnumDto getTransportTopicsDescription(IJellyFishCommandOptions options, IModel model);
}
