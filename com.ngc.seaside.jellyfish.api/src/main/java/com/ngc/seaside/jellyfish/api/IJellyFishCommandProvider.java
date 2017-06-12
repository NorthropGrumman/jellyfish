package com.ngc.seaside.jellyfish.api;

import com.ngc.seaside.command.api.ICommandProvider;

/**
 * The JellyFish command provider interface will provide JellyFish commands and allow access to those
 * commands via calls to this interface. The run method will parse the input parameters but will not validate the
 * contents. The individual command must validate their own input.
 */
public interface IJellyFishCommandProvider extends ICommandProvider<IJellyFishCommand> {
}
