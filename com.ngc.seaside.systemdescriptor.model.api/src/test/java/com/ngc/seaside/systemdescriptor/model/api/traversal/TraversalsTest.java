package com.ngc.seaside.systemdescriptor.model.api.traversal;

import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TraversalsTest {

   @Mock
   private IVisitor visitor;

   @Mock
   private ISystemDescriptor descriptor;

   @Mock
   private IPackage p;

   @Mock
   private IData data;

   @Mock
   private IDataField dataField;

   @Mock
   private IModel model1;

   @Mock
   private IModel model2;

   @Mock
   private IDataReferenceField input;

   @Mock
   private IDataReferenceField output;

   @Mock
   private IModelReferenceField requirement;

   @Mock
   private IModelReferenceField part;

   @Mock
   private IScenario scenario;

   @Mock
   private IModelLink<IModelReferenceField> link;

   @Mock
   private IEnumeration enumeration;

   @Before
   public void setup() throws Throwable {
      when(descriptor.getPackages()).thenReturn(asCollection(p));
      when(p.getData()).thenReturn(asCollection(data));
      when(p.getModels()).thenReturn(asCollection(model1, model2));
      when(p.getEnumerations()).thenReturn(asCollection(enumeration));
      when(data.getFields()).thenReturn(asCollection(dataField));

      when(model1.getInputs()).thenReturn(asCollection(input));
      when(model1.getOutputs()).thenReturn(asCollection(output));
      when(model1.getRequiredModels()).thenReturn(asCollection(requirement));
      when(model1.getParts()).thenReturn(asCollection(part));
      when(model1.getScenarios()).thenReturn(asCollection(scenario));
      when(model1.getLinks()).thenReturn(Collections.singletonList(link));

      when(model2.getInputs()).thenReturn(asCollection());
      when(model2.getOutputs()).thenReturn(asCollection());
      when(model2.getRequiredModels()).thenReturn(asCollection());
      when(model2.getParts()).thenReturn(asCollection());
      when(model2.getScenarios()).thenReturn(asCollection());
      when(model2.getLinks()).thenReturn(Collections.emptyList());
   }

   @Test
   public void testDoesVisitAllPartsOfModel() throws Throwable {
      assertFalse("should not return a result!",
                  Traversals.traverse(descriptor, visitor).isPresent());

      verify(visitor).visitSystemDescriptor(any(), eq(descriptor));
      verify(visitor).visitPackage(any(), eq(p));
      verify(visitor).visitData(any(), eq(data));
      verify(visitor).visitDataField(any(), eq(dataField));
      verify(visitor).visitModel(any(), eq(model1));
      verify(visitor).visitDataReferenceFieldAsInput(any(), eq(input));
      verify(visitor).visitDataReferenceFieldAsOutput(any(), eq(output));
      verify(visitor).visitModelReferenceFieldAsRequirement(any(), eq(requirement));
      verify(visitor).visitModelReferenceFieldAsPart(any(), eq(part));
      verify(visitor).visitScenario(any(), eq(scenario));
      verify(visitor).visitLink(any(), eq(link));
      verify(visitor).visitEnumeration(any(), eq(enumeration));
   }

   @Test
   public void testDoesReturnResult() throws Throwable {
      doAnswer(invocation -> {
         IVisitorContext ctx = (IVisitorContext) invocation.getArgument(0);
         ctx.setResult("hello world");
         return null;
      }).when(visitor).visitSystemDescriptor(any(), eq(descriptor));

      assertEquals("did not return correct result!",
                   "hello world",
                   Traversals.traverse(descriptor, visitor).get());
   }

   @Test
   public void testDoesAbortTraversal() throws Throwable {
      doAnswer(invocation -> {
         IVisitorContext ctx = invocation.getArgument(0);
         ctx.stop();
         return null;
      }).when(visitor).visitSystemDescriptor(any(), eq(descriptor));

      Traversals.traverse(descriptor, visitor);

      verify(visitor, never()).visitPackage(any(), eq(p));
   }

   @Test
   public void testDoesCollectModels() throws Throwable {
      Collection<IModel> models = Traversals.collectModels(descriptor, m -> m.equals(model1));
      assertTrue("results did not include accepted model!",
                 models.contains(model1));
      assertEquals("contains models that the predicate did not accept!",
                   1,
                   models.size());
   }

   private static <P, T extends INamedChild<P>> INamedChildCollection<P, T> asCollection(T... children) {
      MockedCollection<P, T> collection = new MockedCollection<>();
      collection.addAll(Arrays.asList(children));
      return collection;
   }

   private static class MockedCollection<P, T extends INamedChild<P>>
         extends ArrayList<T>
         implements INamedChildCollection<P, T> {

      @Override
      public Optional<T> getByName(String name) {
         // This is not needed for this test.
         throw new UnsupportedOperationException("not implemented");
      }
   }
}
