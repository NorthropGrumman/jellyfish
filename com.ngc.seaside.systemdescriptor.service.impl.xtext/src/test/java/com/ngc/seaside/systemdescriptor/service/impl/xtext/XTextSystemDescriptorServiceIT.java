package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil.InjectorTestFactory;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class XTextSystemDescriptorServiceIT {

   private XTextSystemDescriptorService service;

   private ISystemDescriptorValidator validator;

   @Before
   public void setup() throws Throwable {
      validator = new AbstractSystemDescriptorValidator() {
         @Override
         protected void validateDataField(IValidationContext<IDataField> context) {
            IDataField field = context.getObject();
            if ("hour".equals(field.getName())) {
               context.declare(Severity.ERROR, "testing custom validation", field).getName();
            }
         }
      };

      service = InjectorTestFactory.getSharedInstance().getInstance(XTextSystemDescriptorService.class);
   }

   @Test
   public void testDoesParseProject() throws Throwable {
      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("did not parse project!",
                 result.isSuccessful());
   }
   
   @Test
   public void testDoesProgrammaticallyRegisterValidators() throws Throwable {
      service.addValidator(validator);
      try {
         IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
         assertFalse("validator should have triggered issues!",
                     result.isSuccessful());
      } finally {
         service.removeValidator(validator);
      }
   }
}
