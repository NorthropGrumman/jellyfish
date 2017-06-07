package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class XTextSystemDescriptorServiceIT {

   private XTextSystemDescriptorService service;

   private ISystemDescriptorValidator validator;

   @Mock
   private ILogService logService;

   @Before
   public void setup() throws Throwable {
      validator = new AbstractSystemDescriptorValidator() {
         @Override
         protected void validateDataField(IValidationContext<IDataField> context) {
            IDataField field = context.getObject();
            if ("hour".equals(field.getName())) {
               context.declare(Severity.ERROR, "YOUS CANT HAVE NO FIELDS NAM'D 'hour'!!!!", field).getName();
            }
         }
      };

      Collection<Module> modules = new ArrayList<>();
      modules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(logService);
         }
      });
      service = XTextSystemDescriptorServiceBuilder.forIntegration(modules::add)
            .build(() -> Guice.createInjector(modules))
            .getInstance(XTextSystemDescriptorService.class);
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
      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      System.out.println(result.getIssues());
      assertFalse("validator should have triggered issues!",
                 result.isSuccessful());
   }

   @Test
   public void testDoesAutomaticallyRegisterInjectedValidators() throws Throwable {
   }
}
