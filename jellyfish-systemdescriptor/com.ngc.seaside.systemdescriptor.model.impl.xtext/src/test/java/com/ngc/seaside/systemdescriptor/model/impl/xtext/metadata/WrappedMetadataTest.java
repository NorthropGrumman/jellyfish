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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Array;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ArrayValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BooleanValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DblValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.IntValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.NullValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata.newArrayValue;
import static com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata.newBooleanValue;
import static com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata.newDblValue;
import static com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata.newIntValue;
import static com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata.newJsonValue;
import static com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata.newMember;
import static com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata.newNullValue;
import static com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata.newStringValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WrappedMetadataTest extends AbstractWrappedXtextTest {

   private IMetadata wrapped;

   private Metadata metadata;

   @Before
   public void setup() throws Throwable {
      JsonObject root = factory().createJsonObject();
      root.getMembers().add(newMember("hello", newStringValue("world")));
      root.getMembers().add(newMember("x", newIntValue(1)));
      root.getMembers().add(newMember("y", newDblValue(1.5)));
      root.getMembers().add(newMember("z", newNullValue()));
      root.getMembers().add(newMember("a", newArrayValue(Arrays.asList(newStringValue("a1"),
                                                                       newStringValue("a2")))));

      JsonObject nested = factory().createJsonObject();
      nested.getMembers().add(newMember("once", newStringValue("again")));
      root.getMembers().add(newMember("nested", newJsonValue(nested)));

      root.getMembers().add(newMember("b", newBooleanValue(true)));
      root.getMembers().add(newMember("c", newBooleanValue(false)));

      metadata = factory().createMetadata();
      metadata.setJson(root);
   }

   @Test
   public void testDoesWrapMetadata() throws Throwable {
      wrapped = WrappedMetadata.fromXtext(metadata);

      assertEquals("world",
                   wrapped.getJson().getJsonString("hello").getString());
      assertEquals(1,
                   wrapped.getJson().getJsonNumber("x").intValue());
      assertEquals(Double.MIN_VALUE,
                   1.5,
                   wrapped.getJson().getJsonNumber("y").doubleValue());
      assertEquals(javax.json.JsonValue.NULL,
                   wrapped.getJson().getValue("/z"));

      assertEquals("a1",
                   wrapped.getJson().getJsonArray("a").getString(0));
      assertEquals("a2",
                   wrapped.getJson().getJsonArray("a").getString(1));

      assertEquals("again",
                   wrapped.getJson().getJsonObject("nested").getJsonString("once").getString());

      assertTrue("boolean value not correct!",
                 wrapped.getJson().getBoolean("b"));
      assertFalse("boolean value not correct!",
                  wrapped.getJson().getBoolean("c"));
   }

   @Test
   public void testDoesUnwrapMetadata() throws Throwable {
      wrapped = WrappedMetadata.fromXtext(metadata);
      Metadata andBackAgain = WrappedMetadata.toXtext(wrapped);

      assertEquals("hello",
                   andBackAgain.getJson().getMembers().get(0).getKey());
      assertEquals("world",
                   ((StringValue) andBackAgain.getJson().getMembers().get(0).getValue()).getValue());

      assertEquals("x",
                   andBackAgain.getJson().getMembers().get(1).getKey());
      assertEquals(1,
                   ((IntValue) andBackAgain.getJson().getMembers().get(1).getValue()).getValue());

      assertEquals("y",
                   andBackAgain.getJson().getMembers().get(2).getKey());
      assertEquals(Double.MIN_VALUE,
                   1.5,
                   ((DblValue) andBackAgain.getJson().getMembers().get(2).getValue()).getValue());

      assertEquals("z",
                   andBackAgain.getJson().getMembers().get(3).getKey());
      assertTrue(andBackAgain.getJson().getMembers().get(3).getValue() instanceof NullValue);

      assertEquals("a",
                   andBackAgain.getJson().getMembers().get(4).getKey());
      Array array = ((ArrayValue) andBackAgain.getJson().getMembers().get(4).getValue()).getValue();
      assertEquals("a1",
                   ((StringValue) array.getValues().get(0)).getValue());
      assertEquals("a2",
                   ((StringValue) array.getValues().get(1)).getValue());

      assertEquals("nested",
                   andBackAgain.getJson().getMembers().get(5).getKey());
      JsonObject object = ((JsonValue) andBackAgain.getJson().getMembers().get(5).getValue()).getValue();
      assertEquals("once",
                   object.getMembers().get(0).getKey());
      assertEquals("again",
                   ((StringValue) object.getMembers().get(0).getValue()).getValue());

      assertEquals(Boolean.TRUE.toString(),
                   ((BooleanValue) andBackAgain.getJson().getMembers().get(6).getValue()).getValue());
      assertEquals(Boolean.FALSE.toString(),
                   ((BooleanValue) andBackAgain.getJson().getMembers().get(7).getValue()).getValue());
   }
}
