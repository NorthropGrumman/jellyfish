package com.ngc.seaside.jellyfish.service.data.impl.dataservice;

import com.ngc.seaside.jellyfish.service.data.api.IDataService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

public class DataService implements IDataService {

   @Override
   public Map<INamedChild<IPackage>, Boolean> aggregateNestedFields(Collection<? extends IData> data) {
      Queue<IData> queue = new ArrayDeque<>(data);
      Map<INamedChild<IPackage>, Boolean> map = new LinkedHashMap<>();
      data.forEach(d -> map.put(d, true));
      while (!queue.isEmpty()) {
         IData next = queue.poll();
         IData parent = next.getSuperDataType().orElse(null);
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

}
