package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.extension.IValidatorExtension;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.validation.SystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ValidationDelegateTest {

   private ValidationDelegate delegate;

   private ISystemDescriptor descriptor;

   @Mock
   private SystemDescriptorValidator dslValidator;

   @Mock
   private ISystemDescriptorValidator validator;

   @Mock
   private ILogService logService;

   @Mock
   private IValidatorExtension.ValidationHelper helper;

   @Before
   public void setup() throws Throwable {
      delegate = new ValidationDelegate(dslValidator, logService) {
         @Override
         protected void doValidate(EObject source, ValidationHelper helper,
                                   ISystemDescriptor descriptor) {
            // Save the descriptor so we can use it in verification.
            ValidationDelegateTest.this.descriptor = descriptor;
            super.doValidate(source, helper, descriptor);
         }
      };
   }

   @Test
   public void testDoesRegisterAndUnregisterValidatorWithDsl() throws Throwable {
      delegate.addValidator(validator);
      delegate.removeValidator(validator);

      verify(dslValidator).addValidatorExtension(delegate);
      verify(dslValidator).removeValidatorExtension(delegate);
   }

   @Test
   public void testDoesValidatePackage() throws Throwable {
      Package source = factory().createPackage();
      source.setName("foo.package");

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IPackage toValidate = descriptor.getPackages().getByName(source.getName()).get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateData() throws Throwable {
      Data source = factory().createData();
      source.setName("MyData");
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(source);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IData toValidate = descriptor.findData(p.getName(), source.getName()).get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }

   @Test
   public void testDoesValidateDataField() throws Throwable {
      DataFieldDeclaration source = factory().createDataFieldDeclaration();
      source.setName("myField");
      Data data = factory().createData();
      data.setName("MyData");
      data.getFields().add(source);
      Package p = factory().createPackage();
      p.setName("foo.package");
      p.setElement(data);

      delegate.addValidator(validator);
      delegate.validate(source, helper);

      IDataField toValidate = descriptor.findData(p.getName(), data.getName()).get()
            .getFields()
            .getByName(source.getName())
            .get();
      verify(validator).validate(argThat(ctx -> toValidate.equals(ctx.getObject())));
   }


   @Test
   public void testDoesConsumeValidatorException() throws Throwable {
      Package source = factory().createPackage();
      source.setName("foo.package");

      doThrow(new RuntimeException("testing error handling")).when(validator).validate(any());

      delegate.addValidator(validator);
      delegate.validate(source, helper);
      // No exception should be thrown.
   }

   private static SystemDescriptorFactory factory() {
      return SystemDescriptorFactory.eINSTANCE;
   }
}
