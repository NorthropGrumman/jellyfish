package com.ngc.seaside.jellyfish.service.requirements.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Set;

/**
 * Service for dealing with requirements.
 */
public interface IRequirementsService {

   /**
    * Gets the requirements declared in the given metadata.
    * 
    * @param options the options the current command is being executed with
    * @param metadata used to describe requirement
    * @return set of requirements
    */
   Set<String> getRequirements(IJellyFishCommandOptions options, IMetadata metadata);

   /**
    * Gets the requirements declared in the given field's metadata.
    * 
    * @param options the options the current command is being executed with
    * @param field used to retrieve the requirements
    * @return set of requirements
    */
   default Set<String> getRequirements(IJellyFishCommandOptions options, IReferenceField field) {
      return getRequirements(options, field.getMetadata());
   }

   /**
    * Gets the requirements declared in the given field's metadata.
    * 
    * @param options the options the current command is being executed with
    * @param field used to retrieve the requirements
    * @return set of requirements
    */
   default Set<String> getRequirements(IJellyFishCommandOptions options, IDataField field) {
      return getRequirements(options, field.getMetadata());
   }

   /**
    * Gets the requirements declared in the given data's metadata.
    * 
    * @param options the options the current command is being executed with
    * @param data used to retrieve the requirements
    * @return set of requirements
    */
   default Set<String> getRequirements(IJellyFishCommandOptions options, IData data) {
      return getRequirements(options, data.getMetadata());
   }

   /**
    * Gets the requirements declared in the given enumeration's metadata.
    * 
    * @param options the options the current command is being executed with
    * @param enumeration used to retrieve the requirements
    * @return set of requirements
    */
   default Set<String> getRequirements(IJellyFishCommandOptions options, IEnumeration enumeration) {
      return getRequirements(options, enumeration.getMetadata());
   }

   /**
    * Gets the requirements declared in the given model's metadata.
    * 
    * @param options the options the current command is being executed with
    * @param model used to retrieve the requirements
    * @return set of requirements
    */
   default Set<String> getRequirements(IJellyFishCommandOptions options, IModel model) {
      return getRequirements(options, model.getMetadata());
   }

   /**
    * Gets the requirements declared in the given scenario's metadata.
    * 
    * @param options the options the current command is being executed with
    * @param scenario used to retrieve the requirements
    * @return set of requirements
    */
   default Set<String> getRequirements(IJellyFishCommandOptions options, IScenario scenario) {
      return getRequirements(options, scenario.getMetadata());
   }

}
