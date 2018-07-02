package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.multicast.MulticastConfigurationDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.api.MessagingParadigm;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

/**
 * Adds configuration topics so that a service can receive and respond to its inputs and outputs.
 */
public class MulticastIOTopicPlugin extends AbstractIOTopicPlugin<MulticastConfigurationDto> {

   @Inject
   public MulticastIOTopicPlugin(ITransportConfigurationService service, IJavaServiceGenerationService generateService,
                                 IScenarioService scenarioService) {
      super(service, generateService, scenarioService);
   }

   @Override
   protected Collection<MulticastConfigurationDto> getConfigurations(ConfigurationContext context,
            IDataReferenceField field, boolean shouldSend, MessagingParadigm paradigm) {
      return service.getMulticastConfiguration(context.getOptions(), field)
               .stream()
               .map(config -> new MulticastConfigurationDto(config, shouldSend, !shouldSend))
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return Collections.emptySet();
   }
}
