package com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.processor;

import com.ngc.seaside.jellyfish.service.scenario.api.IRequestResponseMessagingFlow;
import com.ngc.seaside.jellyfish.service.scenario.correlation.api.ICorrelationDescription;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Optional;

public class RequestResponseMessagingFlow implements IRequestResponseMessagingFlow {

   @Override
   public IDataReferenceField getInput() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IDataReferenceField getOutput() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public FlowType getFlowType() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Optional<IModelReferenceField> getInvokedServerSideComponent() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Optional<IScenario> getInvokedServerSideScenario() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IScenario getScenario() {
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public Optional<ICorrelationDescription> getCorrelationDescription() {
      throw new UnsupportedOperationException("not implemented");
   }
}
