package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.ClassDto;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
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
      Queue<INamedChild<IPackage>> queue = new ArrayDeque<>();
      return map;
   }

}
