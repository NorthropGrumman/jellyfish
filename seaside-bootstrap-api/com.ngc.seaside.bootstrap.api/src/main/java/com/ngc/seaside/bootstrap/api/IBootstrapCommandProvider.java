package com.ngc.seaside.bootstrap.api;

import com.ngc.seaside.command.api.ICommandProvider;

/**
 * The Bootstrap command provider interface will provide bootstrap commands and allow access to those
 * commands via calls to this interface. The run method will parse the input parameters but will not validate the
 * contents. The individual bootstrap command must validate their own input.
 *
 * @see ICommandProvider
 */
public interface IBootstrapCommandProvider
         extends ICommandProvider<IBootstrapCommandOptions, IBootstrapCommand> {
}
