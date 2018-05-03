package com.ngc.seaside.jellyfish.api;

/**
 * This interface provides an extension of the {@link ICommand} interface only in the type of options that are
 * presented to the command at run time. The main difference being that of the System Descriptor. The system descriptor
 * is read in at run time via the {@link IJellyFishCommandProvider} implementation and provided to the command.
 */
public interface IJellyFishCommand extends ICommand<IJellyFishCommandOptions> {

   /**
    * If true, this command requires a valid System Descriptor project.  If false, this command may be invoked without
    * valid project.  The default implementation returns {@code true}.
    *
    * @return whether or not this project requires a valid System Descriptor project to execute
    */
   default boolean requiresValidSystemDescriptorProject() {
      return true;
   }
}
