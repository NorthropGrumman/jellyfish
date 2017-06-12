package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.bootstrap.DefaultBootstrapCommandOptions;
import com.ngc.seaside.command.api.IParameterCollection;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

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
      return  String.format("%s, systemDescriptor: %s", super.toString(), systemDescriptor);
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
}
