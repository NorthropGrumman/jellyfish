/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.service.requirements.impl.requirementsservice;

import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
