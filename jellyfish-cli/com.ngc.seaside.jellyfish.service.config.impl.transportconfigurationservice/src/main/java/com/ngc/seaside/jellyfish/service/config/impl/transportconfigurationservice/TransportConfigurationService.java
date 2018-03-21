package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.config.api.dto.MulticastConfiguration;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IPropertyDataValue;
import com.ngc.seaside.systemdescriptor.model.impl.view.AggregatedModelView;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An implementation of {@code ITransportConfigurationService}.
 */
@Component(service = ITransportConfigurationService.class)
public class TransportConfigurationService implements ITransportConfigurationService {

   static final String MULTICAST_CONFIGURATION_QUALIFIED_NAME = "com.ngc.seaside.deployment.multicast.MulticastConfiguration";
   static final String SOCKET_ADDRESS_FIELD_NAME = "socketAddress";
   static final String ADDRESS_FIELD_NAME = "address";
   static final String PORT_FIELD_NAME = "port";

   private static final Pattern[] PATTERNS = { Pattern.compile("([a-z\\d])([A-Z]+)"),
            Pattern.compile("([A-Z])([A-Z][a-z\\d])") };
   private static final String[] REPLACEMENTS = { "$1_$2", "$1_$2" };

   private ILogService logService;

   @Override
   public String getTransportTopicName(IMessagingFlow flow, IDataReferenceField field) {
      String topic = field.getType().getName();

      for (int i = 0; i < PATTERNS.length; i++) {
         topic = PATTERNS[i].matcher(topic).replaceAll(REPLACEMENTS[i]);
      }

      return topic.toUpperCase();
   }

   @Override
   public Collection<MulticastConfiguration> getMulticastConfiguration(IJellyFishCommandOptions options,
            IDataReferenceField field) {
      IModel deploymentModel = new AggregatedModelView(getDeploymentModel(options));
      Collection<IModelLink<?>> links = findLinks(deploymentModel, field);
      Collection<MulticastConfiguration> configurations = new LinkedHashSet<>();
      for (IModelLink<?> link : links) {
         configurations.addAll(getConfigurations(link));
      }
      return configurations;
   }

   private static IModel getDeploymentModel(IJellyFishCommandOptions options) {
      IParameter<?> deploymentModelParameter = options.getParameters()
                                                      .getParameter(CommonParameters.DEPLOYMENT_MODEL.getName());
      if (deploymentModelParameter == null) {
         throw new IllegalStateException(CommonParameters.DEPLOYMENT_MODEL.getName() + " parameter is not set");
      }
      String deploymentModel = deploymentModelParameter.getStringValue();
      return options.getSystemDescriptor()
                    .findModel(deploymentModel)
                    .orElseThrow(() -> new IllegalStateException("Cannot find deployment model " + deploymentModel));
   }

   /**
    * Returns all of the given model's links that contain the given field as either a target or source.
    */
   private static Collection<IModelLink<?>> findLinks(IModel model, IDataReferenceField field) {
      return model.getLinks()
                  .stream()
                  .filter(link -> Objects.equals(field, link.getSource()) || Objects.equals(field, link.getTarget()))
                  .collect(Collectors.toList());
   }

   private static Collection<MulticastConfiguration> getConfigurations(IModelLink<?> link) {
      Collection<IPropertyDataValue> multicastPropertyValues = link.getProperties()
            .stream()
            .filter(property -> DataTypes.DATA == property.getType())
            .filter(property -> MULTICAST_CONFIGURATION_QUALIFIED_NAME.equals(property.getReferencedDataType().getFullyQualifiedName()))
            .filter(property -> FieldCardinality.SINGLE == property.getCardinality())
            .map(IProperty::getData)
            .collect(Collectors.toList());
      Collection<MulticastConfiguration> configurations = new ArrayList<>(multicastPropertyValues.size());
      for (IPropertyDataValue value : multicastPropertyValues) {
         if (!value.isSet()) {
            throw new IllegalStateException("Multicast configuration is not completely set for link " + link);
         }
         configurations.add(getConfiguration(value));
      }
      return configurations;
   }

   private static MulticastConfiguration getConfiguration(IPropertyDataValue value) {
      MulticastConfiguration configuration = new MulticastConfiguration();
      IPropertyDataValue socket = value.getData(value.getFieldByName(SOCKET_ADDRESS_FIELD_NAME).orElseThrow(
         () -> new IllegalStateException("Missing " + SOCKET_ADDRESS_FIELD_NAME + " field")));
      String address = socket.getPrimitive(socket.getFieldByName(ADDRESS_FIELD_NAME).orElseThrow(
         () -> new IllegalStateException("Missing " + ADDRESS_FIELD_NAME + " field"))).getString();
      BigInteger port = socket.getPrimitive(socket.getFieldByName(PORT_FIELD_NAME).orElseThrow(
         () -> new IllegalStateException("Missing " + PORT_FIELD_NAME + " field"))).getInteger();
      configuration.setAddress(address);
      configuration.setPort(port.intValueExact());
      return configuration;
   }

   @Activate
   public void activate() {
      logService.debug(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

}
