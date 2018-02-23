package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedPartDeclaration;

@RunWith(MockitoJUnitRunner.Silent.class)
public class WrappedRefinedPartModelReferenceFieldTest extends AbstractWrappedXtextTest {

   private WrappedBasePartModelReferenceField wrapped;
   private WrappedRefinedPartModelReferenceField refinedWrapped;

   private BasePartDeclaration basePartDeclaration;
   private RefinedPartDeclaration refinedPartDeclaration;

   private Model modelType;
   private Model refinedModelType;

   @Mock
   private IModel parent;
   
   @Mock
   private IModel refinedParent;
   
   @Mock
   private IModel wrappedModelType;
   
   @Mock
   private IModel refinedWrappedModelType;

   @SuppressWarnings("unchecked")
   @Before
   public void setup() throws Throwable {
      basePartDeclaration = factory().createBasePartDeclaration();
      basePartDeclaration.setName("part1");
      refinedPartDeclaration = factory().createRefinedPartDeclaration();
      refinedPartDeclaration.setName(basePartDeclaration.getName());

      modelType = factory().createModel();
      modelType.setName("MyType");
      basePartDeclaration.setType(modelType);

      refinedModelType = factory().createModel();
      refinedModelType.setName("MyType");
      refinedPartDeclaration.setRefinedField(true);

      Model model = factory().createModel();
      model.setParts(factory().createParts());
      model.getParts().getDeclarations().add(basePartDeclaration);
      
      Model refinedModel = factory().createModel();
      refinedModel.setRefinedModel(model);
      refinedModel.setParts(factory().createParts());
      refinedModel.getParts().getDeclarations().add(refinedPartDeclaration);
      
      IPackage p = mock(IPackage.class);
      when(p.getName()).thenReturn("some.package");

      when(resolver().getWrapperFor(model)).thenReturn(parent);
      when(resolver().getWrapperFor(refinedModel)).thenReturn(refinedParent);
      when(resolver().getWrapperFor(modelType)).thenReturn(wrappedModelType);
      when(resolver().findXTextModel(model.getName(), p.getName())).thenReturn(Optional.of(modelType));
      when(resolver().findXTextModel(refinedModel.getName(), p.getName())).thenReturn(Optional.of(refinedModelType));
      when(refinedParent.getRefinedModel()).thenReturn(Optional.of(parent));
      when(parent.getParts()).thenReturn(mock(INamedChildCollection.class));
      when(parent.getParts().getByName(basePartDeclaration.getName())).thenAnswer(args -> Optional.of(wrapped));
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedBasePartModelReferenceField(resolver(), basePartDeclaration);
      refinedWrapped = new WrappedRefinedPartModelReferenceField(resolver(), refinedPartDeclaration);
      assertEquals("name not correct!",
                   refinedWrapped.getName(),
                   refinedPartDeclaration.getName());
      assertEquals("parent not correct!",
                   refinedParent,
                   refinedWrapped.getParent());
      assertEquals("type not correct!",
                   wrappedModelType,
                   refinedWrapped.getType());
   }

   @Test
   public void testDoesConvertToXtextObject() throws Throwable {
      wrapped = new WrappedBasePartModelReferenceField(resolver(), basePartDeclaration);
      refinedWrapped = new WrappedRefinedPartModelReferenceField(resolver(), refinedPartDeclaration);
      RefinedPartDeclaration xtext = WrappedRefinedPartModelReferenceField.toXTextPartDeclaration(resolver(), refinedWrapped);
      assertEquals("name not correct!",
                   refinedPartDeclaration.getName(),
                   xtext.getName());
      assertEquals("refined not correct!",
                   refinedPartDeclaration.isRefinedField(),
                   xtext.isRefinedField());
   }
}
