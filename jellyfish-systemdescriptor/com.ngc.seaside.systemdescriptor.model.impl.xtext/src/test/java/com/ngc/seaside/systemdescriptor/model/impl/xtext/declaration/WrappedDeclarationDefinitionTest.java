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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DeclarationDefinition;
import com.ngc.seaside.systemdescriptor.systemDescriptor.JsonObject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Member;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.StringValue;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Value;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class WrappedDeclarationDefinitionTest extends AbstractWrappedXtextTest {

   private DeclarationDefinition definition;

   @Before
   public void setup() throws Throwable {
      JsonObject root = factory().createJsonObject();
      root.getMembers().add(newMember("hello", newStringValue("world")));

      Metadata metadata = factory().createMetadata();
      metadata.setJson(root);

      definition = factory().createDeclarationDefinition();
      definition.setMetadata(metadata);
   }

   @Test
   public void testDoesConvertMetadataFromXtext() {
      IMetadata metadata = WrappedDeclarationDefinition.metadataFromXtext(definition);
      assertNotNull("metadata is null!",
                    metadata);
      assertNotSame("returned empty metadata!",
                    IMetadata.EMPTY_METADATA,
                    metadata);
      assertEquals("missing metadata fields!",
                   1,
                   metadata.getJson().size());

      metadata = WrappedDeclarationDefinition.metadataFromXtext(null);
      assertSame("a null definition should result in an empty metadata object!",
                 IMetadata.EMPTY_METADATA,
                 metadata);

      definition.setMetadata(null);
      metadata = WrappedDeclarationDefinition.metadataFromXtext(definition);
      assertSame("null metadata should result in an empty metadata object!",
                 IMetadata.EMPTY_METADATA,
                 metadata);
   }

   @Test
   public void testDoesConvertMetadataToXtext() {
      IMetadata metadata = newMetadata("hello", "world");
      DeclarationDefinition to = WrappedDeclarationDefinition.toXtext(resolver(), metadata, null);
      assertNotNull("did not convert to XText!",
                    to);
      assertEquals("did not convert metadata correctly!",
                   1,
                   to.getMetadata().getJson().getMembers().size());

      to = WrappedDeclarationDefinition.toXtext(resolver(), metadata, null);
      assertNotNull("did not convert to XText!",
                    to);
      assertEquals("did not convert metadata correctly!",
                   1,
                   to.getMetadata().getJson().getMembers().size());

      to = WrappedDeclarationDefinition.toXtext(resolver(), null, null);
      assertNull("null metadata should result in a null definition!",
                 to);
   }

   private static Member newMember(String key, Value value) {
      Member m = factory().createMember();
      m.setKey(key);
      m.setValue(value);
      return m;
   }

   private static StringValue newStringValue(String x) {
      StringValue value = factory().createStringValue();
      value.setValue(x);
      return value;
   }
}
