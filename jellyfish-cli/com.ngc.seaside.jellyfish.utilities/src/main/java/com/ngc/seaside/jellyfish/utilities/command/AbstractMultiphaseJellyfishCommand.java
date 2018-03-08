package com.ngc.seaside.jellyfish.utilities.command;

import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.IParameter;


public abstract class AbstractMultiphaseJellyfishCommand extends AbstractJellyfishCommand {

   public AbstractMultiphaseJellyfishCommand(String name) {
      super(name);
   }

   protected abstract void runDefaultPhase();

   protected abstract void runDeferredPhase();

   @Override
   protected void doRun() {
      switch (getPhase()) {
         case DEFERRED:
            runDeferredPhase();
            break;
         default:
            runDefaultPhase();
            break;
      }
   }

   protected static IParameter<?> allPhasesParameter() {
      return phaseParameter(JellyfishCommandPhase.DEFAULT, JellyfishCommandPhase.DEFERRED);
   }

   protected static IParameter<?> phaseParameter(JellyfishCommandPhase phase, JellyfishCommandPhase... phases) {
      StringBuilder description = new StringBuilder(CommonParameters.PHASE.getDescription());
      description.append(phase.toString().toLowerCase());
      if (phases != null) {
         for (JellyfishCommandPhase p : phases) {
            description.append(", ").append(p.toString().toLowerCase());
         }
      }

      DefaultParameter<?> p = new DefaultParameter<>(CommonParameters.PHASE.getName());
      p.setDescription(description.toString());
      return p;
   }

   private JellyfishCommandPhase getPhase() {
      JellyfishCommandPhase phase = JellyfishCommandPhase.DEFAULT;

      IParameter<?> param = getOptions().getParameters().getParameter(CommonParameters.PHASE.getName());
      if (param != null) {
         try {
            phase = JellyfishCommandPhase.valueOf(param.getStringValue().toUpperCase());
         } catch (IllegalArgumentException e) {
            throw new CommandException(param.getStringValue() + " is not a valid phase!");
         }
      }

      return phase;
   }
}
