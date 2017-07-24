package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.module.XTextSystemDescriptorServiceModule;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.testutil.InjectorTestFactory;
import com.ngc.seaside.systemdescriptor.validation.api.AbstractSystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.IValidationContext;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class XTextSystemDescriptorServiceIT {

   private ISystemDescriptorService service;

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

      service = InjectorTestFactory.getSharedInstance().getInstance(ISystemDescriptorService.class);
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

   @Test
   public void testDoesCollectionModelsByStereotypes() throws Throwable {
      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("did not parse project!",
                 result.isSuccessful());

      Collection<IModel> systems = Traversals.collectModels(result.getSystemDescriptor(),
                                                            ModelPredicates.withAnyStereotype("system"));
      assertTrue("did not find AlarmClock system!",
                 systems.contains(result.getSystemDescriptor().findModel("clocks.AlarmClock").get()));
      assertEquals("contains extra model objects that don't have the system annotation!",
                   1,
                   systems.size());

      Collection<IModel> everythingButSystems = Traversals.collectModels(
            result.getSystemDescriptor(),
            ModelPredicates.withAnyStereotype("system").negate());
      assertFalse("AlarmClock system should not be in the result",
                  everythingButSystems.contains(result.getSystemDescriptor().findModel("clocks.AlarmClock").get()));
      assertEquals("did not find all results!",
                   4,
                   everythingButSystems.size());
   }

   @Ignore("This test cannot run with the build because XText holds state statically; however it is still useful to run"
           + " by itself to make sure the standalone configuration works.")
   @Test
   public void testDoesCreateStandaloneServiceWithGuice() throws Throwable {
      ILogService logService = mock(ILogService.class);
      Collection<Module> modules = new ArrayList<>();
      modules.add(new AbstractModule() {
         @Override
         protected void configure() {
            bind(ILogService.class).toInstance(new PrintStreamLogService());
         }
      });
      modules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
      Injector injector = Guice.createInjector(modules);
      service = injector.getInstance(ISystemDescriptorService.class);

      IParsingResult result = service.parseProject(Paths.get("build", "resources", "test", "valid-project"));
      assertTrue("did not parse project!",
                 result.isSuccessful());
   }
}
