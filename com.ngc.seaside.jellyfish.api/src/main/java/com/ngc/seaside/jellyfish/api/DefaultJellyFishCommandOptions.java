package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.bootstrap.api.DefaultBootstrapCommandOptions;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Default implementation of the {@link IJellyFishCommandOptions}
 */
public class DefaultJellyFishCommandOptions extends DefaultBootstrapCommandOptions implements IJellyFishCommandOptions {

   private IParsingResult parsingResult;
   private Path systemDescriptorProjectPath;

   @Override
   public IParsingResult getParsingResult() {
      return parsingResult;
   }

   public DefaultJellyFishCommandOptions setParsingResult(IParsingResult parsingResult) {
      this.parsingResult = parsingResult;
      return this;
   }

   @Override
   public ISystemDescriptor getSystemDescriptor() {
      return parsingResult.getSystemDescriptor();
   }

   @Override
   public Path getSystemDescriptorProjectPath() {
      return systemDescriptorProjectPath;
   }

   @Override
   public String toString() {
      return String.format("%s, parsingResult: %s", super.toString(), parsingResult);
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
      return super.equals(obj) && Objects.equals(parsingResult, that.getParsingResult());
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), parsingResult);
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
      mergedOptions.setParameters(mergedParams);
      mergedOptions.setSystemDescriptorProjectPath(options.getSystemDescriptorProjectPath());
      return mergedOptions;
   }

   private static boolean doesContainParameterNamed(String name, Collection<IParameter> parameters) {
      return parameters.stream().noneMatch(p -> p.getName().equals(name));
   }

   public void setSystemDescriptorProjectPath(Path path) {
      this.systemDescriptorProjectPath = path;
   }
}
