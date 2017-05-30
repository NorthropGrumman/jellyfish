package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traveral.IVisitor;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.WrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import java.util.Optional;

public class WrappedSystemDescriptor implements ISystemDescriptor {

  private final INamedChildCollection<ISystemDescriptor, IPackage> packages = new NamedChildCollection<>();

  private final IWrapperResolver resolver;
  private final EObject rootXtextObject;

  public WrappedSystemDescriptor(Package parsedPackage) {
    this.rootXtextObject = Preconditions.checkNotNull(parsedPackage, "parsedPackage may not be null!");
    resolver = newResolver();
    findAllPackages();
  }

  @Override
  public INamedChildCollection<ISystemDescriptor, IPackage> getPackages() {
    return packages;
  }

  @Override
  public Optional<Object> traverse(IVisitor visitor) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<IModel> findModel(String fullyQualifiedName) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<IModel> findModel(String packageName, String name) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<IData> findData(String fullyQualifiedName) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public Optional<IData> findData(String packageName, String name) {
    throw new UnsupportedOperationException("not implemented");
  }

  protected IWrapperResolver newResolver() {
    return new WrapperResolver(this, rootXtextObject);
  }

  protected ResourceSet doGetResourceSet(EObject object) {
    return object.eResource().getResourceSet();
  }

  private void findAllPackages() {
    ResourceSet set = doGetResourceSet(rootXtextObject);
    for (Resource r : set.getResources()) {
      for (EObject o : r.getContents()) {
        if (o instanceof Package) {
          Package p = (Package) o;
          packages.add(new WrappedPackage(resolver, this, p));
        }
      }
    }
  }
}
