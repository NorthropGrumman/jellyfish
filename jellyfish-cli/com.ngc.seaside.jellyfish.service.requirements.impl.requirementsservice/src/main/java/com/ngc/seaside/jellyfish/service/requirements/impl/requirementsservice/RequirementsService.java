package com.ngc.seaside.jellyfish.service.requirements.impl.requirementsservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonValue;

@Component(service = IRequirementsService.class)
public class RequirementsService implements IRequirementsService {

   static final String REQUIREMENTS_KEY = "satisfies";
   
   private ILogService logService;
   
   
   @Override
   public Set<String> getRequirements(IJellyFishCommandOptions options, IMetadata metadata) {
      
      JsonValue value = metadata.getJson().get(REQUIREMENTS_KEY);
      
      if (value instanceof JsonString) {
         return Collections.singleton(((JsonString) value).getString());
      }
      
      if (value instanceof JsonArray) {
         Set<String> requirements = new TreeSet<>();
         for (JsonValue element : (JsonArray) value) {
            if (element instanceof JsonString) {
               requirements.add(((JsonString) element).getString());
            } else {
               logService.warn(RequirementsService.class, "Invalid requirement value: " + element);
            }
         }
         return requirements;
      }
      
      if (value != null) {
         logService.warn(RequirementsService.class, "Invalid requirement value: " + value);
      }
      
      return Collections.emptySet();
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
