package com.ngc.seaside.jellyfish.service.data.api;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Service for working with {@link IData}.
 */
public interface IDataService {

   /**
    * Returns the generated java events type (package and name) for the given {@link IData} or {@link IEnumeration}.
    * 
    * @param options the options the current command is being executed with
    * @param data the data or enumeration
    * @return the generated java events type
    */
   TypeDto<?> getEventClass(IJellyFishCommandOptions options, INamedChild<IPackage> data);
   
   /**
    * Returns the generated java message type (package and name) for the given {@link IData} or {@link IEnumeration}.
    * 
    * @param options the options the current command is being executed with
    * @param data the data or enumeration
    * @return the generated java type
    */
   TypeDto<?> getMessageClass(IJellyFishCommandOptions options, INamedChild<IPackage> data);

   /**
    * Returns a set of all {@link IData} and {@link IEnumeration} associated with the given data. This includes all
    * fields and nested fields of any of the given data and any inherited classes of the aforementioned. The values of
    * the returned map are true for a given key if the key is a field or nested field of any of the given data, and
    * false if the key is exclusively an inherited class.
    *
    * @param data collection of data
    * @return set of all {@link IData} and {@link IEnumeration} associated with the given data
    */
   Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(Collection<? extends IData> data);

   /**
    * Returns a set of all {@link IData} and {@link IEnumeration} associated with the given model's input and output
    * data. This includes all fields and nested fields of any of the model's data and any inherited classes of the
    * aforementioned. The values of the returned map are true for a given key if the key is a field or nested field of
    * any of the given data, and false if the key is exclusively an inherited class.
    *
    * @param model model
    * @return set of all {@link IData} and {@link IEnumeration} associated with the given model's data
    */
   default Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(IModel model) {
      Collection<IDataReferenceField> inputs = model.getInputs();
      Collection<IDataReferenceField> outputs = model.getOutputs();
      Collection<IData> data = new ArrayList<>(inputs.size() + outputs.size());
      inputs.forEach(field -> data.add(field.getType()));
      outputs.forEach(field -> data.add(field.getType()));
      return aggregateNestedFields(data);
   }

}
