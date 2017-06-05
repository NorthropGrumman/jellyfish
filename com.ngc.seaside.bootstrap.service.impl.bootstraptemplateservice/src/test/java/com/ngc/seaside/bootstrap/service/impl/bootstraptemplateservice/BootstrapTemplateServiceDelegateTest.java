package com.ngc.seaside.bootstrap.service.impl.bootstraptemplateservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.template.api.BootstrapTemplateException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @see BootstrapTemplateServiceDelegate
 */
public class BootstrapTemplateServiceDelegateTest {

   private ILogService logService;
   private IResourceService resourceService;
   private IPromptUserService promptUserService;

   private BootstrapTemplateServiceDelegate delegate;

   @Rule
   public TemporaryFolder testFolder = new TemporaryFolder();

   @Before
   public void setup() throws URISyntaxException {
      logService = new PrintStreamLogService();
      resourceService = mock(IResourceService.class);

      URL resourcesDir = getClass().getClassLoader().getResource("templates");
      File file = new File(resourcesDir.toURI()).getParentFile();

      when(resourceService.getResourceRootPath()).thenReturn(Paths.get(file.getAbsolutePath()));

      promptUserService = mock(IPromptUserService.class);

      delegate = new BootstrapTemplateServiceDelegate();
      delegate.setLogService(logService);
      delegate.setResourceService(resourceService);
      delegate.setPromptUserService(promptUserService);
   }

   @After
   public void shutdown() {
      delegate.removeLogService(logService);
      delegate.removePromptUserService(promptUserService);
      delegate.removeResourceService(resourceService);
   }

   /**
    * Simple test to ensure that the templates exist but it does not validate they are correct. See test below.
    */
   @Test
   public void testTemplateExists() {
      assertFalse(delegate.templateExists("invalid"));
      assertTrue(delegate.templateExists("example"));
      assertTrue(delegate.templateExists("invalidnofile"));
      assertTrue(delegate.templateExists("invalidnofolder"));
   }

   /**
    * Using the Temporary folder, ensure that the example is actually unpacked.
    *
    * @throws IOException if the test output directory can't be created.
    */
   @Test
   public void doesUnpack() throws IOException {
      File outputDirectory = testFolder.newFolder("output");

      when(promptUserService.prompt(anyString(), anyString(), any())).thenReturn("value");

      delegate.unpack("example", Paths.get(outputDirectory.getAbsolutePath()), false);

      //this is called 'value' because of the "when" above that mocks the promptUserService
      File value = Paths.get(outputDirectory.getAbsolutePath(), "value").toFile();
      File src = Paths.get(value.getAbsolutePath(), "src").toFile();

      assertTrue(value.exists());
      assertTrue(src.exists());
   }

   /**
    * Ensure that the invalid templates don't unpack.
    *
    * @throws IOException if the test output directory can't be created.
    */
   @Test
   public void doesNotUnpack() throws IOException {
      File outputDirectory = testFolder.newFolder("output");

      when(promptUserService.prompt(anyString(), anyString(), any())).thenReturn("value");

      try {
         delegate.unpack("invalidnofile", Paths.get(outputDirectory.getAbsolutePath()), false);
      } catch (BootstrapTemplateException e) {
         assertEquals(e.getMessage(),
                      "An error occurred processing the template zip file: invalidnofile");
         assertEquals(e.getCause().getMessage(), "Invalid template. Each template must contain template.properties and a template folder named 'template'");
      }

      try {
         delegate.unpack("invalidnofolder", Paths.get(outputDirectory.getAbsolutePath()), false);
      } catch (BootstrapTemplateException e) {
         assertEquals(e.getMessage(),
                      "An error occurred processing the template zip file: invalidnofolder");
         assertEquals(e.getCause().getMessage(), "Invalid template. Each template must contain template.properties and a template folder named 'template'");
      }
   }

}
