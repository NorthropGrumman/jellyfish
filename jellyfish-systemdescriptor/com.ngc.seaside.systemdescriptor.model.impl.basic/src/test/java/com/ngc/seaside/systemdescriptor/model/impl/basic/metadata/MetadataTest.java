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
package com.ngc.seaside.systemdescriptor.model.impl.basic.metadata;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;

import org.junit.Test;

import javax.json.Json;

import static com.ngc.seaside.systemdescriptor.model.impl.basic.TestUtils.demandImmutability;

public class MetadataTest {

   @Test
   public void testDoesMakeImmutable() throws Throwable {
      Metadata data = new Metadata();
      data.setJson(Json.createObjectBuilder().add("foo", "bar").build());

      IMetadata immutable = Metadata.immutable(data);
      demandImmutability(() -> immutable.setJson(Json.createObjectBuilder()
                                                       .add("foo", "bar").build()));
   }
}
