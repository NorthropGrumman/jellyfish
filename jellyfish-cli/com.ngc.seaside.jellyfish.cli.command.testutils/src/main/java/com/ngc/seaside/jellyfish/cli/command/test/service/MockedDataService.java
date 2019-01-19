/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
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
