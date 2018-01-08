package com.ngc.seaside.jellyfish.service.requirements.impl.requirementsservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

   private static abstract class JsonArrayImpl extends AbstractList<JsonValue> implements JsonArray {
   }

}
