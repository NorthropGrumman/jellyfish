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
package com.ngc.seaside.systemdescriptor.service.source.api;

import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinStep;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall.NoParameterMethodCall;

/**
 * Service for locating where a System Descriptor element is located in the source code.  Also supports locating
 * elements within a Gherkin feature file.
 */
public interface ISourceLocatorService {

   /**
    * Returns the source location of the given system descriptor element, such as an {@link IModel},
    * {@link IModelLink}, {@link IDataField}, {@link IFeature}, {@link IGherkinScenario},
    * or {@link IGherkinStep}. If {@code entireElement} is {@code false}, the service will attempt to
    * narrow the location of the element, such as the location of just the name of the model for an {@link IModel}.
    *
    * @param element       system descriptor element
    * @param entireElement whether or not to return the location of the entire element
    * @return the source location of the given element
    * @throws UnknownSourceLocationException if unable to find the source location
    */
   ISourceLocation getLocation(Object element, boolean entireElement);

   /**
    * Starts a chained method call for getting the source location of a specific part of the given element.
    * 
    * <p/>
    * The chained method call can be used in two ways. The first way uses the method
    * {@link IChainedMethodCall#calling} to get the source location. For example:
    * 
    * <pre>
    * ISourceLocation location =
    *     sourceLocatorService.with(model)
    *                         .calling(m -> m.getMetadata().getJson().getValue("requirements"))
    *                         .getLocation()
    * </pre>
    * 
    * This example returns the source location of the value pertaining to the {@code "requirements"} key of the model's
    * metadata.
    * 
    * <p/>
    * The second way involves a chain of {@link IChainedMethodCall#then(NoParameterMethodCall) then} calls using method
    * references to get the location. For example:
    * 
    * <pre>
    * ISourceLocation location = 
    *     sourceLocatorService.with(step)
    *                         .then(IGherkinStep::getContent)
    *                         .then(String::substring, 10, 20)
    *                         .getLocation()
    * </pre>
    * 
    * This example returns the source location of a substring of the gherkin step.
    * 
    * @param element System Descriptor or Gherkin element
    * @return {@link IChainedMethodCall} starting with the given element
    * @throws UnknownSourceLocationException if the given element cannot be used as the starting point for a source code
    *            location chain
    */
   <T> IChainedMethodCall<T> with(T element);

}
