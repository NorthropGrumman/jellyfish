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
package com.ngc.seaside.jellyfish.service.requirements.impl.requirementsservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.service.log.api.PrintStreamLogService;

public class RequirementsServiceTest {

   private RequirementsService service;

   @Mock
   private IJellyFishCommandOptions options;

   @Before
   public void setup() {
      service = new RequirementsService();
      service.setLogService(new PrintStreamLogService());
   }

   @Test
   public void testMetaDataRequirements() {
      IMetadata metadata = getMockMetadata();
      assertEquals(Collections.emptySet(), service.getRequirements(options, metadata));

      metadata = getMockMetadata("TE001");
      assertEquals(Collections.singleton("TE001"), service.getRequirements(options, metadata));

      metadata = getMockMetadata("TE001", "TE002", "TE003");
      assertEquals(new HashSet<>(Arrays.asList("TE001", "TE002", "TE003")), service.getRequirements(options, metadata));
   }

   private static IMetadata getMockMetadata(String... requirements) {
      IMetadata metadata = mock(IMetadata.class);
      when(metadata.getJson()).thenReturn(mock(JsonObject.class));
      final JsonValue result;
      if (requirements.length == 0) {
         result = null;
      } else if (requirements.length == 1) {
         result = mockString(requirements[0]);
      } else {
         JsonArray array = mock(JsonArrayImpl.class, CALLS_REAL_METHODS);
         when(array.size()).thenReturn(requirements.length);
         when(array.get(anyInt())).thenAnswer(args -> mockString(requirements[(Integer) args.getArgument(0)]));
         result = array;
      }
      when(metadata.getJson().get(RequirementsService.REQUIREMENTS_KEY)).thenReturn(result);
      return metadata;
   }

   private static JsonString mockString(String value) {
      JsonString string = mock(JsonString.class);
      when(string.getString()).thenReturn(value);
      return string;
   }

   private abstract static class JsonArrayImpl extends AbstractList<JsonValue> implements JsonArray {

   }

}
