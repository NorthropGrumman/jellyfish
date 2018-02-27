package com.ngc.seaside.systemdescriptor.formatting2;

import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.formatting2.AbstractFormatter2;
import org.eclipse.xtext.formatting2.FormatterRequest;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.IFormatter2;
import org.eclipse.xtext.formatting2.regionaccess.ITextReplacement;
import org.eclipse.xtext.resource.XtextResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		registerFormatter(new PackageFormatter(), SystemDescriptorPackage.Literals.PACKAGE);
		registerFormatter(new ModelFormatter(), SystemDescriptorPackage.Literals.MODEL);
		registerFormatter(new DataFormatter(), SystemDescriptorPackage.Literals.DATA);
		registerFormatter(new DeclarationDefinitionFormatter(),
				SystemDescriptorPackage.Literals.DECLARATION_DEFINITION);
		registerFormatter(new MetadataFormatter(),
				SystemDescriptorPackage.Literals.METADATA,
				SystemDescriptorPackage.Literals.JSON_OBJECT,
				SystemDescriptorPackage.Literals.MEMBER,
				SystemDescriptorPackage.Literals.JSON_VALUE,
				SystemDescriptorPackage.Literals.ARRAY_VALUE);
		registerFormatter(new EnumerationFormatter(),
				SystemDescriptorPackage.Literals.ENUMERATION,
				SystemDescriptorPackage.Literals.ENUMERATION_VALUE_DECLARATION);
		registerFormatter(new DataFieldDeclarationFormatter(),
				SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION,
				SystemDescriptorPackage.Literals.PRIMITIVE_DATA_FIELD_DECLARATION,
				SystemDescriptorPackage.Literals.REFERENCED_DATA_MODEL_FIELD_DECLARATION);
		registerFormatter(new InputFormatter(),
				SystemDescriptorPackage.Literals.INPUT,
				SystemDescriptorPackage.Literals.INPUT_DECLARATION);
		registerFormatter(new OutputFormatter(),
				SystemDescriptorPackage.Literals.OUTPUT,
				SystemDescriptorPackage.Literals.OUTPUT_DECLARATION);
		registerFormatter(new PartsFormatter(),
				SystemDescriptorPackage.Literals.PARTS,
				SystemDescriptorPackage.Literals.PART_DECLARATION);
		registerFormatter(new RequiresFormatter(),
				SystemDescriptorPackage.Literals.REQUIRES,
				SystemDescriptorPackage.Literals.REQUIRE_DECLARATION);
		registerFormatter(new LinksFormatter(),
				SystemDescriptorPackage.Literals.LINKS,
				SystemDescriptorPackage.Literals.LINK_DECLARATION);
		registerFormatter(new ScenarioFormatter(),
				SystemDescriptorPackage.Literals.SCENARIO,
				SystemDescriptorPackage.Literals.GIVEN_DECLARATION,
				SystemDescriptorPackage.Literals.GIVEN_STEP,
				SystemDescriptorPackage.Literals.WHEN_DECLARATION,
				SystemDescriptorPackage.Literals.WHEN_STEP,
				SystemDescriptorPackage.Literals.THEN_DECLARATION);
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
				replacements = doFormat(model);
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
			doFormat((EObject) object, document);
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

	private List<ITextReplacement> doFormat(EObject object) {
		List<ITextReplacement> replacements = new ArrayList<>();

		// Find the formatters for the element's types and perform the
		// formatting.
		for (EClass clazz : getTypesOf(object)) {
			AbstractSystemDescriptorFormatter f = formatters.get(clazz);
			if (f != null) {
				replacements.addAll(f.format(request));
			}
		}

		return replacements;
	}

	private void doFormat(EObject object, IFormattableDocument document) {
		for (EClass clazz : getTypesOf(object)) {
			AbstractSystemDescriptorFormatter f = formatters.get(clazz);
			if (f != null) {
				// Necessary to avoid NPEs due to how AbstractFormatter2 is
				// setup.
				f.initalize(request);
				try {
					f.format(object, document);
				} finally {
					f.reset();
				}
			}
		}
	}

	/**
	 * Gets all the types of the given object.
	 * 
	 * @param object
	 *            the object to get the types for
	 * @return the types of the object
	 */
	private static Set<EClass> getTypesOf(EObject object) {
		// Build a set that contains all the types of the given object.
		Set<EClass> clazzes = new LinkedHashSet<>(object.eClass().getEAllSuperTypes());
		// We need the add the actual type to the set because
		// getEAllSuperTypes() just returns the super types, not the actual
		// concrete type of the object.
		clazzes.add(object.eClass());
		return clazzes;
	}
}
