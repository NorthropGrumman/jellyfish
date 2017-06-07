package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
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
               context.declare(Severity.ERROR, "testing custom validation", field).getName();
            }
         }
      };
   }

   @Test
   public void testDoesParseProject() throws Throwable {
      setupService(new ArrayList<>());

      service.addValidator(validator);
      try {
         IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
         assertTrue("did not parse project!",
                    result.isSuccessful());
      }  finally {
         service.removeValidator(validator);
      }
   }

   @Test
   public void testDoesProgrammaticallyRegisterValidators() throws Throwable {
      setupService(new ArrayList<>());

      service.addValidator(validator);
      try {
         for(int i = 0; i < 5; i++) {
            IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
            assertFalse("validator should have triggered issues!",
                        result.isSuccessful());
         }
      } finally {
         service.removeValidator(validator);
      }
   }

   @Test
   public void testDoesAutomaticallyRegisterInjectedValidators() throws Throwable {
      Collection<Module> modules = new ArrayList<>();
      modules.add(new AbstractModule() {
         @Override
         protected void configure() {
            Multibinder<ISystemDescriptorValidator> validatorBinder = Multibinder.newSetBinder(
                  binder(),
                  ISystemDescriptorValidator.class);
            validatorBinder.addBinding().toInstance(validator);
         }
      });
      setupService(modules);

      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertFalse("validator should have triggered issues!",
                  result.isSuccessful());
   }

   private void setupService(Collection<Module> modules) {
      modules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(new PrintStreamLogService());
         }
      });
      service = XTextSystemDescriptorServiceBuilder.forIntegration(modules::add)
            .build(() -> Guice.createInjector(modules))
            .getInstance(XTextSystemDescriptorService.class);
   }
}
