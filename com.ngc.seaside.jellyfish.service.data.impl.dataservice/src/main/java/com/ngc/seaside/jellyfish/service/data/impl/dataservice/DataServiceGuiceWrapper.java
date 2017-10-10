package com.ngc.seaside.jellyfish.service.data.impl.dataservice;

import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.Map;

public class DataServiceGuiceWrapper implements IDataService {

   private final DataService delegate = new DataService();
   
   @Override
   public Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(Collection<? extends IData> data) {
      return delegate.aggregateNestedFields(data);
   }
   
   @Override
   public Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(IModel model) {
      return delegate.aggregateNestedFields(model);
   }

}
