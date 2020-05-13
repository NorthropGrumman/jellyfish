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
