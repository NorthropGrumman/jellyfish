package com.ngc.seaside.jellyfish.utilities.command;

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
      IParameter<?> phase = getOptions().getParameters().getParameter(CommonParameters.PHASE.getName());
      if (phase == null) {
         runDefaultPhase();
      } else {
         switch (JellyfishCommandPhase.valueOf(phase.getStringValue().toUpperCase())) {
            case DEFAULT:
               runDefaultPhase();
               break;
            case DEFERRED:
               runDeferredPhase();
               break;
            default:
               throw new IllegalStateException("this abstract class needs to be updated to handle the phase "
                                               + phase.getStringValue());
         }
      }
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
}
