package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;

public class WrappedBasePartModelReferenceFieldTest extends AbstractWrappedXtextTest {

   private WrappedBasePartModelReferenceField wrapped;

   private BasePartDeclaration basePartDeclaration;

   private Model modelType;

   @Mock
   private IModel parent;

   @Mock
   private IModel wrappedModelType;

   @Before
   public void setup() throws Throwable {
      basePartDeclaration = factory().createBasePartDeclaration();
      basePartDeclaration.setName("part1");

      modelType = factory().createModel();
      modelType.setName("MyType");
      basePartDeclaration.setType(modelType);

      Model model = factory().createModel();
      model.setParts(factory().createParts());
      model.getParts().getDeclarations().add(basePartDeclaration);

      IPackage p = mock(IPackage.class);
      when(p.getName()).thenReturn("some.package");
      when(wrappedModelType.getParent()).thenReturn(p);

      when(resolver().getWrapperFor(model)).thenReturn(parent);
      when(resolver().getWrapperFor(modelType)).thenReturn(wrappedModelType);
      when(resolver().findXTextModel(model.getName(), p.getName())).thenReturn(Optional.of(modelType));
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedBasePartModelReferenceField(resolver(), basePartDeclaration);
      assertEquals("name not correct!",
                   wrapped.getName(),
                   basePartDeclaration.getName());
      assertEquals("parent not correct!",
                   parent,
                   wrapped.getParent());
      assertEquals("type not correct!",
                   wrappedModelType,
                   wrapped.getType());
   }

   @Test
   public void testDoesUpdateXtextObject() throws Throwable {
      Model newXtextType = factory().createModel();
      IModel newType = mock(IModel.class);
      IPackage p = mock(IPackage.class);
      when(newType.getName()).thenReturn("NewModel");
      when(newType.getParent()).thenReturn(p);
      when(p.getName()).thenReturn("some.package");
      when(resolver().findXTextModel(newType.getName(), p.getName())).thenReturn(Optional.of(newXtextType));

      wrapped = new WrappedBasePartModelReferenceField(resolver(), basePartDeclaration);
      wrapped.setType(newType);
      assertEquals("type not updated!",
                   newXtextType,
                   basePartDeclaration.getType());
   }

   @Test
   public void testDoesConvertToXtextObject() throws Throwable {
      wrapped = new WrappedBasePartModelReferenceField(resolver(), basePartDeclaration);
      BasePartDeclaration xtext = WrappedBasePartModelReferenceField.toXTextPartDeclaration(resolver(), wrapped);
      assertEquals("name not correct!",
                   basePartDeclaration.getName(),
                   xtext.getName());
      assertEquals("type not correct!",
                   basePartDeclaration.getType(),
                   xtext.getType());
   }
}
