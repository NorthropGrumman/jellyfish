package com.ngc.seaside.jellyfish.service.data.impl.dataservice;

import com.google.inject.Inject;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.codegen.api.dto.TypeDto;
import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.Map;

public class DataServiceGuiceWrapper implements IDataService {

   private final DataService delegate = new DataService();
   
   @Inject
   public DataServiceGuiceWrapper(IPackageNamingService packageService) {
      this.delegate.setPackageNamingService(packageService);
   }
   
   @Override
   public Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(Collection<? extends IData> data) {
      return delegate.aggregateNestedFields(data);
   }
   
   @Override
   public Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(IModel model) {
      return delegate.aggregateNestedFields(model);
   }

   @Override
   public TypeDto<?> getEventClass(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      return delegate.getEventClass(options, data);
   }

   @Override
   public TypeDto<?> getMessageClass(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      return delegate.getMessageClass(options, data);
   }

}
