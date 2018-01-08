package com.ngc.seaside.systemdescriptor.model.api.traversal;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.function.Predicate;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ModelPredicatesTest {

   @Mock
   private IModel model1;

   @Mock
   private IModel model2;

   @Mock
   private IMetadata metadata1;

   @Mock
   private IMetadata metadata2;

   @Before
   public void setup() throws Throwable {
      when(model1.getMetadata()).thenReturn(metadata1);
      when(model2.getMetadata()).thenReturn(metadata2);
   }

   @Test
   public void testDoesFilterByAnyStereotype() throws Throwable {
      JsonValue value1 = Json.createValue("system");
      JsonValue value2 = Json.createValue("service");

      JsonObject json1 = Json.createObjectBuilder()
            .add(ModelPredicates.STEREOTYPE_MEMBER_NAME, value1)
            .build();
      JsonObject json2 = Json.createObjectBuilder()
            .add(ModelPredicates.STEREOTYPE_MEMBER_NAME, value2)
            .build();
      when(metadata1.getJson()).thenReturn(json1);
      when(metadata2.getJson()).thenReturn(json2);

      Predicate<IModel> predicate = ModelPredicates.withAnyStereotype("system", "foo");
      assertTrue("predicate should accept model with stereotype!",
                 predicate.test(model1));
      assertFalse("predicate should not accept model with stereotype!",
                  predicate.test(model2));
   }

   @Test
   public void testDoesFilterByAnyStereotypeWithArrays() throws Throwable {
      JsonValue value1 = Json.createArrayBuilder()
            .add("system")
            .add("blah")
            .build();
      JsonValue value2 = Json.createValue("service");

      JsonObject json1 = Json.createObjectBuilder()
            .add(ModelPredicates.STEREOTYPE_MEMBER_NAME, value1)
            .build();
      JsonObject json2 = Json.createObjectBuilder()
            .add(ModelPredicates.STEREOTYPE_MEMBER_NAME, value2)
            .build();
      when(metadata1.getJson()).thenReturn(json1);
      when(metadata2.getJson()).thenReturn(json2);

      Predicate<IModel> predicate = ModelPredicates.withAnyStereotype("system", "foo");
      assertTrue("predicate should accept model with stereotype!",
                 predicate.test(model1));
      assertFalse("predicate should not accept model with stereotype!",
                  predicate.test(model2));
   }

   @Test
   public void testDoesFilterByAllStereotypes() throws Throwable {
      JsonValue value1 = Json.createArrayBuilder()
            .add("system")
            .add("foo")
            .build();
      JsonValue value2 = Json.createValue("system");

      JsonObject json1 = Json.createObjectBuilder()
            .add(ModelPredicates.STEREOTYPE_MEMBER_NAME, value1)
            .build();
      JsonObject json2 = Json.createObjectBuilder()
            .add(ModelPredicates.STEREOTYPE_MEMBER_NAME, value2)
            .build();
      when(metadata1.getJson()).thenReturn(json1);
      when(metadata2.getJson()).thenReturn(json2);

      Predicate<IModel> predicate = ModelPredicates.withAllStereotypes("system", "foo");
      assertTrue("predicate should accept model with stereotype!",
                 predicate.test(model1));
      assertFalse("predicate should not accept model with stereotype!",
                  predicate.test(model2));
   }

}
