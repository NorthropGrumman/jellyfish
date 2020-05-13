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
package com.ngc.seaside.jellyfish.service.impl.parameterservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.ParameterCategory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
            "-Dkey1=value1", "-Dkey2=value2", "-Dkey3=value3", "-Dkey4=value4,and,5", "dashD=isOptional");

      IParameterCollection collection = delegate.parseParameters(validParameters);
      assertEquals(5, collection.getAllParameters().size());

      assertTrue(collection.containsParameter("key1"));
      assertTrue(collection.containsParameter("key2"));
      assertTrue(collection.containsParameter("key3"));
      assertTrue(collection.containsParameter("key4"));
      assertTrue(collection.containsParameter("dashD"));

      assertEquals("value1", collection.getParameter("key1").getValue());
      assertEquals("value2", collection.getParameter("key2").getValue());
      assertEquals("value3", collection.getParameter("key3").getValue());
      assertEquals("value4,and,5", collection.getParameter("key4").getValue());
      assertEquals("isOptional", collection.getParameter("dashD").getValue());
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
            new DefaultParameter<>("key1").setParameterCategory(ParameterCategory.REQUIRED),
            new DefaultParameter<>("key2").setParameterCategory(ParameterCategory.ADVANCED),
            new DefaultParameter<>("key3").setParameterCategory(ParameterCategory.REQUIRED));

      assertTrue(delegate.isUsageSatisfied(usage1, collection));
      IParameterCollection unset = delegate.getUnsetRequiredParameters(usage1, collection);
      assertTrue(unset.isEmpty());

      List<String> missingParameters = Arrays.asList(
            "-Dkey1=value1", "-Dkey3=value3");
      IParameterCollection collection2 = delegate.parseParameters(missingParameters);
      DefaultUsage usage2 = new DefaultUsage(
            "Description",
            new DefaultParameter<>("key1").setParameterCategory(ParameterCategory.REQUIRED),
            new DefaultParameter<>("key2").setParameterCategory(ParameterCategory.REQUIRED),
            new DefaultParameter<>("key3").setParameterCategory(ParameterCategory.REQUIRED));

      assertFalse(delegate.isUsageSatisfied(usage2, collection2));
      IParameterCollection unset2 = delegate.getUnsetRequiredParameters(usage2, collection2);
      assertFalse(unset2.isEmpty());

      assertTrue(unset2.containsParameter("key2"));

   }

}
