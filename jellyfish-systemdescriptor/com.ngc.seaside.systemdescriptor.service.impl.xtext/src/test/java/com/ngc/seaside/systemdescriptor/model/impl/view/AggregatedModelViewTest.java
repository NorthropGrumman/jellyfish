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
package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.DataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario.Scenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AggregatedModelViewTest {

   private AggregatedModelView view;

   @Mock
   private IModel model;

   @Mock
   private IModel parent;

   @Mock
   private IModel grandparent;

   @Before
   public void setup() {
      INamedChildCollection<IModel, IDataReferenceField> dataRefFields;
      INamedChildCollection<IModel, IModelReferenceField> modelRefFields;
      INamedChildCollection<IModel, IScenario> scenarios;
      Collection<IModelLink<?>> links;

      dataRefFields = fields(DataReferenceField.class, "inputA");
      when(model.getInputs()).thenReturn(dataRefFields);
      dataRefFields = fields(DataReferenceField.class, "outputA");
      when(model.getOutputs()).thenReturn(dataRefFields);
      modelRefFields = fields(IModelReferenceField.class, "partA");
      when(model.getParts()).thenReturn(modelRefFields);
      modelRefFields = fields(IModelReferenceField.class, "requirementA");
      when(model.getRequiredModels()).thenReturn(modelRefFields);
      scenarios = fields(Scenario.class, "scenarioA");
      when(model.getScenarios()).thenReturn(scenarios);
      links = links("linkA");
      when(model.getLinks()).thenReturn(links);
      when(model.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(model.getRefinedModel()).thenReturn(Optional.of(parent));
      when(model.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);

      dataRefFields = fields(DataReferenceField.class, "inputB");
      when(parent.getInputs()).thenReturn(dataRefFields);
      dataRefFields = fields(DataReferenceField.class, "outputB");
      when(parent.getOutputs()).thenReturn(dataRefFields);
      modelRefFields = fields(IModelReferenceField.class, "partB");
      when(parent.getParts()).thenReturn(modelRefFields);
      modelRefFields = fields(IModelReferenceField.class, "requirementB");
      when(parent.getRequiredModels()).thenReturn(modelRefFields);
      scenarios = fields(Scenario.class, "scenarioB");
      when(parent.getScenarios()).thenReturn(scenarios);
      links = links("linkB");
      when(parent.getLinks()).thenReturn(links);
      when(parent.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(parent.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);
      when(parent.getRefinedModel()).thenReturn(Optional.of(grandparent));

      dataRefFields = fields(DataReferenceField.class, "inputC");
      when(grandparent.getInputs()).thenReturn(dataRefFields);
      dataRefFields = fields(DataReferenceField.class, "outputC");
      when(grandparent.getOutputs()).thenReturn(dataRefFields);
      modelRefFields = fields(IModelReferenceField.class, "partC");
      when(grandparent.getParts()).thenReturn(modelRefFields);
      modelRefFields = fields(IModelReferenceField.class, "requirementC");
      when(grandparent.getRequiredModels()).thenReturn(modelRefFields);
      scenarios = fields(Scenario.class, "scenarioC");
      when(grandparent.getScenarios()).thenReturn(scenarios);
      links = links("linkC");
      when(grandparent.getLinks()).thenReturn(links);
      when(grandparent.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
      when(grandparent.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);
      when(grandparent.getRefinedModel()).thenReturn(Optional.empty());

      view = new AggregatedModelView(model);
   }

   @Test
   public void testDoesGetInputsFromRefinedModels() {
      assertTrue("missing input field on model object!",
                 view.getInputs().getByName("inputA").isPresent());
      assertTrue("missing input field on parent model object!",
                 view.getInputs().getByName("inputB").isPresent());
      assertTrue("missing input field on grandparent model object!",
                 view.getInputs().getByName("inputC").isPresent());
   }

   @Test
   public void testDoesGetOutputsFromRefinedModels() {
      assertTrue("missing output field on model object!",
                 view.getOutputs().getByName("outputA").isPresent());
      assertTrue("missing output field on parent model object!",
                 view.getOutputs().getByName("outputB").isPresent());
      assertTrue("missing output field on grandparent model object!",
                 view.getOutputs().getByName("outputC").isPresent());
   }

   @Test
   public void testDoesGetPartsFromRefinedModels() {
      assertTrue("missing part field on model object!",
                 view.getParts().getByName("partA").isPresent());
      assertTrue("missing part field on parent model object!",
                 view.getParts().getByName("partB").isPresent());
      assertTrue("missing part field on grandparent model object!",
                 view.getParts().getByName("partC").isPresent());
   }

   @Test
   public void testDoesGetRequirementsFromRefinedModels() {
      assertTrue("missing requirement field on model object!",
                 view.getRequiredModels().getByName("requirementA").isPresent());
      assertTrue("missing requirement field on parent model object!",
                 view.getRequiredModels().getByName("requirementB").isPresent());
      assertTrue("missing requirement field on grandparent model object!",
                 view.getRequiredModels().getByName("requirementC").isPresent());
   }

   @Test
   public void testDoesGetScenariosFromRefinedModels() {
      assertTrue("missing scenario on model object!",
                 view.getScenarios().getByName("scenarioA").isPresent());
      assertTrue("missing scenario on parent model object!",
                 view.getScenarios().getByName("scenarioB").isPresent());
      assertTrue("missing scenario on grandparent model object!",
                 view.getScenarios().getByName("scenarioC").isPresent());
   }

   @Test
   public void testDoesGetLinksFromRefinedModels() {
      assertTrue("missing link on model object!",
                 view.getLinkByName("linkA").isPresent());
      assertTrue("missing link on parent model object!",
                 view.getLinkByName("linkB").isPresent());
      assertTrue("missing link on grandparent model object!",
                 view.getLinkByName("linkC").isPresent());
   }

   /**
    * Creates a collection of fields of the given type with the given name.
    */
   public static <T extends INamedChild<IModel>> INamedChildCollection<IModel, T> fields(Class<? extends T> fieldType,
                                                                                         String... names) {
      NamedChildCollection<IModel, T> collection = new NamedChildCollection<>();
      if (names != null) {
         for (String name : names) {
            T field = Mockito.mock(fieldType);
            when(field.getName()).thenReturn(name);
            if (IModelReferenceField.class.isAssignableFrom(fieldType)) {
               when(((IModelReferenceField) field).getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
               when(((IModelReferenceField) field).getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);
            }
            collection.add(field);
         }
      }
      return collection;
   }

   /**
    * Creates a collection of mocked links with the given names.
    */
   public static Collection<IModelLink<?>> links(String... names) {
      Collection<IModelLink<?>> collection = new ArrayList<>();
      if (names != null) {
         for (String name : names) {
            IModelLink<?> link = Mockito.mock(IModelLink.class);
            when(link.getName()).thenReturn(Optional.ofNullable(name));
            when(link.getProperties()).thenReturn(IProperties.EMPTY_PROPERTIES);
            when(link.getMetadata()).thenReturn(IMetadata.EMPTY_METADATA);
            collection.add(link);
         }
      }
      return collection;
   }
}
