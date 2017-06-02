package com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implements the IScenario interface.  Stores the "Given", "When", "Then" clauses that
 * define a Scenario.
 * 
 * @author psnell
 *
 */
public class Scenario implements IScenario {

   private final String name;
   private IModel parent;
   private ArrayList<IScenarioStep> givens;
   private ArrayList<IScenarioStep> whens;
   private ArrayList<IScenarioStep> thens;

   public Scenario(String name) {
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");
      this.name = name;
      this.givens = new ArrayList<IScenarioStep>();
      this.whens = new ArrayList<IScenarioStep>();
      this.thens = new ArrayList<IScenarioStep>();
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
    * @param givens the array of IScenarioStep objects specifying the Given steps of the Scenario
    */
   public void setGivens(ArrayList<IScenarioStep> givens) {
      this.givens = givens;
   }

   /**
    * Sets the Scenario's "whens" array.
    * @param whens the array of IScenarioStep objects specifying the When steps of the Scenario
    */
   public void setWhens(ArrayList<IScenarioStep> whens) {
      this.whens = whens;
   }

   /** Sets the Scenarios "thens" array.
    * 
    * @param thens the array of IScenarioStep objects specifying the Then steps of the Scenario
    */
   public void setThens(ArrayList<IScenarioStep> thens) {
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
     return Objects.equals(name, s.name) &&
             parent == s.parent &&
             Objects.equals(givens, s.givens) &&
             Objects.equals(whens, s.whens) &&
             Objects.equals(thens, s.thens);
   }

   @Override
   public int hashCode() {
     return Objects.hash(name,System.identityHashCode(parent), givens, whens, thens);
   }

   @Override
   public String toString() {
     return "Scenario[" + 
            "name='" + name + '\'' +
            ", parent=" + (parent == null ? "null" : parent.getName()) +
            ", givens=" + givens +
            ", whens=" + whens +
            ", thens=" + thens +
            ']';
   }
}
