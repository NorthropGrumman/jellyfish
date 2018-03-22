// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=375027

/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.ngc.seaside.systemdescriptor.tests.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.IResourceFactory;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.StringInputStream;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * Some convenience methods for parsing (i.e. testing, etc.) 
 * 
 * @author Sven Efftinge - Initial contribution and API
 */
public class ParseHelper<T extends EObject> {
    
    public final static String EXTENSION = "org.eclipse.xtext.junit4.util.ParseHelper.extension";
	
	@Inject
	private Provider<XtextResourceSet> resourceSetProvider;
	
	@Inject
	private IResourceFactory resourceFactory;

	@Inject
	private IResourceValidator validator;
	
    @Inject(optional = true)
    @Named(EXTENSION)
    private String extension = "uri";
    
    /**
     * Call this before any <code>parse()</code> method to set the file extension
     * of your DSL or the validation will not work correctly.
     */
    public void setExtension( String extension ) {
        this.extension = extension;
    }
	
	public T parse(InputStream in, URI uriToUse, Map<?,?> options, ResourceSet resourceSet) {
		Resource resource = resourceFactory.createResource(uriToUse);
		resourceSet.getResources().add(resource);
		try {
			resource.load(in, options);
        } catch (IOException e) {
            throw new WrappedException(e);
        }
		
	    @SuppressWarnings("unchecked")
		final T root = (T) (resource.getContents().isEmpty()? null : resource.getContents().get(0));
		
		if(null == root) {
            validate(resource);
        }
		
		return root;
	}
	
	public T parse(CharSequence text) throws Exception {
		return parse(text, resourceSetProvider.get());
	}
	
	public T parse(CharSequence text, URI uriToUse) throws Exception {
	    return parse(getAsStream(text), uriToUse, null, resourceSetProvider.get());
	}
	
	public T parse(CharSequence text, ResourceSet resourceSetToUse) throws Exception {
		return parse(getAsStream(text), computeUnusedUri(resourceSetToUse), null, resourceSetToUse);
	}

    public void validate(T model) {
        validate(model.eResource());
    }
        
    public void validate(Resource resource) {
        List<Issue> issues = validator.validate(resource, CheckMode.ALL, null);
        if(!issues.isEmpty()) {
            throw new XtextParseError(issues);
        }
    }
	
	protected URI computeUnusedUri(ResourceSet resourceSet) {
		String name = "__synthetic";
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			URI syntheticUri = URI.createURI(name+i+"."+extension);
			if (resourceSet.getResource(syntheticUri, false)==null)
				return syntheticUri;
		}
		throw new IllegalStateException();
	}

	protected InputStream getAsStream(CharSequence text) {
		return new StringInputStream(text == null ? "" : text.toString());
	}
}