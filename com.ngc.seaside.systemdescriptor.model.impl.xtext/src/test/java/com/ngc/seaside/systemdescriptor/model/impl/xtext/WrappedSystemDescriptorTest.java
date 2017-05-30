package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WrappedSystemDescriptorTest extends AbstractWrappedXtextTest {

  private WrappedSystemDescriptor wrapped;

  private Package xtextPackage1;

  private Package xtextPackage2;

  @Mock
  private ResourceSet resourceSet;

  @Before
  public void setup() throws Throwable {
    Data data = factory().createData();
    data.setName("MyData");

    Model model = factory().createModel();
    model.setName("MyModel");

    xtextPackage1 = factory().createPackage();
    xtextPackage2 = factory().createPackage();
    xtextPackage1.setName("hello.world");
    xtextPackage2.setName(xtextPackage1.getName());
    xtextPackage1.setElement(data);
    xtextPackage2.setElement(model);

    Resource resource1 = mock(Resource.class);
    Resource resource2 = mock(Resource.class);

    when(resourceSet.getResources()).thenReturn(ECollections.asEList(resource1, resource2));
    when(resource1.getContents()).thenReturn(ECollections.asEList(xtextPackage1));
    when(resource2.getContents()).thenReturn(ECollections.asEList(xtextPackage2));
  }

  @Test
  public void testDoesWrapAllPackages() throws Throwable {
    wrapped = new WrappedSystemDescriptor(xtextPackage1) {
      @Override
      protected ResourceSet doGetResourceSet(EObject object) {
        return resourceSet;
      }
    };
    assertTrue("did not wrap package!",
               wrapped.getPackages().getByName(xtextPackage1.getName()).isPresent());
    assertTrue("did not wrap package!",
               wrapped.getPackages().getByName(xtextPackage2.getName()).isPresent());
  }
}
