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
package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import javax.json.JsonObject;
import javax.json.spi.JsonProvider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AggregatedMetadataViewTest {

   private IMetadata view;

   private JsonProvider jsonProvider;

   @Mock
   private IData data;

   @Mock
   private IModel model;

   @Before
   public void setup() {
      jsonProvider = JsonProvider.provider();
      setupForData();
      setupForModel();
   }

   private void setupForData() {
      IData parent = mock(IData.class);
      IData grandparent = mock(IData.class);

      JsonObject dataJson = jsonProvider.createObjectBuilder()
            .add("data", "value1")
            .add("hello", "from data")
            .build();
      JsonObject parentJson = jsonProvider.createObjectBuilder()
            .add("parent", "value2")
            .add("hello", "from parent")
            .add("shared", "parentValue")
            .build();
      JsonObject grandparentJson = jsonProvider.createObjectBuilder()
            .add("grandparent", "value3")
            .add("shared", "grandparentValue")
            .build();

      when(data.getMetadata()).thenReturn(metadata(dataJson));
      when(data.getExtendedDataType()).thenReturn(Optional.of(parent));
      when(parent.getMetadata()).thenReturn(metadata(parentJson));
      when(parent.getExtendedDataType()).thenReturn(Optional.of(grandparent));
      when(grandparent.getMetadata()).thenReturn(metadata(grandparentJson));
      when(grandparent.getExtendedDataType()).thenReturn(Optional.empty());
   }

   private void setupForModel() {
      IModel parent = mock(IModel.class);
      IModel grandparent = mock(IModel.class);

      JsonObject modelJson = jsonProvider.createObjectBuilder()
            .add("model", "value1")
            .add("hello", "from model")
            .build();
      JsonObject parentJson = jsonProvider.createObjectBuilder()
            .add("parent", "value2")
            .add("hello", "from parent")
            .add("shared", "parentValue")
            .build();
      JsonObject grandparentJson = jsonProvider.createObjectBuilder()
            .add("grandparent", "value3")
            .add("shared", "grandparentValue")
            .build();

      when(model.getMetadata()).thenReturn(metadata(modelJson));
      when(model.getRefinedModel()).thenReturn(Optional.of(parent));
      when(parent.getMetadata()).thenReturn(metadata(parentJson));
      when(parent.getRefinedModel()).thenReturn(Optional.of(grandparent));
      when(grandparent.getMetadata()).thenReturn(metadata(grandparentJson));
      when(grandparent.getRefinedModel()).thenReturn(Optional.empty());
   }

   @Test
   public void testDoesAggregateMetadataForData() {
      view = AggregatedMetadataView.getAggregatedMetadata(data);

      assertEquals("missing metadata from data object!",
                   "value1",
                   view.getJson().getString("data"));
      assertEquals("missing metadata from parent object!",
                   "value2",
                   view.getJson().getString("parent"));
      assertEquals("missing metadata from grandparent object!",
                   "value3",
                   view.getJson().getString("grandparent"));

      assertEquals("did not overwrite metadata from parent object!",
                   "from data",
                   view.getJson().getString("hello"));
      assertEquals("did not overwrite metadata from grandparent object!",
                   "parentValue",
                   view.getJson().getString("shared"));
   }

   @Test
   public void testDoesAggregateMetadataForModel() {
      view = AggregatedMetadataView.getAggregatedMetadata(model);

      assertEquals("missing metadata from model object!",
                   "value1",
                   view.getJson().getString("model"));
      assertEquals("missing metadata from parent object!",
                   "value2",
                   view.getJson().getString("parent"));
      assertEquals("missing metadata from grandparent object!",
                   "value3",
                   view.getJson().getString("grandparent"));

      assertEquals("did not overwrite metadata from parent object!",
                   "from model",
                   view.getJson().getString("hello"));
      assertEquals("did not overwrite metadata from grandparent object!",
                   "parentValue",
                   view.getJson().getString("shared"));
   }

   private static IMetadata metadata(JsonObject json) {
      return new Metadata().setJson(json);
   }
}
