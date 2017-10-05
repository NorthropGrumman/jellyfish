package com.ngc.seaside.systemdescriptor.ui.quickfix.imports;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceDescriptionsProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.util.ITextRegion;

import com.google.common.collect.Iterables;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.systemDescriptor.impl.ImportImpl;

/**
 * Interface for determining the list of imports that a package needs.
 */
@FunctionalInterface
@ImplementedBy(DefaultImportsResolver.class)
public interface IImportsResolver {

   /**
    * Returns a list of imports that the given package needs.
    * 
    * @param pkg package
    * @param resource xtext resource
    * @return a list of imports that the given package needs
    */
   List<Import> resolveImports(Package pkg, XtextResource resource);

}

class DefaultImportsResolver implements IImportsResolver {

   /**
    * A selector for choosing imports.
    */
   @Inject
   private IReferenceSelector importChooser;

   /**
    * A provider that is used to get the IResourceDescriptions which serves as
    * the main index or cache for Xtext. This is used do reverse lookups to
    * elements using their qualified names.
    */
   @Inject
   private IResourceDescriptionsProvider resourceDescriptionsProvider;

   /**
    * A provider that is used to determine the location of elements in a document.
    */
   @Inject
   private ILocationInFileProvider locationInFileProvider;

   /**
    * Used to construct a QualifiedName from a fually qualified name
    * represented as a string.
    */
   @Inject
   private IQualifiedNameConverter qualifiedNameConverter;

   /**
    * Used to determine possible types a reference could be referring to.
    */
   @Inject
   private IReferenceResolver referenceResolver;

   @Override
   public List<Import> resolveImports(Package pkg, XtextResource resource) {
      final IResourceDescriptions descriptions = resourceDescriptionsProvider.getResourceDescriptions(
         pkg.eResource().getResourceSet());
      final Set<QualifiedName> names = new HashSet<>();
      TreeIterator<EObject> contents = EcoreUtil.getAllContents(pkg, true);
      Map<String, QualifiedName> checkedUnresolvedReferences = new HashMap<>();
      while (contents.hasNext()) {
         EObject element = contents.next();

         for (EObject reference : element.eCrossReferences()) {
            List<IEObjectDescription> referenceDescriptions = new ArrayList<>();
            Iterables.addAll(referenceDescriptions, descriptions.getExportedObjectsByObject(reference));
            if (referenceDescriptions.isEmpty()) {
               Entry<EReference, Predicate<? super EObject>> ref = getReference(element);
               ITextRegion refRegion = locationInFileProvider.getSignificantTextRegion(element, ref.getKey(), 0);
               Optional<QualifiedName> choice = findUnknownReference(resource,
                  resource.getResourceSet(),
                  checkedUnresolvedReferences,
                  refRegion,
                  ref.getValue());
               if (choice.isPresent()) {
                  names.add(choice.get());
               }
            } else {
               for (IEObjectDescription description : descriptions.getExportedObjectsByObject(reference)) {
                  names.add(description.getQualifiedName());
               }
            }
         }
      }

      return names.stream().map(name -> new ImportImpl() {
         @Override
         public String getImportedNamespace() {
            return qualifiedNameConverter.toString(name);
         }
      }).collect(Collectors.toList());
   }

   private Entry<EReference, Predicate<? super EObject>> getReference(EObject element) {
      final Predicate<EObject> DATA_INSTANCE = object -> object instanceof Data;
      final Predicate<EObject> DATA_MODEL_INSTANCE = object -> object instanceof DataModel;
      final Predicate<EObject> MODEL_INSTANCE = object -> object instanceof Model;
      if (element instanceof ReferencedDataModelFieldDeclaration) {
         return new SimpleImmutableEntry<>(
            SystemDescriptorPackage.Literals.REFERENCED_DATA_MODEL_FIELD_DECLARATION__DATA_MODEL, DATA_MODEL_INSTANCE);
      }
      if (element instanceof Data) {
         return new SimpleImmutableEntry<>(SystemDescriptorPackage.Literals.DATA__SUPERCLASS, DATA_INSTANCE);
      }
      if (element instanceof InputDeclaration) {
         return new SimpleImmutableEntry<>(SystemDescriptorPackage.Literals.INPUT_DECLARATION__TYPE, DATA_INSTANCE);
      }
      if (element instanceof OutputDeclaration) {
         return new SimpleImmutableEntry<>(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION__TYPE, DATA_INSTANCE);
      }
      if (element instanceof PartDeclaration) {
         return new SimpleImmutableEntry<>(SystemDescriptorPackage.Literals.PART_DECLARATION__TYPE, MODEL_INSTANCE);
      }
      if (element instanceof RequireDeclaration) {
         return new SimpleImmutableEntry<>(SystemDescriptorPackage.Literals.REQUIRE_DECLARATION__TYPE,
            MODEL_INSTANCE);
      }

      throw new IllegalStateException("Unknown reference for type " + element.eClass().getName());
   }

   /**
    * Finds the qualified name for the unknown reference.
    * 
    * @param resource resource
    * @param resourceSet resource set
    * @param references import references that have previously been resolved by the user
    * @param region region of the reference in the resource
    * 
    * @return the qualified name, or {@link Optional#empty()} if the reference cannot be resolved
    * @throws CancellationException if the user cancels the operation
    */
   private Optional<QualifiedName> findUnknownReference(XtextResource resource, ResourceSet resourceSet,
            Map<String, QualifiedName> references,
            ITextRegion region, Predicate<? super EObject> choiceFilter) {
      if (region.getLength() > 0) {
         IParseResult parseResult = resource.getParseResult();
         if (parseResult != null) {
            String completeText = parseResult.getRootNode().getText();
            String refText = completeText.substring(region.getOffset(),
               region.getOffset() + region.getLength());
            if (references.containsKey(refText)) {
               return Optional.of(references.get(refText));
            }
            Set<QualifiedName> possibleTypes = referenceResolver.findPossibleTypes(refText, resourceSet, choiceFilter);
            if (possibleTypes.isEmpty()) {
               return Optional.empty();
            }
            Optional<QualifiedName> name = selectReference(resource,
               possibleTypes,
               region);
            if (!name.isPresent()) {
               throw new CancellationException();
            }
            references.put(refText, name.get());
            return name;
         }
      }
      return Optional.empty();
   }

   /**
    * Selects the qualified name from the given set of possible qualified name
    * 
    * @param resource
    * @param possibleImports
    * @param region region of the import reference
    * @return the selected import or {@link Optional#empty()} if the user cancels the operation
    */
   private Optional<QualifiedName> selectReference(XtextResource resource, Set<QualifiedName> possibleImports,
            ITextRegion region) {
      if (possibleImports.isEmpty()) {
         return Optional.empty();
      } else if (possibleImports.size() == 1) {
         return Optional.of(possibleImports.iterator().next());
      } else {
         List<QualifiedName> choices = new ArrayList<>(possibleImports);
         OptionalInt choice = importChooser.select(choices, resource, region);
         if (choice.isPresent()) {
            return Optional.of(choices.get(choice.getAsInt()));
         } else {
            return Optional.empty();
         }
      }
   }

}
