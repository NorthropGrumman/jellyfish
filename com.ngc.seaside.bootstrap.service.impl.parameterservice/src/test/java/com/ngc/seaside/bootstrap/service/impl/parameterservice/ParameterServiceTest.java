package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
   //TODO Create Unit test for parseParameters(List<String> parameters) throws ParameterServiceException {

   @Test
   public void doesParseValidParameters() throws ParameterServiceException {
      // ARRANGE

      Map<String, String> parameterInputMap = new HashMap<>();
      parameterInputMap.put("name", "value");
      parameterInputMap.put("name", "VALUE.");
      parameterInputMap.put("Name", "Value7");
      parameterInputMap.put("NAME", "VALUEx");
      parameterInputMap.put("age", "15..!");

      List<String> validParameterInputs = new LinkedList<>();

      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet())
      {
         String key = eachInputEntry.getKey();
         String val = eachInputEntry.getValue();
         validParameterInputs.add("-D" + key + "=" + val);
      }

      // ACT
      IParameterCollection parameterCollection = delegate.parseParameters(validParameterInputs);

      // ASSERT
      // assert that the parameter collection that is returned is what is expected
      for (IParameter eachParameter : parameterCollection.getAllParameters())
      {
         String key = eachParameter.getName();
         String value = eachParameter.getValue();

         // Make sure the parameter collection has the parameter at all
         assertTrue(parameterInputMap.containsKey(key));

         String mapValue = parameterInputMap.remove(key);

         // Make sure parameter has the correct value
         assertEquals(mapValue, value);
      }

      assertTrue(parameterInputMap.isEmpty());
   }

   @Test
   public void doesNotParseInvalidParameters() {
      Map<String, String> parameterInputMap = new HashMap<>();
      parameterInputMap.put("name", "value");
      parameterInputMap.put("name ", "VALUE.");
      parameterInputMap.put("Name5", "Value7");
      parameterInputMap.put("NAME!", "VALUEx");
      parameterInputMap.put("age", "15..! ");


      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet())
      {
         List<String> invalidParameterInputs = new LinkedList<>();

         String key = eachInputEntry.getKey();
         String val = eachInputEntry.getValue();

         invalidParameterInputs.add("-D" + key + "=" + val);

         try
         {
            // ACT
            delegate.parseParameters(invalidParameterInputs);

            // ASSERT
            fail("Expected a " + ParameterServiceException.class.getSimpleName());
         }
         catch(ParameterServiceException e)
         {
            // expected
         }
      }
   }

   @Test
   public void doesParseRequiredParametersWithoutException() throws ParameterServiceException {
      // Set some required parameters
      Set<String> requiredParameterInputs = new LinkedHashSet<>();
      requiredParameterInputs.add("Classname");
      requiredParameterInputs.add("groupId");
      requiredParameterInputs.add("artifactId");
      requiredParameterInputs.add("dashPackage");

      delegate.setRequiredParameters(requiredParameterInputs);

      Map<String, String> parameterInputMap = new HashMap<>();
      parameterInputMap.put("Classname", "value");
      parameterInputMap.put("grouupId", "VALUE.");
      parameterInputMap.put("dashPackage", "Value7");
      parameterInputMap.put("artifactId", "VALUEx");

      List<String> validParameterInputs = new LinkedList<>();

      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet())
      {
         String key = eachInputEntry.getKey();
         String val = eachInputEntry.getValue();
         validParameterInputs.add("-D" + key + "=" + val);
      }

      IParameterCollection parameterCollection = delegate.parseParameters(validParameterInputs);

      // assert that the parameter collection that is returned is what is expected
      for (IParameter eachParameter : parameterCollection.getAllParameters())
      {
         String key = eachParameter.getName();
         String value = eachParameter.getValue();

         // Make sure the parameter collection has the parameter at all
         assertTrue(parameterInputMap.containsKey(key));
         assertTrue(requiredParameterInputs.contains(key));
         String mapKey = parameterInputMap.remove(key);

         // Make sure parameter has the correct value
         //assertEquals(mapKey, value);

      }

      assertTrue(parameterInputMap.isEmpty());

      // parse the parameters
   }

   @Test
   public void doesNotParseIfMissingRequiredParameters() {
      // Set some required parameters

      // create some parameters that do not match all of the required parameters

      // parse the parameters and assert that an exception is thrown
   }
}
