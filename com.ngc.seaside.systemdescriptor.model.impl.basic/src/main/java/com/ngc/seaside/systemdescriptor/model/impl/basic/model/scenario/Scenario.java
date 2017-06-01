package com.ngc.seaside.systemdescriptor.model.impl.basic.model.scenario;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import java.util.ArrayList;
import java.util.List;

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
   
   public Scenario addGiven(IScenarioStep given) {
      givens.add(given);
      return this;
   }
   
   public Scenario addWhen(IScenarioStep when) {
      whens.add(when);
      return this;
   }
   
   public Scenario addThen(IScenarioStep then) {
      thens.add(then);
      return this;
   }

   public void setGivens(ArrayList<IScenarioStep> givens) {
      this.givens = givens;
   }

   public void setWhens(ArrayList<IScenarioStep> whens) {
      this.whens = whens;
   }

   public void setThens(ArrayList<IScenarioStep> thens) {
      this.thens = thens;
   }

   public Scenario setParent(IModel model) {
      parent = model;
      return this;
   }

}
