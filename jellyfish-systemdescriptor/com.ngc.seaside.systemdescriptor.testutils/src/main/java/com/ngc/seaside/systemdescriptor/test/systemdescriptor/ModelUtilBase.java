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
package com.ngc.seaside.systemdescriptor.test.systemdescriptor;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.scenario.impl.standardsteps.CorrelateStepHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ModelUtilBase implements IModel {

   protected final String name;
   protected final IPackage parent;
   protected final ModelUtils.NamedChildCollection<IModel, IDataReferenceField> inputs;
   protected final ModelUtils.NamedChildCollection<IModel, IDataReferenceField> outputs;
   protected final ModelUtils.NamedChildCollection<IModel, IScenario> scenarios;
   protected IModel refinedModel;

   protected ModelUtilBase(String fullyQualifiedName) {
      int index = fullyQualifiedName.lastIndexOf('.');
      assertTrue(fullyQualifiedName + " is not a fully qualified name", index >= 0);
      name = fullyQualifiedName.substring(index + 1);
      parent = mock(IPackage.class);
      when(parent.getName()).thenReturn(fullyQualifiedName.substring(0, index));
      inputs = new ModelUtils.NamedChildCollection<>(this, IDataReferenceField.class);
      outputs = new ModelUtils.NamedChildCollection<>(this, IDataReferenceField.class);
      scenarios = new ModelUtils.NamedChildCollection<>(this, IScenario.class);
   }

   /**
    * Adds the given field to this model's inputs.
    *
    * @param field data reference field
    */
   public void addInput(IDataReferenceField field) {
      inputs.add(field);
   }

   /**
    * Creates a mocked {@link IDataReferenceField} with the given name, given data, and {@link FieldCardinality#SINGLE}
    * and adds it to this model's inputs.
    *
    * @param name reference field name
    * @param data reference field type
    * @return the mocked {@link IDataReferenceField}
    */
   public IDataReferenceField addInput(String name, IData data) {
      IDataReferenceField field = mock(IDataReferenceField.class);
      when(field.getName()).thenReturn(name);
      when(field.getParent()).thenReturn(this);
      when(field.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(field.getType()).thenReturn(data);
      inputs.add(field);
      return field;
   }

   /**
    * Adds the given field to this model's outputs.
    *
    * @param field data reference field
    */
   public void addOutput(IDataReferenceField field) {
      outputs.add(field);
   }

   /**
    * Creates a mocked {@link IDataReferenceField} with the given name, given data, and {@link FieldCardinality#SINGLE}
    * and adds it to this model's outputs.
    *
    * @param name reference field name
    * @param data reference field type
    * @return the mocked {@link IDataReferenceField}
    */
   public IDataReferenceField addOutput(String name, IData data) {
      IDataReferenceField field = mock(IDataReferenceField.class);
      when(field.getName()).thenReturn(name);
      when(field.getParent()).thenReturn(this);
      when(field.getCardinality()).thenReturn(FieldCardinality.SINGLE);
      when(field.getType()).thenReturn(data);
      outputs.add(field);
      return field;
   }

   public void addScenario(IScenario scenario) {
      scenarios.add(scenario);
   }

   /**
    * Creates a mocked generic{@link IScenario} with the given name and adds it to this model.  The steps of the
    * scenario will have no configured verbs or keywords.  The step parameters supplied should be ordered by given
    * statements first, followed by when statement, followed by then statements. The step parameters can either be
    * {@link IDataReferenceField} or a pair of {@link String} followed by {@link IData}.  This method adds the scenario
    * to this model's scenarios and data types to this model's inputs and outputs.
    *
    * @param givens         number of given steps
    * @param whens          number of when steps
    * @param thens          number of then steps
    * @param stepParameters steps
    * @return the mocked  {@link IScenario}
    */
   public IScenario addScenario(String name,
                                int givens,
                                int whens,
                                int thens,
                                Object... stepParameters) {
      IScenario scenario = mock(IScenario.class);
      when(scenario.getName()).thenReturn(name);
      when(scenario.getParent()).thenReturn(this);
      when(scenario.getSteps(any(), any())).thenCallRealMethod();

      List<IScenarioStep> steps = new ArrayList<>(givens + whens + thens);
      List<IDataReferenceField> references = new ArrayList<>();
      for (int n = 0; n < stepParameters.length; n++) {
         IDataReferenceField reference;
         if (stepParameters[n] instanceof String) {
            assertNotEquals(n + 1, stepParameters.length);
            assertTrue(stepParameters[n + 1] + " is not of type IData", stepParameters[n + 1] instanceof IData);
            reference = mock(IDataReferenceField.class);
            when(reference.getCardinality()).thenReturn(FieldCardinality.SINGLE);
            when(reference.getName()).thenReturn((String) stepParameters[n]);
            when(reference.getParent()).thenReturn(this);
            when(reference.getType()).thenReturn((IData) stepParameters[n + 1]);
            n++;
         } else if (stepParameters[n] instanceof IDataReferenceField) {
            reference = (IDataReferenceField) stepParameters[n];
         } else {
            throw new AssertionError("Unknown type for pub sub scenario step: " + stepParameters[n]);
         }
         references.add(reference);
      }
      assertEquals(givens + whens + thens, references.size());

      references.forEach(reference -> {
         IScenarioStep step = mock(IScenarioStep.class);
         when(step.getParent()).thenReturn(scenario);
         String referenceName = reference.getName();
         when(step.getParameters()).thenReturn(Collections.singletonList(referenceName));
         steps.add(step);
      });

      when(scenario.getGivens()).thenReturn(new ArrayList<>(steps.subList(0, givens)));
      when(scenario.getWhens()).thenReturn(new ArrayList<>(steps.subList(givens, givens + whens)));
      when(scenario.getThens()).thenReturn(new ArrayList<>(steps.subList(givens + whens, givens + whens + thens)));

      scenarios.add(scenario);
      inputs.addAll(references.subList(0, givens + whens));
      outputs.addAll(references.subList(givens + whens, givens + whens + thens));

      return scenario;
   }

   /**
    * Adds a correlation to the given scenario.
    *
    * @param scenario name of scenario
    * @param from     first correlation statement
    * @param to       second correlation statement
    */
   public void correlate(String scenario, String from, String to) {
      IScenario sc = this.getScenarios().getByName(scenario).orElseThrow(
            () -> new AssertionError("Scenario " + scenario + " has not been defined"));
      String[] first = from.split("\\.");
      String[] second = to.split("\\.");
      assertTrue("Invalid correlation: " + from, first.length > 1);
      assertTrue("Invalid correlation: " + to, second.length > 1);
      IScenarioStep correlationStep = mock(IScenarioStep.class);
      when(correlationStep.getParameters()).thenReturn(Arrays.asList(from, "to", to));
      when(correlationStep.getParent()).thenReturn(sc);
      if (inputs.getByName(first[0]).isPresent() && inputs.getByName(second[0]).isPresent()) {
         when(correlationStep.getKeyword()).thenReturn(CorrelateStepHandler.PRESENT.getVerb());
         sc.getWhens().add(correlationStep);
      } else {
         when(correlationStep.getKeyword()).thenReturn(CorrelateStepHandler.FUTURE.getVerb());
         sc.getThens().add(correlationStep);
      }
   }

   @Override
   public INamedChildCollection<IModel, IDataReferenceField> getInputs() {
      return inputs;
   }

   @Override
   public INamedChildCollection<IModel, IDataReferenceField> getOutputs() {
      return outputs;
   }

   @Override
   public INamedChildCollection<IModel, IScenario> getScenarios() {
      return scenarios;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public IPackage getParent() {
      return parent;
   }

   @Override
   public String getFullyQualifiedName() {
      return parent.getName() + '.' + getName();
   }

   @Override
   public IMetadata getMetadata() {
      return null;
   }

   @Override
   public IModel setMetadata(IMetadata metadata) {
      throw new UnsupportedOperationException();
   }

   @Override
   public IProperties getProperties() {
      return null;
   }

   @Override
   public IModel setProperties(IProperties properties) {
      throw new UnsupportedOperationException();
   }

   @Override
   public INamedChildCollection<IModel, IModelReferenceField> getRequiredModels() {
      return ModelUtils.mockedNamedCollectionOf();
   }

   @Override
   public INamedChildCollection<IModel, IModelReferenceField> getParts() {
      return ModelUtils.mockedNamedCollectionOf();
   }

   @Override
   public Collection<IModelLink<?>> getLinks() {
      return Collections.emptySet();
   }

   @Override
   public Optional<IModelLink<?>> getLinkByName(String name) {
      return Optional.empty();
   }

   @Override
   public Optional<IModel> getRefinedModel() {
      return Optional.ofNullable(refinedModel);
   }

   @Override
   public IModel setRefinedModel(IModel refinedModel) {
      this.refinedModel = refinedModel;
      return this;
   }
}
