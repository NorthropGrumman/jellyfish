package com.ngc.seaside.systemdescriptor.model.impl.xtext;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.impl.basic.NamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.WrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import java.util.Optional;

/**
 * An implementation of {@code ISystemDescriptor} that wraps one or more {@link Package}s as parsed by XText.
 *
 * This class is not threadsafe.
 */
public class WrappedSystemDescriptor implements ISystemDescriptor {

   private final INamedChildCollection<ISystemDescriptor, IPackage> packages = new NamedChildCollection<>();
   private final IWrapperResolver resolver;
   private final EObject rootXtextObject;

   public WrappedSystemDescriptor(Package parsedPackage) {
      this.rootXtextObject = Preconditions.checkNotNull(parsedPackage, "parsedPackage may not be null!");
      // Create a new resolver.
      resolver = newResolver();
      // Register all packages that XText parsed.
      findAllPackages();

      // Perform any initialize that needs to happen after all packages are registered.
      DeferredInitialization.packagesWrapped();
   }

   @Override
   public INamedChildCollection<ISystemDescriptor, IPackage> getPackages() {
      return packages;
   }

   @Override
   public Optional<IModel> findModel(String fullyQualifiedName) {
      Preconditions.checkNotNull(fullyQualifiedName, "fullyQualifiedName may not be null!");
      Preconditions.checkArgument(!fullyQualifiedName.trim().isEmpty(), "fullyQualifiedName may not be empty!");
      int namePosition = fullyQualifiedName.lastIndexOf('.');
      Preconditions.checkArgument(namePosition > 0
                                  && namePosition < fullyQualifiedName.length() - 1,
                                  "expected a fully qualified name of the form <packageName>.<modelName> but got '%s'!",
                                  fullyQualifiedName);
      String packageName = fullyQualifiedName.substring(0, namePosition);
      String modelName = fullyQualifiedName.substring(namePosition + 1);
      return findModel(packageName, modelName);
   }

   @Override
   public Optional<IModel> findModel(String packageName, String name) {
      Preconditions.checkNotNull(packageName, "packageName may not be null!");
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!packageName.trim().isEmpty(), "packageName may not be empty!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");

      Optional<IModel> model = Optional.empty();
      Optional<IPackage> p = packages.getByName(packageName);
      if (p.isPresent()) {
         model = p.get().getModels().getByName(name);
      }
      return model;
   }

   @Override
   public Optional<IData> findData(String fullyQualifiedName) {
      Preconditions.checkNotNull(fullyQualifiedName, "fullyQualifiedName may not be null!");
      Preconditions.checkArgument(!fullyQualifiedName.trim().isEmpty(), "fullyQualifiedName may not be empty!");
      int namePosition = fullyQualifiedName.lastIndexOf('.');
      Preconditions.checkArgument(namePosition > 0
                                  && namePosition < fullyQualifiedName.length() - 1,
                                  "expected a fully qualified name of the form <packageName>.<dataName> but got '%s'!",
                                  fullyQualifiedName);
      String packageName = fullyQualifiedName.substring(0, namePosition);
      String dataName = fullyQualifiedName.substring(namePosition + 1);
      return findData(packageName, dataName);
   }

   @Override
   public Optional<IData> findData(String packageName, String name) {
      Preconditions.checkNotNull(packageName, "packageName may not be null!");
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!packageName.trim().isEmpty(), "packageName may not be empty!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");

      Optional<IData> data = Optional.empty();
      Optional<IPackage> p = packages.getByName(packageName);
      if (p.isPresent()) {
         data = p.get().getData().getByName(name);
      }
      return data;
   }

   @Override
   public Optional<IEnumeration> findEnumeration(String fullyQualifiedName) {
      Preconditions.checkNotNull(fullyQualifiedName, "fullyQualifiedName may not be null!");
      Preconditions.checkArgument(!fullyQualifiedName.trim().isEmpty(), "fullyQualifiedName may not be empty!");
      int namePosition = fullyQualifiedName.lastIndexOf('.');
      Preconditions.checkArgument(namePosition > 0
                                  && namePosition < fullyQualifiedName.length() - 1,
                                  "expected a fully qualified name of the form <packageName>.<enumName> but got '%s'!",
                                  fullyQualifiedName);
      String packageName = fullyQualifiedName.substring(0, namePosition);
      String enumName = fullyQualifiedName.substring(namePosition + 1);
      return findEnumeration(packageName, enumName);
   }

   @Override
   public Optional<IEnumeration> findEnumeration(String packageName, String name) {
      Preconditions.checkNotNull(packageName, "packageName may not be null!");
      Preconditions.checkNotNull(name, "name may not be null!");
      Preconditions.checkArgument(!packageName.trim().isEmpty(), "packageName may not be empty!");
      Preconditions.checkArgument(!name.trim().isEmpty(), "name may not be empty!");

      Optional<IEnumeration> enumeration = Optional.empty();
      Optional<IPackage> p = packages.getByName(packageName);
      if (p.isPresent()) {
         enumeration = p.get().getEnumerations().getByName(name);
      }
      return enumeration;
   }

   /**
    * Factory method that creates a new {@link IWrapperResolver} implementation.  Extending classes or tests may
    * override this method.
    */
   protected IWrapperResolver newResolver() {
      return new WrapperResolver(this, rootXtextObject);
   }

   /**
    * Template method that obtains the resource set used to create the root object or {@code null} if there is no such
    * set.  Extending classes or tests may override this method.
    */
   protected ResourceSet doGetResourceSet(EObject object) {
      // eResource can be null depending on how the XText parser was used/configured.
      Resource resource = object.eResource();
      return resource == null ? null : resource.getResourceSet();
   }

   private void findAllPackages() {
      ResourceSet set = doGetResourceSet(rootXtextObject);
      if (set != null) {
         for (Resource r : set.getResources()) {
            for (EObject o : r.getContents()) {
               if (o instanceof Package) {
                  Package p = (Package) o;
                  // Have we already wrapped a package with the same name?  If so, don't create a new wrapper, but reuse the
                  // old one.
                  Optional<IPackage> wrapper = packages.getByName(p.getName());
                  if (wrapper.isPresent()) {
                     // This cast is safe because, as this point, only WrappedPackages are contained in the collection.
                     ((WrappedPackage) wrapper.get()).wrap(p);
                  } else {
                     // Otherwise, create a new wrapper for the package.
                     packages.add(new WrappedPackage(resolver, this, p));
                  }
               }
            }
         }
      } else if (rootXtextObject instanceof Package) {
         // If the resource set is null, just register the single package.  This means imports will not resolve.
         packages.add(new WrappedPackage(resolver, this, (Package) rootXtextObject));
      }
   }
}
