/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.data.impl.dataservice;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;

import org.junit.Test;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataServiceTest {

   private final DataService service = new DataService();

   @Test
   public void testDataService() {
      IData[] data = new IData[18];
      for (int n = 0; n < data.length; n++) {
         data[n] = getMockData(IData.class, "test" + n + ".Data" + n);
      }
      IEnumeration[] enums = new IEnumeration[4];
      for (int n = 0; n < enums.length; n++) {
         enums[n] = getMockData(IEnumeration.class, "test" + n + ".Enum" + n);
      }

      setupData(data[0], data[1], enums[0], DataTypes.INT, data[3], data[4], data[5]);
      setupData(data[1], data[2], enums[1], DataTypes.STRING, data[5], data[6], data[7]);
      setupData(data[2], null, enums[2], DataTypes.FLOAT, data[7], data[8]);
      setupData(data[3], data[9], data[10], enums[3]);
      setupData(data[9], data[11], data[12]);
      setupData(data[11], data[13], data[14]);
      setupData(data[12], null, data[15]);
      setupData(data[15], data[16], data[17], data[11]);
      for (IData d : data) {
         if (d.getFields() == null) {
            when(d.getFields()).thenReturn(new NamedChildCollection(d));
         }
      }

      @SuppressWarnings("unchecked")
      INamedChild<IPackage>[] expectedFields = new INamedChild[]{data[0], data[3], data[4], data[5], data[6], data[7],
                                                                 data[8], data[10], data[11], data[12], data[14],
                                                                 data[15], data[17], enums[0], enums[1], enums[2],
                                                                 enums[3]};
      IData[] expectedSuperClasses = {data[1], data[2], data[9], data[13], data[16]};

      Collection<IData> input = Arrays.asList(data[0]);
      Map<INamedChild<IPackage>, Boolean> map = service.aggregateNestedFields(input);

      for (INamedChild<IPackage> child : expectedFields) {
         Boolean result = map.get(child);
         assertNotNull(child.getParent().getName() + '.' + child.getName() + " was not returned", result);
         assertTrue(child.getParent().getName() + '.' + child.getName() + " was returned as a super class", result);
      }
      for (IData superClass : expectedSuperClasses) {
         Boolean result = map.get(superClass);
         assertNotNull(superClass.getFullyQualifiedName() + " was not returned", result);
         assertFalse(superClass.getFullyQualifiedName() + " was returned as a field", result);
      }

   }

   private static <T extends INamedChild<IPackage>> T getMockData(Class<? extends T> cls, String fullyQualifiedName) {
      T data = mock(cls);
      if (data instanceof IData) {
         when(((IData) data).getFullyQualifiedName()).thenReturn(fullyQualifiedName);
      } else if (data instanceof IEnumeration) {
         when(((IEnumeration) data).getFullyQualifiedName()).thenReturn(fullyQualifiedName);
      }
      int index = fullyQualifiedName.lastIndexOf('.');
      when(data.getName()).thenReturn(fullyQualifiedName.substring(index + 1));
      when(data.getParent()).thenReturn(mock(IPackage.class));
      when(data.getParent().getName()).thenReturn(fullyQualifiedName.substring(0, index));
      return data;
   }

   private static void setupData(IData data, IData superClass, Object... fields) {
      when(data.getExtendedDataType()).thenReturn(Optional.ofNullable(superClass));
      NamedChildCollection collection = new NamedChildCollection(data);
      int i = 0;
      for (Object field : fields) {
         collection.addField("field" + (i++), field);
      }
      when(data.getFields()).thenReturn(collection);
   }

   private static class NamedChildCollection extends AbstractSet<IDataField>
         implements INamedChildCollection<IData, IDataField> {

      private final Map<String, IDataField> map = new LinkedHashMap<>();
      private final IData parent;

      public NamedChildCollection(IData parent) {
         this.parent = parent;
      }

      public void addField(String name, Object value) {
         IDataField field = mock(IDataField.class);
         when(field.getName()).thenReturn(name);
         when(field.getParent()).thenReturn(parent);
         if (value instanceof IData) {
            when(field.getType()).thenReturn(DataTypes.DATA);
            when(field.getReferencedDataType()).thenReturn((IData) value);
         } else if (value instanceof IEnumeration) {
            when(field.getType()).thenReturn(DataTypes.ENUM);
            when(field.getReferencedEnumeration()).thenReturn((IEnumeration) value);
         } else if (value instanceof DataTypes) {
            when(field.getType()).thenReturn((DataTypes) value);
         }
         when(field.getCardinality()).thenReturn(FieldCardinality.SINGLE);
         map.put(name, field);
      }

      @Override
      public Optional<IDataField> getByName(String name) {
         return Optional.ofNullable(map.get(name));
      }

      @Override
      public Iterator<IDataField> iterator() {
         return map.values().iterator();
      }

      @Override
      public int size() {
         return map.size();
      }

   }

}
