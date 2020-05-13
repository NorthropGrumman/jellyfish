/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.model.SequenceFlowImplementation;

/**
 * Finds undeclared flows by looking for implementations that exists but aren't declared.  This functionality is
 * currently disabled.
 */
public class SyntheticFlowImplementationGenerator extends AbstractFlowImplementationGenerator {

   /**
    * The context object that contains the configuration of the generator.
    */
   private SyntheticFlowImplementationGeneratorContext context;

   /**
    * Creates a new SyntheticFlowImplementationGenerator.
    *
    * @param scenarioService the scenario service
    * @param logService      the log service
    */
   public SyntheticFlowImplementationGenerator(IScenarioService scenarioService,
                                               ILogService logService) {
      super(scenarioService, logService);
   }

   /**
    * Attempts to find a single high level implementation of a flow that is not declared.
    *
    * @param context the context object that configures the generator
    * @return an implementation of an undeclared flow or {@code null} if no undeclared flows where found
    */
   public SequenceFlowImplementation generateSyntheticImplementation(
         SyntheticFlowImplementationGeneratorContext context) {
      // This method returns only one impl because we actually group all inputs and outputs
      // that form flows into a single undeclared flow with multiple inputs and outputs.
      this.context = context;
      this.options = context.getOptions();

      logService.trace(FlowImplementationGenerator.class,
                       "Attempting to find undeclared flows for %s with inputs %s.",
                       context.getModel().getFullyQualifiedName(),
                       context.getInputs());

      SequenceFlowImplementation impl = findImpl();
      if (impl == null) {
         logService.trace(getClass(),
                          "Did not find any undeclared flows for %s.",
                          context.getModel().getFullyQualifiedName());
      } else {
         logService.trace(getClass(),
                          "Found an undeclared flow for %s.",
                          context.getModel().getFullyQualifiedName());
      }

      return impl;
   }

   private SequenceFlowImplementation findImpl() {
      // Discovery any flows.
      SequenceFlowImplementation impl = new SequenceFlowImplementation();
      populateImpl(impl, context.getModel(), context.getInputs());

      // Only include if this implementation if there is not a flow already declared that has this implementation.
      // Also, if the resulting impl has no flows, there is no undeclared flow.
      return impl.getFlows().isEmpty() || isAlreadyDeclared(impl) ? null : impl;
   }

   private boolean isAlreadyDeclared(SequenceFlowImplementation impl) {
      // TODO TH: This is why the test case fails.  It is discovering what it thinks is an undeclared flow but it is
      // actually declared.
      return context.getDeclaredFlows()
            .stream()
            .filter(f -> f.getImplementation().isPresent())
            .map(f -> (SequenceFlowImplementation) f.getImplementation().get())
            .anyMatch(impl::isEquivalent);
   }
}
