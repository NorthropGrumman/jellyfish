package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedPartModelReferenceFieldTest extends AbstractWrappedXtextTest {

   private WrappedPartModelReferenceField wrapped;

   private PartDeclaration partDeclaration;

   private Model modelType;

   @Mock
   private IModel parent;

   @Mock
   private IModel wrappedModelType;

   @Before
   public void setup() throws Throwable {
      partDeclaration = factory().createPartDeclaration();
      partDeclaration.setName("part1");

      modelType = factory().createModel();
      modelType.setName("MyType");
      partDeclaration.setType(modelType);

      Model model = factory().createModel();
      model.setParts(factory().createParts());
      model.getParts().getDeclarations().add(partDeclaration);

      IPackage p = mock(IPackage.class);
      when(p.getName()).thenReturn("some.package");
      when(wrappedModelType.getParent()).thenReturn(p);

      when(resolver().getWrapperFor(model)).thenReturn(parent);
      when(resolver().getWrapperFor(modelType)).thenReturn(wrappedModelType);
      when(resolver().findXTextModel(model.getName(), p.getName())).thenReturn(Optional.of(modelType));
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedPartModelReferenceField(resolver(), partDeclaration);
      assertEquals("name not correct!",
                   wrapped.getName(),
                   partDeclaration.getName());
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

      wrapped = new WrappedPartModelReferenceField(resolver(), partDeclaration);
      wrapped.setType(newType);
      assertEquals("type not updated!",
                   newXtextType,
                   partDeclaration.getType());
   }

   @Test
   public void testDoesConvertToXtextObject() throws Throwable {
      wrapped = new WrappedPartModelReferenceField(resolver(), partDeclaration);
      PartDeclaration xtext = WrappedPartModelReferenceField.toXTextPartDeclaration(resolver(), wrapped);
      assertEquals("name not correct!",
                   partDeclaration.getName(),
                   xtext.getName());
      assertEquals("type not correct!",
                   partDeclaration.getType(),
                   xtext.getType());
   }
}
