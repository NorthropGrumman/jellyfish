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
package com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget;

import com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget.Budget;
import com.ngc.seaside.jellyfish.cli.command.analyzebudget.budget.SdBudgetAdapter;
import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.DataTypes;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Properties;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import systems.uom.unicode.CLDR;
import tec.uom.se.unit.Units;

import java.util.Optional;
import java.util.Set;

import javax.measure.Quantity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SdBudgetAdapterTest {

   @Mock
   private IModel model;

   @Mock
   private ISystemDescriptorService sdService;

   private SdBudgetAdapter adapter;

   @Before
   public void before() {
      adapter = new SdBudgetAdapter();
      adapter.setSdService(sdService);

      when(sdService.getAggregatedView(model)).thenReturn(model);
   }

   @Test
   public void testAdapter() {
      Properties properties = new Properties();
      when(model.getProperties()).thenReturn(properties);

      properties.add(SdBudgetAdapterTest.getMockedBudget("1.3 mg", "10.9 mg", "mass1"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 g", "mass2"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 kg", "mass3"));

      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 ms", "time1"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 s", "time2"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 min", "time3"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 h", "time4"));

      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 B", "memory1"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 KB", "memory2"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 MB", "memory3"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 GB", "memory4"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 TB", "memory5"));

      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 Hz", "frequency1"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 kHz", "frequency2"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 MHz", "frequency3"));
      properties.add(SdBudgetAdapterTest.getMockedBudget("0", "1 GHz", "frequency4"));

      Set<Budget<? extends Quantity<?>>> budgets = adapter.getBudgets(model);

      assertEquals(16, budgets.size());

      assertEquals(3, budgets.stream()
               .filter(budget -> budget.getProperty().startsWith("mass"))
               .peek(budget -> assertEquals(Units.KILOGRAM, budget.getMinimum().getUnit().getSystemUnit()))
               .count());

      assertEquals(4, budgets.stream()
               .filter(budget -> budget.getProperty().startsWith("time"))
               .peek(budget -> assertEquals(Units.SECOND, budget.getMinimum().getUnit().getSystemUnit()))
               .count());

      assertEquals(5, budgets.stream()
               .filter(budget -> budget.getProperty().startsWith("memory"))
               .peek(budget -> assertEquals(CLDR.BIT, budget.getMinimum().getUnit().getSystemUnit()))
               .count());

      assertEquals(4, budgets.stream()
               .filter(budget -> budget.getProperty().startsWith("frequency"))
               .peek(budget -> assertEquals(Units.HERTZ, budget.getMinimum().getUnit().getSystemUnit()))
               .count());
   }

   /**
    * Creates a mocked budget property.
    * 
    * @param minimum minimum string
    * @param maximum maximum string
    * @param givenBy givenBy string
    * @return mocked budget property
    */
   public static IProperty getMockedBudget(String minimum, String maximum, String givenBy) {
      IProperty property = mock(IProperty.class, Mockito.RETURNS_DEEP_STUBS);
   
      when(property.getReferencedDataType().getFullyQualifiedName()).thenReturn(SdBudgetAdapter.BUDGET_QUALIFIED_NAME);
      when(property.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(property.getType()).thenReturn(DataTypes.DATA);
      when(property.getName()).thenReturn(givenBy);
   
      IDataField maxField = mock(IDataField.class);
      IDataField minField = mock(IDataField.class);
      IDataField givenByField = mock(IDataField.class);
   
      when(property.getData().getFieldByName(SdBudgetAdapter.BUDGET_MINIMUM_FIELD_NAME))
               .thenReturn(Optional.of(minField));
      when(property.getData().getFieldByName(SdBudgetAdapter.BUDGET_MAXIMUM_FIELD_NAME))
               .thenReturn(Optional.of(maxField));
      when(property.getData().getFieldByName(SdBudgetAdapter.BUDGET_GIVEN_BY_FIELD_NAME))
               .thenReturn(Optional.of(givenByField));
   
      when(property.getData().getPrimitive(minField).getString()).thenReturn(minimum);
      when(property.getData().getPrimitive(maxField).getString()).thenReturn(maximum);
      when(property.getData().getPrimitive(givenByField).getString()).thenReturn(givenBy);
   
      return property;
   }

}
