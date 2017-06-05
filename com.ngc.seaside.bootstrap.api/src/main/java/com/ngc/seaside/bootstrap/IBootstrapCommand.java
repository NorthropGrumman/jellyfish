package com.ngc.seaside.bootstrap;

import com.ngc.seaside.command.api.ICommand;

/**
 * The bootstrap command doesn't extend the functionality of the ICommand interface but it does make for an
 * easier to read command provider implementation.
 *
 * @see IBootstrapCommandProvider
 * @see IBootstrapCommandOptions
 * @see ICommand
 */
public interface IBootstrapCommand extends ICommand<IBootstrapCommandOptions> {

}
