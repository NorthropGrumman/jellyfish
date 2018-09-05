/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.io;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.ConfigurationContext;
import com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.plugin.transporttopic.zeromq.ZeroMqTcpConfigurationDto;
import com.ngc.seaside.jellyfish.service.codegen.api.IJavaServiceGenerationService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqTcpTransportConfiguration;
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
public class ZeroMqIOTopicPlugin extends AbstractIOTopicPlugin<ZeroMqTcpConfigurationDto> {

   @Inject
   public ZeroMqIOTopicPlugin(ITransportConfigurationService service, IJavaServiceGenerationService generateService,
                              IScenarioService scenarioService) {
      super(service, generateService, scenarioService);
   }

   @Override
   protected Collection<ZeroMqTcpConfigurationDto> getConfigurations(ConfigurationContext context,
            IDataReferenceField field, boolean shouldSend, MessagingParadigm paradigm) {
      boolean pubSub = paradigm == MessagingParadigm.PUBLISH_SUBSCRIBE;
      return service.getZeroMqConfiguration(context.getOptions(), field).stream()
               .filter(ZeroMqTcpTransportConfiguration.class::isInstance)
               .map(ZeroMqTcpTransportConfiguration.class::cast)
               .map(config -> new ZeroMqTcpConfigurationDto(config, shouldSend && pubSub, !shouldSend && pubSub,
                        shouldSend && !pubSub, !shouldSend && !pubSub))
               .collect(Collectors.toCollection(LinkedHashSet::new));
   }

   @Override
   public Set<String> getDependencies(ConfigurationContext context, DependencyType dependencyType) {
      return Collections.emptySet();
   }
}
