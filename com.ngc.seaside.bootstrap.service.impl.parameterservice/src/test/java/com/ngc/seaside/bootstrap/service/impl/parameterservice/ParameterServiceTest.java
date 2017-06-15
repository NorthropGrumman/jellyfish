package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
   public void doesParseValidParameters() throws ParameterServiceException {
      Map<String, String> parameterInputMap = new HashMap<>();
      parameterInputMap.put("name", "value");
      parameterInputMap.put("name", "VALUE.");
      parameterInputMap.put("Name", "Value7");
      parameterInputMap.put("NAME", "VALUEx");
      parameterInputMap.put("age", "15..!");

      List<String> validParameterInputs = new LinkedList<>();

      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet()) {
         String key = eachInputEntry.getKey();
         String val = eachInputEntry.getValue();
         DefaultParameter param = new DefaultParameter("-D" + key, false);

         validParameterInputs.add("-D" + key + "=" + val);
      }

      DefaultUsage usage = new DefaultUsage("This is a test usage with no required parameters.");

      // ACT
      IParameterCollection parameterCollection = delegate.parseParameters(usage, validParameterInputs);

      // ASSERT
      // assert that the parameter collection that is returned is what is expected
      for (IParameter eachParameter : parameterCollection.getAllParameters()) {
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

//   @Test
//   public void doesNotParseInvalidParameters() {
//      Map<String, String> parameterInputMap = new HashMap<>();
//      // invalid names
//      parameterInputMap.put("n ame", "value");
//      parameterInputMap.put("name ", "value");
//      parameterInputMap.put("Name5", "value");
//      parameterInputMap.put("NAME!", "value");
//      // invalid values
//      parameterInputMap.put("name", "15..! ");
//
//
//      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet())
//      {
//         List<String> invalidParameterInputs = new LinkedList<>();
//
//         String key = eachInputEntry.getKey();
//         String val = eachInputEntry.getValue();
//
//         invalidParameterInputs.add("-D" + key + "=" + val);
//
//         try
//         {
//            // ACT
//            DefaultUsage usage = new DefaultUsage("This is a test usage with no required parameters.");
//            delegate.parseParameters(usage, invalidParameterInputs);
//
//            // ASSERT
//            fail("Expected a " + ParameterServiceException.class.getSimpleName());
//         }
//         catch(ParameterServiceException e)
//         {
//            // expected
//         }
//      }
//   }

   @Test
   public void doesParseWithRequiredParameters() {
      // Set some required parameters
      List<String> requiredParameterNames = new ArrayList<>();
      requiredParameterNames.add("Classname");
      requiredParameterNames.add("groupId");
      requiredParameterNames.add("artifactId");
      requiredParameterNames.add("dashPackage");

      // Create the required parameter usage
      List<IParameter> requiredParameters = new ArrayList<>();
      for (String eachRequiredParameterName : requiredParameterNames)
      {
         requiredParameters.add(new DefaultParameter(eachRequiredParameterName, true));
      }
      DefaultUsage usage = new DefaultUsage("This is a test usage with some required parameters.", requiredParameters);

      // Add all the required params to the input list
      Map<String, String> parameterInputMap = new HashMap<>();
      for (String eachRequiredParameterName : requiredParameterNames)
      {
         parameterInputMap.put(eachRequiredParameterName, "value");
      }

      // Add some more required params to the input list
      parameterInputMap.put("SomeOtherName", "value");
      parameterInputMap.put("AnotherName", "value");

      // Create the actual parameters from the input map
      List<String> validParameterInputs = new LinkedList<>();
      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet())
      {
         String key = eachInputEntry.getKey();
         String val = eachInputEntry.getValue();
         validParameterInputs.add("-D" + key + "=" + val);
      }

      // ACT
      IParameterCollection parameterCollection = delegate.parseParameters(usage, validParameterInputs);

      // ASSERT
      // assert that the parameter collection that is returned is what is expected
      for (IParameter eachParameter : parameterCollection.getAllParameters()) {
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
   public void doesNotParseIfMissingRequiredParameters() {
      // Create some required parameters
      List<IParameter> requiredParameters = new ArrayList<>();
      requiredParameters.add(new DefaultParameter("Classname", true));
      requiredParameters.add(new DefaultParameter("groupId", true));
      requiredParameters.add(new DefaultParameter("artifactId", true));
      requiredParameters.add(new DefaultParameter("dashPackage", true));

      // Create the required usage
      DefaultUsage usage = new DefaultUsage("This is a test usage with some required parameters.", requiredParameters);

      // Add some non-required params to the input list
      Map<String, String> parameterInputMap = new HashMap<>();
      parameterInputMap.put("SomeOtherName", "value");
      parameterInputMap.put("AnotherName", "value");

      // Create the actual parameters from the input map
      List<String> parameterInputsMissingRequiredParameters = new LinkedList<>();
      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet())
      {
         String key = eachInputEntry.getKey();
         String val = eachInputEntry.getValue();
         parameterInputsMissingRequiredParameters.add("-D" + key + "=" + val);
      }

      // parse the parameters and assert that an exception is thrown
      try {
         // ACT
         delegate.parseParameters(usage, parameterInputsMissingRequiredParameters);

         // ASSERT
         fail("Expected a " + ParameterServiceException.class.getSimpleName());
      } catch (ParameterServiceException e) {
         // expected
      }
   }
}
