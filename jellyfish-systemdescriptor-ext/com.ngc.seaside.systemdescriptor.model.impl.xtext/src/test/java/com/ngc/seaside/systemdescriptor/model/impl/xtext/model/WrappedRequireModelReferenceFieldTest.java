package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtextTest;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedRequireModelReferenceFieldTest extends AbstractWrappedXtextTest {

   private WrappedRequireModelReferenceField wrapped;

   private RequireDeclaration requireDeclaration;

   private Model modelType;

   @Mock
   private IModel parent;

   @Mock
   private IModel wrappedModelType;

   @Before
   public void setup() throws Throwable {
      requireDeclaration = factory().createRequireDeclaration();
      requireDeclaration.setName("require1");

      modelType = factory().createModel();
      modelType.setName("MyType");
      requireDeclaration.setType(modelType);

      Model model = factory().createModel();
      model.setRequires(factory().createRequires());
      model.getRequires().getDeclarations().add(requireDeclaration);

      IPackage p = mock(IPackage.class);
      when(p.getName()).thenReturn("some.package");
      when(wrappedModelType.getParent()).thenReturn(p);

      when(resolver().getWrapperFor(model)).thenReturn(parent);
      when(resolver().getWrapperFor(modelType)).thenReturn(wrappedModelType);
      when(resolver().findXTextModel(model.getName(), p.getName())).thenReturn(Optional.of(modelType));
   }

   @Test
   public void testDoesWrapXtextObject() throws Throwable {
      wrapped = new WrappedRequireModelReferenceField(resolver(), requireDeclaration);
      assertEquals("name not correct!",
                   wrapped.getName(),
                   requireDeclaration.getName());
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

      wrapped = new WrappedRequireModelReferenceField(resolver(), requireDeclaration);
      wrapped.setType(newType);
      assertEquals("type not updated!",
                   newXtextType,
                   requireDeclaration.getType());
   }

   @Test
   public void testDoesConvertToXtextObject() throws Throwable {
      wrapped = new WrappedRequireModelReferenceField(resolver(), requireDeclaration);
      RequireDeclaration xtext = WrappedRequireModelReferenceField.toXTextRequireDeclaration(resolver(), wrapped);
      assertEquals("name not correct!",
                   requireDeclaration.getName(),
                   xtext.getName());
      assertEquals("type not correct!",
                   requireDeclaration.getType(),
                   xtext.getType());
   }
}
