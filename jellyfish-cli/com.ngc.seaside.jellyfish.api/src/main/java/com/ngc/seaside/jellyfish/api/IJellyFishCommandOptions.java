package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import java.nio.file.Path;

/**
 * This interface provides the same information as the {@link ICommandOptions} plus the added
 * System Descriptor model. The commands will be passed this object upon the execution of their tasks by the
 * {@link IJellyFishCommandProvider}.
 *
 * Each command should determine if the system descriptor must be set, not the provider. In the event that the
 * system descriptor is null and it is required, the command should issue an error to the user.
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
    * Get the system descriptor. Some commands may not require the system descriptor but most will. In the
    * event that the system descriptor is not needed it may be null.
    *
    * Each command should determine if the system descriptor must be set, not the provider. In the event that the
    * system descriptor is null and it is required, the command should issue an error to the user.
    *
    * @return the system descriptor model.
    */
   ISystemDescriptor getSystemDescriptor();

   Path getSystemDescriptorProjectPath();
}
