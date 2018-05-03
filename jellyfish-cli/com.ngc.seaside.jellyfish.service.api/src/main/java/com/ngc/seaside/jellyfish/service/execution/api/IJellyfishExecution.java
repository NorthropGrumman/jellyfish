package com.ngc.seaside.jellyfish.service.execution.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;

import java.nio.file.Path;

/**
 * The results of a service execution of Jellyfish.
 */
public interface IJellyfishExecution {

   /**
    * Gets the command options used to execute Jellyfish.
    *
    * @return the command options used to execute Jellyfish
    */
   IJellyFishCommandOptions getOptions();

   /**
    * Gets the results of parsing the system descriptor project.  This will contain any errors or warnings that were
    * discovered during parsing.
    *
    * @return the results of parsing the system descriptor project (never {@code null})
    */
   default IParsingResult getParsingResult() {
      return getOptions().getParsingResult();
   }

   /**
    * Get the system descriptor. Some commands may not require the system descriptor but most will. In the event that
    * the system descriptor is not needed it may be null.
    *
    * <p> Each command should determine if the system descriptor must be set, not the provider. In the event that the
    * system descriptor is null and it is required, the command should issue an error to the user.</p>
    *
    * @return the system descriptor model
    */
   default ISystemDescriptor getSystemDescriptor() {
      return getOptions().getSystemDescriptor();
   }

   /**
    * Gets the path to the system descriptor project.
    *
    * @return the path to the system descriptor project
    */
   default Path getSystemDescriptorProjectPath() {
      return getOptions().getSystemDescriptorProjectPath();
   }
}
