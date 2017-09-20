package com.ngc.seaside.systemdescriptor.validation;

import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.resource.IContainer;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.resource.IResourceDescriptionsProvider;
import org.eclipse.xtext.validation.Check;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Import;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * This is a validator that ensures that imports correctly resolve to some
 * element.
 *
 */
public class ImportValidator extends AbstractSystemDescriptorValidator {

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

	@Check
	public void checkForMissingImports(Import i) {
		String fqn = i.getImportedNamespace();
		// Convert the string to a qualified name.
		QualifiedName name = qualifiedNameConverter.toQualifiedName(fqn);

		// Get descriptors for all resources.
		IResourceDescriptions descriptions = resourceDescriptionsProvider
				.getResourceDescriptions(i.eResource().getResourceSet());
		// Get all exported objects.  These are all objects that are declared.
		Iterable<IEObjectDescription> eobjectDescriptions = Iterables
				.concat(Iterables.transform(descriptions.getAllResourceDescriptions(), r -> r.getExportedObjects()));

		// Filter the objects for an object with the qualified name.
		// TODO TH: compare this usage with IContainer.Manager.  Not sure which to use in this case.
		eobjectDescriptions = Iterables.filter(eobjectDescriptions, e -> e.getQualifiedName().equals(name));

		// If the object is not resolved, declare an error.
		if (!eobjectDescriptions.iterator().hasNext()) {
			String msg = String.format("Could not resolve element '%s'.", fqn);
			error(msg, i, SystemDescriptorPackage.Literals.IMPORT__IMPORTED_NAMESPACE);
		}
	}
}
