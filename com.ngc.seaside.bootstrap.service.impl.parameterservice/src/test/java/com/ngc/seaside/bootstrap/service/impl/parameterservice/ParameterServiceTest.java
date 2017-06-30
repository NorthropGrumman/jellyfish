package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.parameter.api.ParameterServiceException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameter;
import com.ngc.seaside.command.api.IParameterCollection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 */
public class ParameterServiceTest {

   private ParameterService delegate;
   private ILogService logService;

   @Before
   public void setup() {
      logService = new PrintStreamLogService();

      delegate = new ParameterService();
      delegate.setLogService(logService);
      delegate.activate();
   }

   @After
   public void after() {
      delegate.deactivate();
      delegate.removeLogService(logService);
   }

   @Test
   public void doesParseValidParameters() {
      List<String> validParameters = Arrays.asList(
            "-Dkey1=value1", "-Dkey2=value2", "-Dkey3=value3");

      IParameterCollection collection = delegate.parseParameters(validParameters);

      assertEquals(3, collection.getAllParameters().size());

      assertTrue(collection.containsParameter("key1"));
      assertTrue(collection.containsParameter("key2"));
      assertTrue(collection.containsParameter("key3"));

      assertEquals("value1", collection.getParameter("key1").getValue());
      assertEquals("value2", collection.getParameter("key2").getValue());
      assertEquals("value3", collection.getParameter("key3").getValue());
   }

   @Test
   public void doesParseMap() {
      Map<String, String> parameters = new LinkedHashMap<>();
      parameters.put("key1", "value1");
      parameters.put("key2", "value2");
      parameters.put("key3", "value3");

      IParameterCollection collection = delegate.parseParameters(parameters);

      assertTrue(collection.containsParameter("key1"));
      assertTrue(collection.containsParameter("key2"));
      assertTrue(collection.containsParameter("key3"));

      assertEquals("value1", collection.getParameter("key1").getValue());
      assertEquals("value2", collection.getParameter("key2").getValue());
      assertEquals("value3", collection.getParameter("key3").getValue());
   }

   @Test
   public void doesSkipInvalidParameters() {
      List<String> validParameters = Arrays.asList(
            "-Dkey1=value1", "-Dkey2:value2", "-Dkey3=value3");

      IParameterCollection collection = delegate.parseParameters(validParameters);

      assertEquals(2, collection.getAllParameters().size());

      assertTrue(collection.containsParameter("key1"));
      assertFalse(collection.containsParameter("key2"));
      assertTrue(collection.containsParameter("key3"));

      assertEquals("value1", collection.getParameter("key1").getValue());
      assertEquals("value3", collection.getParameter("key3").getValue());
   }

