package com.ngc.seaside.bootstrap.service.impl.templateservice;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.blocs.service.resource.api.IResourceService;
import com.ngc.blocs.test.impl.common.log.PrintStreamLogService;
import com.ngc.seaside.bootstrap.service.promptuser.api.IPromptUserService;
import com.ngc.seaside.bootstrap.service.property.api.IProperties;
import com.ngc.seaside.bootstrap.service.property.api.IPropertyService;
import com.ngc.seaside.bootstrap.service.template.api.TemplateServiceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 */
public class TemplateServiceTest {

   private ILogService logService;
   private IResourceService resourceService;
   private IPromptUserService promptUserService;
   private IPropertyService propertyService;

   private TemplateService delegate;

   @Rule
   public TemporaryFolder testFolder = new TemporaryFolder();

   @Before
   public void setup() throws URISyntaxException {
      logService = new PrintStreamLogService();
      resourceService = mock(IResourceService.class);

      URL resourcesDir = getClass().getClassLoader().getResource("templates");
      assertNotNull(resourcesDir);
      File file = new File(resourcesDir.toURI()).getParentFile();

      when(resourceService.getResourceRootPath()).thenReturn(Paths.get(file.getAbsolutePath()));

      promptUserService = mock(IPromptUserService.class);
      propertyService = mock(IPropertyService.class);

      delegate = new TemplateService();
      delegate.setLogService(logService);
      delegate.setResourceService(resourceService);
      delegate.setPromptUserService(promptUserService);
      delegate.setPropertyService(propertyService);
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
      assertFalse(delegate.templateExists("com.ngc.seaside.bootstrap.command.impl.invalid"));
      assertTrue(delegate.templateExists("com.ngc.seaside.bootstrap.command.impl.example"));
      assertTrue(delegate.templateExists("com.ngc.seaside.bootstrap.command.impl.invalidnofile"));
      assertTrue(delegate.templateExists("com.ngc.seaside.bootstrap.command.impl.invalidnofolder"));
   }

   /**
    * Using the Temporary folder, ensure that the example is actually unpacked.
    *
    * @throws IOException if the test output directory can't be created.
    */
   @Test
   public void doesUnpack() throws IOException {
      File outputDirectory = testFolder.newFolder("output");

      ArgumentCaptor<String> parameterCapture = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> defaultCapture = ArgumentCaptor.forClass(String.class);

      IProperties properties = mock(IProperties.class);
      when(properties.get("classname")).thenReturn("MyClass");
      when(properties.get("groupId")).thenReturn("com.ngc.seaside");
      when(properties.get("artifactId")).thenReturn("mybundle");
      when(properties.get("version")).thenReturn("1.0-SNAPSHOT");
      when(properties.get("package")).thenReturn("com.ngc.seaside.mybundle");
      when(properties.getKeys()).thenReturn(
               Arrays.asList(new String[] { "classname", "groupId", "artifactId", "version", "package" }));

      when(propertyService.load(any())).thenReturn(properties);
      when(promptUserService.prompt(
               parameterCapture.capture(), defaultCapture.capture(), any())).thenReturn("value");

      delegate.unpack("com.ngc.seaside.bootstrap.command.impl.example",
                      Paths.get(outputDirectory.getAbsolutePath()), false);

      verify(promptUserService, times(5))
               .prompt(anyString(), anyString(), any());

      //this is called 'value' because of the "when" above that mocks the promptUserService
      File value = Paths.get(outputDirectory.getAbsolutePath(), "value.value").toFile();
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
         delegate.unpack("com.ngc.seaside.bootstrap.command.impl.invalidnofile",
                         Paths.get(outputDirectory.getAbsolutePath()), false);
      } catch (TemplateServiceException e) {
         assertEquals(e.getMessage(),
                      "An error occurred processing the template zip file: com.ngc.seaside.bootstrap.command.impl.invalidnofile");
         assertEquals(e.getCause().getMessage(),
                      "Invalid template. Each template must contain template.properties and a template folder named 'templateContent'");
      }

      try {
         delegate.unpack("com.ngc.seaside.bootstrap.command.impl.invalidnofolder",
                         Paths.get(outputDirectory.getAbsolutePath()), false);
      } catch (TemplateServiceException e) {
         assertEquals(e.getMessage(),
                      "An error occurred processing the template zip file: com.ngc.seaside.bootstrap.command.impl.invalidnofolder");
         assertEquals(e.getCause().getMessage(),
                      "Invalid template. Each template must contain template.properties and a template folder named 'templateContent'");
      }
   }

}
