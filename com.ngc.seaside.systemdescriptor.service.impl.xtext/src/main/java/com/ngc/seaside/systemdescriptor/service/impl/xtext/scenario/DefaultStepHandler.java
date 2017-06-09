package com.ngc.seaside.systemdescriptor.service.impl.xtext.scenario;

import com.ngc.seaside.systemdescriptor.scenario.api.IScenarioStepHandler;
import com.ngc.seaside.systemdescriptor.scenario.api.ScenarioStepVerb;

import java.util.Objects;

public class DefaultStepHandler implements IScenarioStepHandler {

   public final static IScenarioStepHandler RECEIVE = new DefaultStepHandler(
         ScenarioStepVerb.create("haveReceived", "receiving", "willReceive"));

   public final static IScenarioStepHandler PUBLISH = new DefaultStepHandler(
         ScenarioStepVerb.create("havePublished", "publishing", "willPublish"));

   public final static IScenarioStepHandler ASK = new DefaultStepHandler(
         ScenarioStepVerb.create("haveAsked", "asking", "willAsk"));

   private final ScenarioStepVerb verb;

   private DefaultStepHandler(ScenarioStepVerb verb) {
      this.verb = verb;
   }

   @Override
   public ScenarioStepVerb getVerb() {
      return verb;
   }

   @Override
   public String toString() {
      return verb.toString();
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof DefaultStepHandler)) {
         return false;
      }
      DefaultStepHandler that = (DefaultStepHandler) o;
      return Objects.equals(verb, that.verb);
   }

   @Override
   public int hashCode() {
      return Objects.hash(verb);
   }
}
