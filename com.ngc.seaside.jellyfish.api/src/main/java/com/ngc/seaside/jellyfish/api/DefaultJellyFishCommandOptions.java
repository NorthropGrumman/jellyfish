package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.bootstrap.api.DefaultBootstrapCommandOptions;
import com.ngc.seaside.command.api.DefaultParameterCollection;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Default implementation of the {@link IJellyFishCommandOptions}
 */
public class DefaultJellyFishCommandOptions extends DefaultBootstrapCommandOptions implements IJellyFishCommandOptions {

   private ISystemDescriptor systemDescriptor;

   @Override
   public ISystemDescriptor getSystemDescriptor() {
      return systemDescriptor;
   }

   public void setSystemDescriptor(ISystemDescriptor systemDescriptor) {
      this.systemDescriptor = systemDescriptor;
   }

   @Override
   public String toString() {
      return String.format("%s, systemDescriptor: %s", super.toString(), systemDescriptor);
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
      return super.equals(obj) && Objects.equals(systemDescriptor, that.getSystemDescriptor());
   }

   @Override
   public int hashCode() {
      return Objects.hash(super.hashCode(), systemDescriptor);
   }

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
      mergedOptions.setSystemDescriptor(options.getSystemDescriptor());
      mergedOptions.setParameters(mergedParams);
      return mergedOptions;
   }

   private static boolean doesContainParameterNamed(String name, Collection<IParameter> parameters) {
      return parameters.stream().anyMatch(p -> p.getName().equals(name));
   }
}
