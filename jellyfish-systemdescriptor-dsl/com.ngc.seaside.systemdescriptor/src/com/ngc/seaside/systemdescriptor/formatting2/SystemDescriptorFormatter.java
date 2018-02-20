package com.ngc.seaside.systemdescriptor.formatting2;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.formatting2.AbstractFormatter2;
import org.eclipse.xtext.formatting2.FormatterRequest;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IFormatter2;
import org.eclipse.xtext.formatting2.regionaccess.ITextReplacement;
import org.eclipse.xtext.resource.XtextResource;

import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

/**
 * The System Descriptor formatter. This formatter delegates to other more
 * specific implementations based on the element that is being formatted. This
 * formatter requires all delegates to extend
 * {@link AbstractSystemDescriptorFormatter}.
 */
public class SystemDescriptorFormatter implements IFormatter2 {

	private final static Logger LOGGER = Logger.getLogger(SystemDescriptorFormatter.class);

	/**
	 * Contains all formatters that have been registered. These formatters are
	 * keyed by the EClass literal they can format. This map is unmodifiable
	 * once the constructor completes.
	 */
	private Map<EClass, AbstractSystemDescriptorFormatter> formatters;

	/**
	 * The current formatting request.
	 */
	private FormatterRequest request;

	public SystemDescriptorFormatter() {
		formatters = new HashMap<>();

		// Register all formatters here. Note it is possible to register a
		// single formatter to handle multiple types.
		registerFormatter(new MetadataFormatter(),
				SystemDescriptorPackage.Literals.METADATA,
				SystemDescriptorPackage.Literals.JSON_OBJECT);
		registerFormatter(new PackageFormatter(), SystemDescriptorPackage.Literals.PACKAGE);
		registerFormatter(new ElementFormatter(), SystemDescriptorPackage.Literals.ELEMENT);
		registerFormatter(new DataFormatter(), SystemDescriptorPackage.Literals.DATA);
		registerFormatter(new EnumerationFormatter(), SystemDescriptorPackage.Literals.ENUMERATION);
		registerFormatter(new ModelFormatter(), SystemDescriptorPackage.Literals.MODEL);
		registerFormatter(new DataFieldDeclarationFormatter(), 
				SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION, 
				SystemDescriptorPackage.Literals.PRIMITIVE_DATA_FIELD_DECLARATION, 
				SystemDescriptorPackage.Literals.REFERENCED_DATA_MODEL_FIELD_DECLARATION);
		// End formatter registration.

		formatters = Collections.unmodifiableMap(formatters);
	}

	@Override
	public List<ITextReplacement> format(FormatterRequest request) {
		this.request = request;
		// This list effectively contains the formatted document.
		List<ITextReplacement> replacements = Collections.emptyList();

		try {
			// The resource that should be formatted.
			XtextResource resource = request.getTextRegionAccess().getResource();
			// Dig out the EObject that should be formatted.
			List<EObject> contents = resource.getContents();
			if (!contents.isEmpty()) {
				EObject model = contents.get(0);
				// Find the formatter for the element and perform the
				// formatting.
				for (EClass cls : model.eClass().getEAllSuperTypes()) {
					AbstractFormatter2 f = formatters.get(cls);
					if (f != null) {
						replacements = f.format(request);
					}
				}
				AbstractFormatter2 f = formatters.get(model.eClass());
				if (f != null) {
					replacements = f.format(request);
				} else {
					// This means we have defined a grammar rule but have not
					// created a formatter to format the rule.
					LOGGER.warn("No formatter register to handle objects of type " + model.eClass().getName() + ".");
				}
			}		
		} finally {
			this.request = null;
		}

		return replacements;
	}

	/**
	 * Invoked by {@code AbstractSystemDescriptorFormatter} to continue a chain
	 * of formatting calls. This method is only invoked during the execution of
	 * {@link #format(FormatterRequest)}.
	 * 
	 * @param object
	 * @param document
	 */
	protected void format(Object object, IFormattableDocument document) {
		if (object instanceof EObject) {
			EObject eobj = (EObject) object;
			for (EClass cls : eobj.eClass().getEAllSuperTypes()) {
				AbstractSystemDescriptorFormatter f = formatters.get(cls);
				if (f != null) {
					f.initalize(request);
					try {
						f.format(eobj, document);
					} finally {
						f.reset();
					}
				}
			}
			AbstractSystemDescriptorFormatter f = formatters.get(eobj.eClass());
			if (f != null) {
				// Necessary to avoid NPEs due to how AbstractFormatter2 is
				// setup.
				f.initalize(request);
				try {
					f.format(eobj, document);
				} finally {
					f.reset();
				}
			}
		}
	}

	/**
	 * Registers the given formatter with the specific types of elements
	 * identified by the {@code EClass}es.
	 */
	protected void registerFormatter(AbstractSystemDescriptorFormatter formatter, EClass clazz, EClass... clazzes) {
		formatter.setRootFormatter(this);
		formatters.put(clazz, formatter);
		if (clazzes != null) {
			for (EClass c : clazzes) {
				formatters.put(c, formatter);
			}
		}
	}
}
