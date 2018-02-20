package com.ngc.example.correlation.pbs.tests.steps;

import com.google.inject.Inject;
import com.ngc.seaside.service.transport.api.ITransportService;
import org.junit.Assert;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class PeanutButterServiceSteps {

   private ITransportService transportService;

   @Before
   public void setup() {
   }

   @After
   public void cleanup() {
   }
   
   @Inject
   public void setTransportService(ITransportService transportService) {
      this.transportService = transportService;
   }
   
}
