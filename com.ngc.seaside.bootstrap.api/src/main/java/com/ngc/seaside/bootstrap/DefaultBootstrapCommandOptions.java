package com.ngc.seaside.bootstrap;

import com.ngc.seaside.command.api.IParameterCollection;

import java.util.Objects;

/**
 * Default implementation of the {@link IBootstrapCommandOptions}
 */
public class DefaultBootstrapCommandOptions implements IBootstrapCommandOptions {

   private IParameterCollection parameters;

   @Override
   public IParameterCollection getParameters() {
      return parameters;
   }

   public void setParameters(IParameterCollection parameters) {
      this.parameters = parameters;
   }

   @Override
   public String toString() {
      return String.format("parameters: %s", parameters);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof IBootstrapCommandOptions)) {
         return false;
      }
      IBootstrapCommandOptions that = (IBootstrapCommandOptions) obj;
      return Objects.equals(parameters, that.getParameters());
   }

   @Override
   public int hashCode() {
      return Objects.hash(parameters);
   }
}
