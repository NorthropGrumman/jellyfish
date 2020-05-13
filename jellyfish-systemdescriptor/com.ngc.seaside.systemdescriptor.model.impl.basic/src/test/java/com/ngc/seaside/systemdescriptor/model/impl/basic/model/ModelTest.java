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
package com.ngc.seaside.systemdescriptor.model.impl.basic.model;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.basic.Package;
import com.ngc.seaside.systemdescriptor.model.impl.basic.data.Data;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import org.junit.Before;
import org.junit.Test;

import javax.json.Json;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTest {

   private Data time;
   private Model timer;
   private Model gatorade;

   @Before
   public void setup() {
      time = new Data("Time");
      time.setParent(new Package("clock.datatypes"));

      timer = new Model("Timer");
      timer.setParent(new Package("clock.models"));

      gatorade = new Model("Gatorade");
      gatorade.setParent(new Package("beverages.models"));
   }

   @Test
   public void testDoesImplementIPackage() {
      IPackage pkg = mock(IPackage.class);
      when(pkg.getName()).thenReturn("com.ngc.seaside.mypackage");
      Model m = new Model("foo.bar");
      assertEquals("name not set!", "foo.bar", m.getName());

      m.setParent(pkg);
      assertEquals("fullyqualifiedname not set!", "com.ngc.seaside.mypackage.foo.bar", m.getFullyQualifiedName());

   }

   @Test
   public void testModelWithMetadata() {
      Model model = new Model("AlarmClock");

      IDataReferenceField input = new DataReferenceField("alarmTimes");
      input.setCardinality(FieldCardinality.MANY);
      input.setType(time);
      input.setMetadata(createMetadata("satisfies", "requirement1"));
      model.addInput(input);

      IDataReferenceField output = new DataReferenceField("timeOfAlarmDeactivation");
      output.setMetadata(createMetadata("since", "2.0"));
      model.addOutput(output);

      IModelReferenceField part = new BaseModelReferenceField("timer");
      part.setType(timer);
      part.setMetadata(createMetadata("description", "This is provided by 3rd party software"));
      model.addPart(part);

      IModelReferenceField requireModel = new BaseModelReferenceField("thristQuencher");
      requireModel.setType(gatorade);
      requireModel.setMetadata(createMetadata("flavor", "purple"));
      model.addRequiredModel(requireModel);

      assertEquals("requirement1",
                   model.getInputs().getByName("alarmTimes").get().getMetadata().getJson().getString("satisfies"));
      assertEquals("2.0", model.getOutputs().getByName("timeOfAlarmDeactivation").get().getMetadata().getJson()
            .getString("since"));
      assertEquals("This is provided by 3rd party software",
                   model.getParts().getByName("timer").get().getMetadata().getJson().getString("description"));
      assertEquals("purple", model.getRequiredModels().getByName("thristQuencher").get().getMetadata().getJson()
            .getString("flavor"));
   }

   private static IMetadata createMetadata(String key, String value) {
      Metadata meta = new Metadata();
      meta.setJson(Json.createObjectBuilder().add(key, value).build());
      return meta;
   }
}
