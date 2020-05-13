/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
