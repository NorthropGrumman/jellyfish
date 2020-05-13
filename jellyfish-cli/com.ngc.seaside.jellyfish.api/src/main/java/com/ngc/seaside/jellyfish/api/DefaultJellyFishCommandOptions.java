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
package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Default implementation of the {@link IJellyFishCommandOptions}
 */
public class DefaultJellyFishCommandOptions implements IJellyFishCommandOptions {

   private IParsingResult parsingResult;
   private IGherkinParsingResult gherkinParsingResult;
   private IParameterCollection parameters;

   @Override
   public IParameterCollection getParameters() {
      return parameters;
   }

   public void setParameters(IParameterCollection parameters) {
      this.parameters = parameters;
   }

   @Override
   public IParsingResult getParsingResult() {
      return parsingResult;
   }

   public DefaultJellyFishCommandOptions setParsingResult(IParsingResult parsingResult) {
      this.parsingResult = parsingResult;
      return this;
   }

   @Override
   public IGherkinParsingResult getGherkinParsingResult() {
      return gherkinParsingResult;
   }

   public DefaultJellyFishCommandOptions setGherkinParsingResult(IGherkinParsingResult gherkinParsingResult) {
      this.gherkinParsingResult = gherkinParsingResult;
      return this;
   }

   @Override
   public ISystemDescriptor getSystemDescriptor() {
      return parsingResult == null ? null : parsingResult.getSystemDescriptor();
   }

   @Override
   public String toString() {
      return String.format("parameters: %s, parsingResult: %s", parameters, parsingResult);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof IParameterCollection)) {
         return false;
      }
      IJellyFishCommandOptions that = (IJellyFishCommandOptions) obj;
      return Objects.equals(parameters, that.getParameters())
             && Objects.equals(parsingResult, that.getParsingResult())
             && Objects.equals(gherkinParsingResult, that.getGherkinParsingResult());
   }

   @Override
   public int hashCode() {
      return Objects.hash(parameters, parsingResult, gherkinParsingResult);
   }

   /**
    * Returns an {@code IJellyFishCommandOptions} with all the same parameters and system descriptor as the provided
    * options but includes the additional parameters as well.  If a parameter in the original options has the same name
    * as an additional parameter supplied as input to this method, the parameter in teh original options is replaced
    * with the new parameter.
    *
    * @param options    the options that contains a system descriptor and parameters
    * @param parameter  an additional parameter to include in the result
    * @param parameters optional parameters to include in the result
    * @return a {@code IJellyFishCommandOptions} with additional parameters.
    */
   @SuppressWarnings("rawtypes")
   public static IJellyFishCommandOptions mergeWith(IJellyFishCommandOptions options,
                                                    IParameter parameter,
                                                    IParameter... parameters) {
      if (parameter == null) {
         throw new NullPointerException("parameter may not be null!");
      }
      Collection<IParameter> params = new ArrayList<>();
      params.add(parameter);
      if (parameters != null) {
         params.addAll(Arrays.asList(parameters));
      }
      return mergeWith(options, params);
   }

   /**
    * Returns an {@code IJellyFishCommandOptions} with all the same parameters and system descriptor as the provided
    * options but includes the additional parameters as well.  If a parameter in the original options has the same name
    * as an additional parameter supplied as input to this method, the parameter in teh original options is replaced
    * with the new parameter.
    *
    * @param options    the options that contains a system descriptor and parameters
    * @param parameters additional parameters to include in the result
    * @return a {@code IJellyFishCommandOptions} with additional parameters.
    */
   @SuppressWarnings("rawtypes")
   public static IJellyFishCommandOptions mergeWith(IJellyFishCommandOptions options,
                                                    Collection<IParameter> parameters) {
      if (options == null) {
         throw new NullPointerException("options may not be null!");
      }
      if (parameters == null) {
         throw new NullPointerException("parameters may not be null!");
      }

      DefaultParameterCollection mergedParams = new DefaultParameterCollection();
      parameters.forEach(mergedParams::addParameter);
      // Only add parameters from the existing options if they were not specified.  This enables parameters to be
      // overridden.
      options.getParameters().getAllParameters()
            .stream()
            .filter(p -> doesContainParameterNamed(p.getName(), parameters))
            .forEach(mergedParams::addParameter);

      DefaultJellyFishCommandOptions mergedOptions = new DefaultJellyFishCommandOptions();
      mergedOptions.setParsingResult(options.getParsingResult());
      mergedOptions.setGherkinParsingResult(options.getGherkinParsingResult());
      mergedOptions.setParameters(mergedParams);
      return mergedOptions;
   }

   @SuppressWarnings("rawtypes")
   private static boolean doesContainParameterNamed(String name, Collection<IParameter> parameters) {
      return parameters.stream().noneMatch(p -> p.getName().equals(name));
   }

}
