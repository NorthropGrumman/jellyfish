package com.ngc.seaside.jellyfish.service.config.impl.transportconfigurationservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.config.api.ITransportConfigurationService;
import com.ngc.seaside.jellyfish.service.scenario.api.IMessagingFlow;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.regex.Pattern;

/**
 * An implementation of {@code ITransportConfigurationService}.
 */
@Component(service = ITransportConfigurationService.class)
public class TransportConfigurationService implements ITransportConfigurationService {

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
