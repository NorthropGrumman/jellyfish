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
package com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implements the IScenario interface.  Stores the "Given", "When", "Then" clauses that
 * define a Scenario.
 */
public class Scenario implements IScenario {

   private final String name;
   private IModel parent;
   private IMetadata metadata;
   private List<IScenarioStep> givens;
   private List<IScenarioStep> whens;
   private List<IScenarioStep> thens;

   /**
    * Creates a new scenario.
    */
   public Scenario(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
      this.givens = new ArrayList<>();
      this.whens = new ArrayList<>();
      this.thens = new ArrayList<>();
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public IModel getParent() {
      return parent;
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IScenario setMetadata(IMetadata metadata) {
      this.metadata = metadata;
      return this;
   }

   @Override
   public List<IScenarioStep> getGivens() {
      return givens;
   }

   @Override
   public List<IScenarioStep> getWhens() {
      return whens;
   }

   @Override
   public List<IScenarioStep> getThens() {
      return thens;
   }

   /**
    * Add a single "given" IScenarioStep to this Scenario
    *
    * @param given is the IScenarioStep to add
    * @return this Scenario object
    */
   public Scenario addGiven(IScenarioStep given) {
      givens.add(given);
      return this;
   }

   /**
    * Add a single "when" IScenarioStep to this Scenario
    *
    * @param when is the IScenarioStep to add
    * @return this Scenario object
    */
   public Scenario addWhen(IScenarioStep when) {
      whens.add(when);
      return this;
   }

   /**
    * Add a single "then" IScenarioStep to this Scenario
    *
    * @param then is the IScenarioStep to add
    * @return this Scenario object
    */
   public Scenario addThen(IScenarioStep then) {
      thens.add(then);
      return this;
   }

   /**
    * Sets the Scenario's "givens" array.
    *
    * @param givens the array of IScenarioStep objects specifying the Given steps of the Scenario
    */
   public void setGivens(List<IScenarioStep> givens) {
      this.givens = givens;
   }

   /**
    * Sets the Scenario's "whens" array.
    *
    * @param whens the array of IScenarioStep objects specifying the When steps of the Scenario
    */
   public void setWhens(List<IScenarioStep> whens) {
      this.whens = whens;
   }

   /**
    * Sets the Scenarios "thens" array.
    *
    * @param thens the array of IScenarioStep objects specifying the Then steps of the Scenario
    */
   public void setThens(List<IScenarioStep> thens) {
      this.thens = thens;
   }

   public Scenario setParent(IModel model) {
      parent = model;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Scenario)) {
         return false;
      }

      Scenario s = (Scenario) o;
      return Objects.equals(name, s.name)
             && parent == s.parent
             && Objects.equals(metadata, s.metadata)
             && Objects.equals(givens, s.givens)
             && Objects.equals(whens, s.whens)
             && Objects.equals(thens, s.thens);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, System.identityHashCode(parent), metadata, givens, whens, thens);
   }

   @Override
   public String toString() {
      return "Scenario["
             + "name='" + name + '\''
             + ", parent=" + (parent == null ? "null" : parent.getName())
             + ", metadata=" + metadata
             + ", givens=" + givens
             + ", whens=" + whens
             + ", thens=" + thens
             + ']';
   }
}
