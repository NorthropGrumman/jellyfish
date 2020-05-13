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
package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class MockedDataService implements IDataService {

   private final IPackageNamingService packageService = new MockedPackageNamingService();

   @Override
   public TypeDto<?> getEventClass(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      ClassDto dto = new ClassDto();
      dto.setTypeName(data.getName());
      dto.setPackageName(packageService.getEventPackageName(options, data));
      return dto;
   }

   @Override
   public TypeDto<?> getMessageClass(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      ClassDto dto = new ClassDto();
      dto.setTypeName(data.getName());
      dto.setPackageName(packageService.getMessagePackageName(options, data));
      return dto;
   }

   @Override
   public Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(Collection<? extends IData> data) {
      Map<INamedChild<IPackage>, Boolean> map = new LinkedHashMap<>();
      Queue<INamedChild<IPackage>> queue = new ArrayDeque<>(data);
      data.forEach(element -> map.put(element, true));
      while (!queue.isEmpty()) {
         INamedChild<IPackage> element = queue.poll();
         if (element instanceof IData) {
            Optional<IData> parent = ((IData) element).getExtendedDataType();
            while (parent.isPresent()) {
               if (map.putIfAbsent(parent.get(), false) == null) {
                  queue.add(parent.get());
               } else {
                  break;
               }
            }
            
            for (IDataField field : ((IData) element).getFields()) {
               if (field.getType() == DataTypes.DATA) {
                  IData dataField = field.getReferencedDataType();
                  if (map.put(dataField, true) == null) {
                     queue.add(dataField);
                  }
               } else if (field.getType() == DataTypes.ENUM) {
                  map.put(field.getReferencedEnumeration(), true);
               }
            }
         }
      }
      return map;
   }

}
