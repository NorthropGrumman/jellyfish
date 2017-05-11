package com.ngc.seaside.systemdescriptor.validation;

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.validation.Check;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

public class DuplicateNameValidator extends AbstractSystemDescriptorValidator {

	@Inject
	private IResourceServiceProvider.Registry resourceServiceProviderRegistry = IResourceServiceProvider.Registry.INSTANCE;

//	@Check
//	public void checkForDuplicateData(Data data) {
//		Package p = (Package) data.eContainer();
//		// getCon
//		// if(p.get)
//	}

	@Check
	public void checkForDuplicateDataDeclarations(Data data) {
		// Note we can speed up this check.  See the impl of
		// NamesAreUniqueValidator
		
		/*
		Multimap<String, String> dataNamesByPackage = ArrayListMultimap.create();
		
		for(Resource resource : data.eResource().getResourceSet().getResources()) {
			for(IEObjectDescription desc : getResourceDescriptions(resource)) {
				// Is the resource a data element?
				if(SystemDescriptorPackage.Literals.DATA.equals(desc.getEClass())) {
					// Get the package that contains the data.
					Data dataInPackage = (Data) desc.getEObjectOrProxy();
					Package p = (Package) dataInPackage.eContainer();
					
					// TODO TH: implement this later.
					
					//if(dataNamesByPackage.containsEntry(p.getName(), ))
				}
			}
		}
		*/
	}

	private Iterable<IEObjectDescription> getResourceDescriptions(Resource resource) {
		Iterable<IEObjectDescription> descriptions = Collections.emptyList();

		IResourceServiceProvider resourceServiceProvider = resourceServiceProviderRegistry
				.getResourceServiceProvider(resource.getURI());

		if (resourceServiceProvider != null) {
			IResourceDescription.Manager manager = resourceServiceProvider.getResourceDescriptionManager();
			if (manager != null) {
				IResourceDescription description = manager.getResourceDescription(resource);
				if (description != null) {
					descriptions =  description.getExportedObjects();
				}
			}
		}
		
		return descriptions;
	}
}
