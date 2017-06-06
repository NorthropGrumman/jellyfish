package com.ngc.seaside.systemdescriptor.service.impl.xtext.validation;

import com.google.inject.Injector;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegate;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.parsing.ParsingDelegateIT;
import com.ngc.seaside.systemdescriptor.validation.SystemDescriptorValidator;
import com.ngc.seaside.systemdescriptor.validation.api.ISystemDescriptorValidator;

import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ValidationDelegateIT {

   private ValidationDelegate delegate;
   private ParsingDelegate parsingDelegate;

   @Mock
   private ISystemDescriptorValidator validator;

   @Mock
   private ILogService logService;

   @Before
   public void setup() throws Throwable {
      Injector injector = new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration();
      delegate = new ValidationDelegate(injector.getInstance(SystemDescriptorValidator.class));
      parsingDelegate = new ParsingDelegate(injector.getInstance(IParser.class),
                                            injector.getInstance(XtextResourceSet.class),
                                            logService);
   }

   @Test
   public void testDoesRegisterValidator() throws Throwable {
      delegate.addValidator(validator);

      Path time = ParsingDelegateIT.pathTo("valid-project", "clocks", "datatypes", "Time.sd");
      IParsingResult result = parsingDelegate.parseFiles(Collections.singletonList(time));
      assertTrue("parsing should be successful!",
                 result.isSuccessful());
   }
}
