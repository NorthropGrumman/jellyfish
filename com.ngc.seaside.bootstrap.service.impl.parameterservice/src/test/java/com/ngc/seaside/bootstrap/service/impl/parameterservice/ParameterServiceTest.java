package com.ngc.seaside.bootstrap.service.impl.parameterservice;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IParameterCollection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
            "-Dkey1=value1", "-Dkey2=value2", "-Dkey3=value3", "-Dkey4=value4,and,5");

      IParameterCollection collection = delegate.parseParameters(validParameters);

      assertEquals(4, collection.getAllParameters().size());

      assertTrue(collection.containsParameter("key1"));
      assertTrue(collection.containsParameter("key2"));
      assertTrue(collection.containsParameter("key3"));
      assertTrue(collection.containsParameter("key4"));

      assertEquals("value1", collection.getParameter("key1").getValue());
      assertEquals("value2", collection.getParameter("key2").getValue());
      assertEquals("value3", collection.getParameter("key3").getValue());
      assertEquals("value4,and,5", collection.getParameter("key4").getValue());
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
            new DefaultParameter<>("key1").setRequired(true),
            new DefaultParameter<>("key2").setRequired(false),
            new DefaultParameter<>("key3").setRequired(true));

      assertTrue(delegate.isUsageSatisfied(usage1, collection));
      IParameterCollection unset = delegate.getUnsetRequiredParameters(usage1, collection);
      assertTrue(unset.isEmpty());

      List<String> missingParameters = Arrays.asList(
            "-Dkey1=value1", "-Dkey3=value3");
      IParameterCollection collection2 = delegate.parseParameters(missingParameters);
      DefaultUsage usage2 = new DefaultUsage(
            "Description",
            new DefaultParameter<>("key1").setRequired(true),
            new DefaultParameter<>("key2").setRequired(true),
            new DefaultParameter<>("key3").setRequired(true));

      assertFalse(delegate.isUsageSatisfied(usage2, collection2));
      IParameterCollection unset2 = delegate.getUnsetRequiredParameters(usage2, collection2);
      assertFalse(unset2.isEmpty());

      assertTrue(unset2.containsParameter("key2"));

   }

}
