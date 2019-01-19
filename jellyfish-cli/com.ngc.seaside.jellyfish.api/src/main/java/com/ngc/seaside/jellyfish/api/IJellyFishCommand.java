/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.api;

/**
 * This interface provides an extension of the {@link ICommand} interface only in the type of options that are
 * presented to the command at run time. The main difference being that of the System Descriptor. The system descriptor
 * is read in at run time via the {@link IJellyFishCommandProvider} implementation and provided to the command.
 */
public interface IJellyFishCommand extends ICommand<IJellyFishCommandOptions> {
}
