package com.ngc.seaside.jellyfish.api;

/**
 * This interface provides an extension of the {@link ICommand} interface only in the type of options that are
 * presented to the command at run time. The main difference being that of the System Descriptor. The system descriptor
 * is read in at run time via the {@link IJellyFishCommandProvider} implementation and provided to the command.
 */
public interface IJellyFishCommand extends ICommand<IJellyFishCommandOptions> {

}
