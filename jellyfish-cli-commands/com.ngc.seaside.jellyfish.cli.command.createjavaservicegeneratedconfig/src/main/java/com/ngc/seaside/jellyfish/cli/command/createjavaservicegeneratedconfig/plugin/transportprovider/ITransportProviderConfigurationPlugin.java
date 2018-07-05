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