   @Test
   public void testUsageSatisfied() {
      List<String> validParameters = Arrays.asList(
            "-Dkey1=value1", "-Dkey2=value2", "-Dkey3=value3");
      IParameterCollection collection = delegate.parseParameters(validParameters);

      DefaultUsage usage1 = new DefaultUsage(
            "Description",
            new DefaultParameter("key1").setRequired(true),
            new DefaultParameter("key2").setRequired(false),
            new DefaultParameter("key3").setRequired(true));

      assertTrue(delegate.isUsageSatisfied(usage1, collection));
      IParameterCollection unset = delegate.getUnsetRequiredParameters(usage1, collection);
      assertTrue(unset.isEmpty());

      List<String> missingParameters = Arrays.asList(
            "-Dkey1=value1", "-Dkey3=value3");
      IParameterCollection collection2 = delegate.parseParameters(missingParameters);
      DefaultUsage usage2 = new DefaultUsage(
            "Description",
            new DefaultParameter("key1").setRequired(true),
            new DefaultParameter("key2").setRequired(true),
            new DefaultParameter("key3").setRequired(true));

      assertFalse(delegate.isUsageSatisfied(usage2, collection2));
      IParameterCollection unset2 = delegate.getUnsetRequiredParameters(usage2, collection2);
      assertFalse(unset2.isEmpty());

      assertTrue(unset2.containsParameter("key2"));

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
//
//   @Test
//   public void doesParseWithRequiredParameters() {
//      // Set some required parameters
//      List<String> requiredParameterNames = new ArrayList<>();
//      requiredParameterNames.add("Classname");
//      requiredParameterNames.add("groupId");
//      requiredParameterNames.add("artifactId");
//      requiredParameterNames.add("dashPackage");
//
//      // Create the required parameter usage
//      List<IParameter> requiredParameters = new ArrayList<>();
//      for (String eachRequiredParameterName : requiredParameterNames)
//      {
//         requiredParameters.add(new DefaultParameter(eachRequiredParameterName, true));
//      }
//      DefaultUsage usage = new DefaultUsage("This is a test usage with some required parameters.", requiredParameters);
//
//      // Add all the required params to the input list
//      Map<String, String> parameterInputMap = new HashMap<>();
//      for (String eachRequiredParameterName : requiredParameterNames)
//      {
//         parameterInputMap.put(eachRequiredParameterName, "value");
//      }
//
//      // Add some more required params to the input list
//      parameterInputMap.put("SomeOtherName", "value");
//      parameterInputMap.put("AnotherName", "value");
//
//      // Create the actual parameters from the input map
//      List<String> validParameterInputs = new LinkedList<>();
//      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet())
//      {
//         String key = eachInputEntry.getKey();
//         String val = eachInputEntry.getValue();
//         validParameterInputs.add("-D" + key + "=" + val);
//      }
//
//      // ACT
//      IParameterCollection parameterCollection = delegate.parseParameters(usage, validParameterInputs);
//
//      // ASSERT
//      // assert that the parameter collection that is returned is what is expected
//      for (IParameter eachParameter : parameterCollection.getAllParameters()) {
//         String key = eachParameter.getName();
//         String value = eachParameter.getValue();
//
//         // Make sure the parameter collection has the parameter at all
//         assertTrue(parameterInputMap.containsKey(key));
//
//         String mapValue = parameterInputMap.remove(key);
//
//         // Make sure parameter has the correct value
//         assertEquals(mapValue, value);
//      }
//
//      assertTrue(parameterInputMap.isEmpty());
//
//   }
//
//   @Test
//   public void doesNotParseIfMissingRequiredParameters() {
//      // Create some required parameters
//      List<IParameter> requiredParameters = new ArrayList<>();
//      requiredParameters.add(new DefaultParameter("Classname", true));
//      requiredParameters.add(new DefaultParameter("groupId", true));
//      requiredParameters.add(new DefaultParameter("artifactId", true));
//      requiredParameters.add(new DefaultParameter("dashPackage", true));
//
//      // Create the required usage
//      DefaultUsage usage = new DefaultUsage("This is a test usage with some required parameters.", requiredParameters);
//
//      // Add some non-required params to the input list
//      Map<String, String> parameterInputMap = new HashMap<>();
//      parameterInputMap.put("SomeOtherName", "value");
//      parameterInputMap.put("AnotherName", "value");
//
//      // Create the actual parameters from the input map
//      List<String> parameterInputsMissingRequiredParameters = new LinkedList<>();
//      for (Map.Entry<String, String> eachInputEntry : parameterInputMap.entrySet())
//      {
//         String key = eachInputEntry.getKey();
//         String val = eachInputEntry.getValue();
//         parameterInputsMissingRequiredParameters.add("-D" + key + "=" + val);
//      }
//
//      // parse the parameters and assert that an exception is thrown
//      try {
//         // ACT
//         delegate.parseParameters(usage, parameterInputsMissingRequiredParameters);
//
//         // ASSERT
//         fail("Expected a " + ParameterServiceException.class.getSimpleName());
//      } catch (ParameterServiceException e) {
//         // expected
//      }
//   }
}
