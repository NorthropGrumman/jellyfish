package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Created by J55690 on 6/7/2017.
 */
public class ParameterServiceTest {

   private ParameterService delegate;

   @Before
   public void setup() {
      delegate = new ParameterService();
   }

   @Test
   public void doesSetRequiredParameters() {
      Set<String> requiredParameterInputs = new LinkedHashSet<>();
      requiredParameterInputs.add("Classname");
      requiredParameterInputs.add("groupId");
      requiredParameterInputs.add("artifactId");
      requiredParameterInputs.add("dashPackage");

      delegate.setRequiredParameters(requiredParameterInputs);
      Set<String> requiredParameters = delegate.getRequiredParameters();

      assertTrue(requiredParameters.containsAll(requiredParameterInputs));
      assertTrue(requiredParameterInputs.containsAll(requiredParameters));
   }

   @Test
   public void doesParseValidParameters() {
      assertTrue(false);
   }

   @Test
   public void doesNotParseInvalidParameters() {
      assertTrue(false);
   }

   @Test
   public void doesParseRequiredParameters() {
      assertTrue(false);
   }

   @Test
   public void doesNotParseIfMissingRequiredParameters() {
      assertTrue(false);
   }
}
