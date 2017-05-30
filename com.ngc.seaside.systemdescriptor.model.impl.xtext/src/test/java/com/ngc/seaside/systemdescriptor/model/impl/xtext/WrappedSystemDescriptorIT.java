package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.io.Closeables;
import com.google.inject.Inject;
import com.google.inject.Injector;

import com.ngc.seaside.systemdescriptor.SystemDescriptorStandaloneSetup;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WrappedSystemDescriptorIT {

  private WrappedSystemDescriptor wrapped;


  private InputStream timeInputStream;

  private InputStream timerInputStream;

  @Inject
  private IParser parser;

  @Inject
  private XtextResourceSet resourceSet;

  @Before
  public void setup() throws Throwable {
    Injector injector = new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration();
    injector.injectMembers(this);
    resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);

    timeInputStream = getClass().getClassLoader().getResourceAsStream("clocks/datatypes/Time.sd");
    timerInputStream = getClass().getClassLoader().getResourceAsStream("clocks/models/Timer.sd");
  }

  @Test
  public void doesCreateWrappedDescriptorWithSingleResource() throws Throwable {
    IParseResult result = parser.parse(new InputStreamReader(timeInputStream));
    Package p = (Package) result.getRootASTElement();
    wrapped = new WrappedSystemDescriptor(p);

    Optional<IData> data = wrapped.findData("clocks.datatypes", "Time");
    assertTrue("data not found!",
               data.isPresent());
    assertNotNull("metadata not set!",
                  data.get().getMetadata().getJson());
    assertTrue("data field not set!",
               data.get().getFields().getByName("hour").isPresent());
  }

  @Test
  public void testDoesCreateWrappedDescriptorWithMultipleResources() throws Throwable {
    Resource timeResource = resourceSet.createResource(URI.createURI("dummy:/Time.sd"));
    timeResource.load(timeInputStream, resourceSet.getLoadOptions());

    Resource timerResource = resourceSet.createResource(URI.createURI("dummy:/Timer.sd"));
    timerResource.load(timerInputStream, resourceSet.getLoadOptions());

    Package p = (Package) timerResource.getContents().get(0);
    wrapped = new WrappedSystemDescriptor(p);

    Optional<IData> data = wrapped.findData("clocks.datatypes", "Time");
    assertTrue("data not found!",
               data.isPresent());
    assertNotNull("metadata not set!",
                  data.get().getMetadata().getJson());
    assertTrue("data field not set!",
               data.get().getFields().getByName("hour").isPresent());

    Optional<IModel> model = wrapped.findModel("clocks.models", "Timer");
    assertTrue("model not found!",
               model.isPresent());
    assertNotNull("metadata not set!",
                  model.get().getMetadata().getJson());
    assertTrue("output field not set!",
               model.get().getOutputs().getByName("currentTime").isPresent());
  }

  @After
  public void teardown() throws Throwable {
    if (timeInputStream != null) {
      Closeables.closeQuietly(timeInputStream);
    }
    if (timerInputStream != null) {
      Closeables.closeQuietly(timerInputStream);
    }
  }

}
