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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transportprovider;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationTemplatePlugin;

/**
 * Interface for configuring a transport provider. Adding a plugin for a transport provider entails creating a
 * {@link ITransportProviderConfigurationDto#getConfigurationType() configuration} class that has a method with the
 * signature {@code public static void configure(TransportConfiguration, ITransportProvider<T>)}.
 */
public interface ITransportProviderConfigurationPlugin<T extends ITransportProviderConfigurationDto>
         extends IConfigurationTemplatePlugin<T> {

}
