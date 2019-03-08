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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.scenario;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenStep;
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenStep;

import java.util.List;

/**
 * Adapts a {@link Step} instance to {@link IScenarioStep}.
 * This class is not threadsafe.
 */
public class WrappedScenarioStep<T extends Step> extends AbstractWrappedXtext<T> implements IScenarioStep {

   public WrappedScenarioStep(IWrapperResolver resolver, T wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public String getKeyword() {
      return wrapped.getKeyword();
   }

   @Override
   public IScenarioStep setKeyword(String keyword) {
      wrapped.setKeyword(keyword);
      return this;
   }

   @Override
   public List<String> getParameters() {
      return wrapped.getParameters();
   }

   @Override
   public IScenario getParent() {
      // Get the parent scenario object.  The double eContainer() call is due to the fact that steps are wrapped in
      // GivenDeclarations, WhenDeclarations, etc.
      Scenario parentScenario = (Scenario) wrapped.eContainer().eContainer();
      Model parentModel = (Model) parentScenario.eContainer();
      // Now get the scenario for this step.
      return resolver.getWrapperFor(parentModel).getScenarios().getByName(parentScenario.getName()).get();
   }

   /**
    * Creates a new {@code GivenStep} that is equivalent to the given step.  Changes to the {@code
    * IScenarioStep} are not reflected in the returned {@code GivenStep} after construction.
    */
   public static GivenStep toXtextGivenStep(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      GivenStep x = SystemDescriptorFactory.eINSTANCE.createGivenStep();
      x.setKeyword(step.getKeyword());
      x.getParameters().addAll(step.getParameters());
      return x;
   }

   /**
    * Creates a new {@code WhenStep} that is equivalent to the when step.  Changes to the {@code
    * IScenarioStep} are not reflected in the returned {@code WhenStep} after construction.
    */
   public static WhenStep toXtextWhenStep(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      WhenStep x = SystemDescriptorFactory.eINSTANCE.createWhenStep();
      x.setKeyword(step.getKeyword());
      x.getParameters().addAll(step.getParameters());
      return x;
   }

   /**
    * Creates a new {@code ThenStep} that is equivalent to the then step.  Changes to the {@code
    * IScenarioStep} are not reflected in the returned {@code ThenStep} after construction.
    */
   public static ThenStep toXtextThenStep(IScenarioStep step) {
      Preconditions.checkNotNull(step, "step may not be null!");
      ThenStep x = SystemDescriptorFactory.eINSTANCE.createThenStep();
      x.setKeyword(step.getKeyword());
      x.getParameters().addAll(step.getParameters());
      return x;
   }
}
