package com.ngc.seaside.systemdescriptor.validation;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentsEList.FeatureIterator;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceDescriptionsProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.validation.Check;
import org.eclipse.xtext.xbase.validation.IssueCodes;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This is a validator that ensures that imports correctly resolve to some
 * element.
 */
public class ImportValidator extends AbstractUnregisteredSystemDescriptorValidator {

   /**
    * A provider that is used to get the IResourceDescriptions which serves as
    * the main index or cache for Xtext. This is used do reverse lookups to
    * elements using their qualified names.
    */
   @Inject
   private IResourceDescriptionsProvider resourceDescriptionsProvider;

   /**
    * Used to construct a QualifiedName from a fually qualified name
    * represented as a string.
    */
   @Inject
   private IQualifiedNameConverter qualifiedNameConverter;

   /**
    * A provider that is used to determine the location of elements in a document.
    */
   @Inject
   private ILocationInFileProvider locationInFileProvider;

   /**
    * Verifies the imports of the given package actually resolve to something.
    */
   @Check
   public void checkImports(Package pkg) {
      Function<Import, QualifiedName> qualifiedNameFunction = i -> qualifiedNameConverter.toQualifiedName(
            i.getImportedNamespace());
      Function<Import, String> unqualifiedNameFunction = i -> qualifiedNameConverter.toQualifiedName(
            i.getImportedNamespace()).getLastSegment();

      final IResourceDescriptions resourceDescriptions = resourceDescriptionsProvider.getResourceDescriptions(
            pkg.eResource().getResourceSet());

      final Set<QualifiedName> packageResources = new HashSet<>();
      pkg.eAllContents().forEachRemaining(element -> {

         for (FeatureIterator<EObject> iter = (FeatureIterator<EObject>) element.eCrossReferences()
               .iterator(); iter.hasNext(); ) {
            EObject referencedElement = iter.next();
            EStructuralFeature feature = iter.feature();

            String referencedName = getReferencedType(element, feature, pkg);

            if (referencedName.isEmpty() || referencedName.indexOf('.') >= 0) {
               // Ignore references that use fully-qualified names
               continue;
            }

            for (IEObjectDescription description : resourceDescriptions.getExportedObjectsByObject(referencedElement)) {
               packageResources.add(description.getQualifiedName());
            }
         }
      });

      final Set<QualifiedName> externalResources = new HashSet<>();
      resourceDescriptions.getExportedObjectsByType(SystemDescriptorPackage.Literals.ELEMENT)
            .forEach(element -> externalResources.add(element.getQualifiedName()));

      Map<String, Map<QualifiedName, List<Import>>> allImports = pkg.getImports()
            .stream()
            .collect(
                  Collectors.groupingBy(unqualifiedNameFunction,
                                        Collectors.groupingBy(
                                              qualifiedNameFunction)));

      allImports.values()
            .stream()
            .peek(map -> {
               if (map.size() > 1) {
                  map.values().stream().flatMap(List::stream).forEach(i -> {
                     error("The import " + i.getImportedNamespace() + " collides with another import statement",
                           i,
                           null,
                           IssueCodes.IMPORT_COLLISION);
                  });
               }
            })
            .filter(map -> map.size() == 1)
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            .forEach((name, list) -> {
               if (!externalResources.contains(name)) {
                  list.forEach(
                        i -> error("The import " + name + " cannot be resolved",
                                   i,
                                   null,
                                   IssueCodes.IMPORT_UNRESOLVED));
                  return;
               }
               final List<Import> unnecessaryImports;
               if (!packageResources.contains(name)) {
                  unnecessaryImports = list;
               } else if (list.size() > 1) {
                  unnecessaryImports = list.subList(1, list.size());
               } else {
                  unnecessaryImports = Collections.emptyList();
               }
               unnecessaryImports.stream()
                     .forEach(
                           i -> warning("The import " + name + " is never used",
                                        i,
                                        null,
                                        IssueCodes.IMPORT_UNUSED));
            });

   }

   /**
    * Returns the actual text of a reference.
    *
    * @param resource resource containing text
    * @param region   region of reference
    * @return actual text of reference
    */
   private String getReferencedType(EObject element, EStructuralFeature feature, Package pkg) {
      XtextResource resource = (XtextResource) pkg.eResource();
      ITextRegion region = locationInFileProvider.getSignificantTextRegion(element, feature, 0);

      IParseResult parseResult = resource.getParseResult();
      if (parseResult != null && region != null) {
         return parseResult.getRootNode().getText().substring(region.getOffset(),
                                                              region.getOffset() + region.getLength());
      }
      return "";
   }

}
