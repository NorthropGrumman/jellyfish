package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.bootstrap.api.IBootstrapCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;

/**
 * This interface provides the same information as the {@link IBootstrapCommandOptions} plus the added
 * System Descriptor model. The commands will be passed this object upon the execution of their tasks by the
 * {@link IJellyFishCommandProvider}.
 *
 * Each command should determine if the system descriptor must be set, not the provider. In the event that the
 * system descriptor is null and it is required, the command should issue an error to the user.
 *
 */
public interface IJellyFishCommandOptions extends IBootstrapCommandOptions {

   /**
    * Get the system descriptor. Some commands may not require the system descriptor but most will. In the
    * event that the system descriptor is not needed it may be null.
    *
    * Each command should determine if the system descriptor must be set, not the provider. In the event that the
    * system descriptor is null and it is required, the command should issue an error to the user.
    *
    * @return the system descriptor model.
    */
   ISystemDescriptor getSystemDescriptor();
}
