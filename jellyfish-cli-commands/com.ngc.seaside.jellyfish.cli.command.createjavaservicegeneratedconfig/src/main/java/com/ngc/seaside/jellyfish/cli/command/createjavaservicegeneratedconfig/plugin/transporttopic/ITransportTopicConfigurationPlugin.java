package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.IConfigurationPlugin;

import java.util.Set;

public interface ITransportTopicConfigurationPlugin<T> extends IConfigurationPlugin {

   Set<ITransportTopicConfigurationDto<T>> getTopicConfigurations(ConfigurationContext context);

   @Override
   default boolean isValid(ConfigurationContext context) {
      return !getTopicConfigurations(context).isEmpty();
   }

}
