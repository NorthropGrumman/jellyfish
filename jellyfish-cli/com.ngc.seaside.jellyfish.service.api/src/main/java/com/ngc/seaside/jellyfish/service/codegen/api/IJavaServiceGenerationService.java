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
