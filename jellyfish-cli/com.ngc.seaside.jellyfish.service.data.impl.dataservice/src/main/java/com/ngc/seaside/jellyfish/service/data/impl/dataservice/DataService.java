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
package com.ngc.seaside.jellyfish.service.data.impl.dataservice;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

public class DataService implements IDataService {

   private IPackageNamingService packageNamingService;

   @Override
   public Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(Collection<? extends IData> data) {
      Queue<IData> queue = new ArrayDeque<>(data);
      Map<INamedChild<IPackage>, Boolean> map = new LinkedHashMap<>();
      data.forEach(d -> map.put(d, true));
      while (!queue.isEmpty()) {
         IData next = queue.poll();
         IData parent = next.getExtendedDataType().orElse(null);
         while (parent != null) {
            Boolean previous = map.putIfAbsent(parent, false);
            if (previous == null) {
               queue.add(parent);
            } else {
               break;
            }
         }
         for (IDataField field : next.getFields()) {
            switch (field.getType()) {
               case DATA:
                  IData dataField = field.getReferencedDataType();
                  Boolean previous = map.put(dataField, true);
                  if (previous == null) {
                     queue.add(dataField);
                  }
                  break;
               case ENUM:
                  IEnumeration enumField = field.getReferencedEnumeration();
                  map.put(enumField, true);
                  break;
               default:
                  break;
            }
         }
      }

      return map;
   }


   @Override
   public TypeDto<?> getEventClass(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      TypeDto<?> dto = new ClassDto();
      dto.setPackageName(packageNamingService.getEventPackageName(options, data));
      dto.setTypeName(data.getName());
      return dto;
   }


   @Override
   public TypeDto<?> getMessageClass(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      TypeDto<?> dto = new ClassDto();
      dto.setPackageName(packageNamingService.getMessagePackageName(options, data));
      dto.setTypeName(data.getName());
      return dto;
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
   public void setPackageNamingService(IPackageNamingService ref) {
      this.packageNamingService = ref;
   }

   public void removePackageNamingService(IPackageNamingService ref) {
      setPackageNamingService(null);
   }

}
