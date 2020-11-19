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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.scenario.api.IScenarioService;
import com.ngc.seaside.jellyfish.service.scenario.impl.scenarioservice.ScenarioServiceModule;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateMultipleSequencesPubSubSystemTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreatePubSubSystemOfSystemsTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForPubSubSystemWithCorrelatedInputsTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForPubSubSystemWithDuplicateFlowImplTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForPubSubSystemWithFlowImplTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForPubSubSystemWithFlowImplWithMultipleInnerFlows;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForPubSubSystemWithSyntheticFlowsTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForSinglePubSubComponentTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForSinglePubSubComponentWithCorrelatedInputsTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForSinglePubSubComponentWithDuplicateInputsTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceForSinglePubSubComponentWithMultipleScenariosTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceWithDuplicateFlowsIfDuplicateInputsReceivedTestCase;
import com.ngc.seaside.jellyfish.service.sequence.impl.sequenceservice.cases.CreateSequenceWithDuplicateInputsTestCase;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.scenario.impl.module.StepsSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.repository.api.IRepositoryService;

@RunWith(MockitoJUnitRunner.class)
public class SequenceServiceIT {

   private static ISystemDescriptor systemDescriptor;

   private static IScenarioService scenarioService;

   private static ILogService logService;

   private static final Module testModule = new AbstractModule() {
      @Override
      protected void configure() {
         bind(ILogService.class).toInstance(logService);
         bind(IRepositoryService.class).toInstance(mock(IRepositoryService.class));
         install(new ScenarioServiceModule());
      }
   };

   private SequenceService service;

   @Mock
   private IJellyFishCommandOptions options;

   @BeforeClass
   public static void setupClass() {
      logService = mock(ILogService.class);
      // Uncomment this line to see details about each sequence in the console.
      // logService = new PrintStreamLogService();

      // Save time and only parse the project once for all tests.
      Injector injector = startGuice();
      systemDescriptor = getSystemDescriptor(injector);
      scenarioService = injector.getInstance(IScenarioService.class);
   }

   @Before
   public void setup() {
      service = new SequenceService();
      service.setLogService(logService);
      service.setScenarioService(scenarioService);
      service.activate();
   }

   /**
    * This test verifies the sequence of a simple pub/sub component.  This component only has a single scenario and one
    * input and one output.  The resulting sequence contains one flow with no impl.
    */
   @Test
   public void testDoesCreateSequenceForSinglePubSubComponent() {
      new CreateSequenceForSinglePubSubComponentTestCase(state()).execute();
   }

   /**
    * This test verifies the sequence of a simple pub/sub component.  This component only has a single scenario and two
    * inputs and one output.  The inputs must be correlated.  The resulting sequence contains one flow with no impl.
    */
   @Test
   public void testDoesCreateSequenceForSinglePubSubComponentWithCorrelatedInputs() {
      new CreateSequenceForSinglePubSubComponentWithCorrelatedInputsTestCase(state()).execute();
   }

   /**
    * This test verifies the sequence of a simple pub/sub component with a single scenario yields two flows because it
    * is activated twice due to the duplicate inputs.
    */
   @Test
   public void testDoesCreateSequenceWithDuplicateFlowsWithDuplicationInputs() {
      new CreateSequenceForSinglePubSubComponentWithDuplicateInputsTestCase(state()).execute();
   }

   /**
    * This test verifies the sequence of a simple pub/sub component with multiple scenarios yields two flows.
    */
   @Test
   public void testDoesCreateSequenceWithMultipleFlowsIfMultipleScenariosAreActivated() {
      new CreateSequenceForSinglePubSubComponentWithMultipleScenariosTestCase(state()).execute();
   }

   /**
    * This test verifies a sequence has a flow with an implementation.
    */
   @Test
   public void testDoesCreateSequenceForPubSubSystemWithFlowImplementation() {
      new CreateSequenceForPubSubSystemWithFlowImplTestCase(state()).execute();
   }

   /**
    * This test uses the Threat Evaluation system.  The resulting sequence should contain a single top level flow with
    * an implementation.  The implementation itself contains 2 flows.
    */
   @Test
   public void testDoesCreateSequenceForPubSubSystemWithWithFlowImplementationWhichContainsMultipleInnerFlows() {
      new CreateSequenceForPubSubSystemWithFlowImplWithMultipleInnerFlows(state()).execute();
   }

   /**
    * This test uses the Threat Evaluation system.  The resulting sequence contains a flow whose implementation uses
    * correlation.
    */
   @Test
   public void testDoesCreateSequenceForPubSubSystemWithCorrelatedInputs() {
      new CreateSequenceForPubSubSystemWithCorrelatedInputsTestCase(state()).execute();
   }

   /**
    * This tests ensures that sequences are generated with duplicate flows if the inputs to the system require a flow
    * to be executed twice.
    */
   @Test
   public void testDoesCreateSequenceWithDuplicateFlowsIfDuplicateInputsReceived() {
      new CreateSequenceWithDuplicateFlowsIfDuplicateInputsReceivedTestCase(state()).execute();
   }

   /**
    * This test ensures that a sequence has duplicate outputs if duplicate inputs cause a flow to be executed multiple
    * times.
    */
   @Test
   public void testDoesCreateSequenceWithDuplicateInputs() {
      new CreateSequenceWithDuplicateInputsTestCase(state()).execute();
   }

   /**
    * This tests ensures that multiple sequences are generated correctly.
    */
   @Test
   public void testDoesCreateMultipleSequencesPubSubSystem() {
      new CreateMultipleSequencesPubSubSystemTestCase(state()).execute();
   }

   /**
    * This test ensures that sequences are generated correctly for systems of systems.
    */
   @Test
   public void testDoesCreateSequencesForPubSubSystemOfSystems() {
      new CreatePubSubSystemOfSystemsTestCase(state()).execute();
   }

   /**
    * This test ensures that undeclared flows are actually detected.
    */
   @Test
   @Ignore("This test is about finding flows that have not been declared")
   public void testDoesCreateSequenceWithSyntheticFlows() {
      // This test is skipped because we do top-down modeling and don't care to find undeclared flows/scenarios by
      // investigating the sub-components of a model.
      new CreateSequenceForPubSubSystemWithSyntheticFlowsTestCase(state()).execute();
   }

   /**
    * This test verifies a sequence has a flow with an implementation which is really implemented twice.
    */
   @Test
   @Ignore("This test is about finding duplicate flows because of synthetic flows")
   public void testDoesCreateSequenceForPubSubSystemWithFlowImplementedTwice() {
      // We skip this test for the same reason as the above test.  We don't care about synthetic flows right now.
      new CreateSequenceForPubSubSystemWithDuplicateFlowImplTestCase(state()).execute();
   }

   // TODO TH: use a system of systems example.  IE, use the weapon system example.
   // test forks and splits

   private ItTestState state() {
      return new ItTestState(service, systemDescriptor, logService, options);
   }

   private static Injector startGuice() {
      Collection<Module> modules = new ArrayList<>();
      modules.add(testModule);
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      modules.add(new StepsSystemDescriptorServiceModule());
      return Guice.createInjector(modules);
   }

   private static ISystemDescriptor getSystemDescriptor(Injector injector) {
      IParsingResult result = injector
            .getInstance(ISystemDescriptorService.class)
            .parseProject(Paths.get("src/test/resources/"));
      if (!result.isSuccessful()) {
         result.getIssues().forEach(System.err::println);
         fail("failed to parse project for analysis!");
      }
      return result.getSystemDescriptor();
   }
}
