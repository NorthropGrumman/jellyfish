package com.ngc.seaside.systemdescriptor.tests.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.junit4.util.ResourceHelper;

public class ParsingTestResource {

	private final String source;
	private final Collection<ParsingTestResource> requiredResources = new ArrayList<>();

	private ParsingTestResource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public Collection<ParsingTestResource> getRequiredResources() {
		return requiredResources;
	}

	public Resource preparedForParse(ResourceHelper resourceHelper) throws Exception {
		return preparedForParse(resourceHelper, Arrays.asList(this));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ParsingTestResource)) {
			return false;
		}
		ParsingTestResource that = (ParsingTestResource) o;
		return Objects.equals(source, that.source);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source);
	}

	@Override
	public String toString() {
		return source;
	}

	public static ParsingTestResource resource(String source, ParsingTestResource... required) {
		ParsingTestResource r = new ParsingTestResource(source);
		for (ParsingTestResource requiredResource : required) {
			r.requiredResources.add(requiredResource);
		}
		return r;
	}

	public static Resource preparedForParse(ResourceHelper resourceHelper, Collection<ParsingTestResource> resources)
			throws Exception {
		if (resources.isEmpty()) {
			throw new IllegalArgumentException("resources can't be empty!");
		}
		
		Collection<ParsingTestResource> all = new HashSet<>(resources);
		for(ParsingTestResource r : resources) {
			all.addAll(r.getRequiredResources());
		}

		Iterator<ParsingTestResource> i = all.iterator();
		ParsingTestResource currentResource = i.next();
		
		Resource prepared = resourceHelper.resource(currentResource.getSource(), URI.createURI("root.sd"));
		while(i.hasNext()) {
			currentResource = i.next();
			resourceHelper.resource(currentResource.getSource(), prepared.getResourceSet());
		}
		
		return prepared;
	}
}
