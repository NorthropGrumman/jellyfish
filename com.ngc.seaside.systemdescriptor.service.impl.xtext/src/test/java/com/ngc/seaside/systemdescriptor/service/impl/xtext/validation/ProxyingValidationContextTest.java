package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.ngc.seaside.systemdescriptor.extension.IValidatorExtension;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.WrappedPackage;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.data.WrappedDataField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ProxyingValidationContextTest {

   private ProxyingValidationContext<IDataField> ctx;

   private IDataField dataField;

   private DataFieldDeclaration xtext;

   @Mock
   private IValidatorExtension.ValidationHelper validationHelper;

   @Mock
   private IWrapperResolver wrapperResolver;

   @Before
   public void setup() throws Throwable {
      xtext = SystemDescriptorFactory.eINSTANCE.createDataFieldDeclaration();
      xtext.setName("foo");

      dataField = new WrappedDataField(wrapperResolver, xtext);

      ctx = new ProxyingValidationContext<>(dataField, validationHelper);
   }

   @Test
   public void testDoesDeclareError() throws Throwable {
      String msg = "error message";
      ctx.declare(Severity.ERROR, msg, dataField).getName();
      verify(validationHelper).error(msg, xtext, SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME);
   }

   @Test
   public void testDoesDeclareWarning() throws Throwable {
      String msg = "warning message";
      ctx.declare(Severity.WARNING, msg, dataField).getName();
      verify(validationHelper).warning(msg, xtext, SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME);
   }

   @Test
   public void testDoesDeclareSuggestion() throws Throwable {
      String msg = "suggestion message";
      ctx.declare(Severity.SUGGESTION, msg, dataField).getName();
      verify(validationHelper).info(msg, xtext, SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME);
   }

   @Test
   public void testDoesValidatePackage() throws Throwable {
      Package p = SystemDescriptorFactory.eINSTANCE.createPackage();
      p.setName("myPackage");
      p.setElement(SystemDescriptorFactory.eINSTANCE.createModel());

      ISystemDescriptor descriptor = mock(ISystemDescriptor.class);
      IPackage wrapped = new WrappedPackage(wrapperResolver, descriptor, p);

      ProxyingValidationContext<IPackage> ctx = new ProxyingValidationContext<>(wrapped, validationHelper);

      String msg = "error message";
      ctx.declare(Severity.ERROR, msg, wrapped).getName();
      verify(validationHelper).error(msg, p, SystemDescriptorPackage.Literals.PACKAGE__NAME);
   }
}
