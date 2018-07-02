package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.google.inject.Inject;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.RestConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.telemetry.TelemetryReportingConfiguration;
import com.ngc.seaside.jellyfish.service.config.api.dto.zeromq.ZeroMqConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class TransportConfigurationServiceGuiceWrapper implements ITransportConfigurationService {

   private final TransportConfigurationService delegate;

   @Inject
   public TransportConfigurationServiceGuiceWrapper(ILogService logService, ISystemDescriptorService sdService) {
      this.delegate = new TransportConfigurationService();
      this.delegate.setLogService(logService);
      this.delegate.setSystemDescriptorService(sdService);
      this.delegate.activate();
   }

   @Override
   public String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field) {
      return this.delegate.getTransportTopicName(flow, field);
   }

   @Override
   public Optional<String> getTelemetryTransportTopicName(IJellyFishCommandOptions options, IModelReferenceField part) {
      return delegate.getTelemetryTransportTopicName(options, part);
   }

   @Override
   public Optional<String> getTelemetryReportingTransportTopicName(IJellyFishCommandOptions options, IModel model) {
      return delegate.getTelemetryReportingTransportTopicName(options, model);
   }

   @Override
   public Collection<MulticastConfiguration> getMulticastConfiguration(IJellyFishCommandOptions options,
            IDataReferenceField field) {
      return delegate.getMulticastConfiguration(options, field);
   }

   @Override
   public Collection<RestConfiguration> getRestConfiguration(IJellyFishCommandOptions options,
            IDataReferenceField field) {
      return delegate.getRestConfiguration(options, field);
   }

   @Override
   public Collection<ZeroMqConfiguration> getZeroMqConfiguration(IJellyFishCommandOptions options,
            IDataReferenceField field) {
      return delegate.getZeroMqConfiguration(options, field);
   }

   @Override
   public Collection<TelemetryConfiguration> getTelemetryConfiguration(IJellyFishCommandOptions options, IModel model) {
      return delegate.getTelemetryConfiguration(options, model);
   }

   @Override
   public Collection<TelemetryReportingConfiguration> getTelemetryReportingConfiguration(
            IJellyFishCommandOptions options, IModel model) {
      return delegate.getTelemetryReportingConfiguration(options, model);
   }

   @Override
   public Set<TransportConfigurationType> getConfigurationTypes(IJellyFishCommandOptions options, IModel model) {
      return delegate.getConfigurationTypes(options, model);
   }

}
