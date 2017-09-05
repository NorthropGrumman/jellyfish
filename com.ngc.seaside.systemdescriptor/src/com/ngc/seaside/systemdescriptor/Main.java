package com.ngc.seaside.systemdescriptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Element;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Package;

public class Main {

	private static final Path SD_SOURCE_PATH = Paths.get("src", "main", "sd");
	
	private final Injector injector;
	
	public static void main(String[] args) {
		new Main(args);
	}
	
	private Main(String[] args) {
		Preconditions.checkArgument(args.length > 0, "please supply a SD project directory or list of SD files!");
		
		Collection<File> files = new ArrayList<>();
		for(String arg : args) {
			Path path = Paths.get(arg);
			if(Files.isDirectory(path)) {
				files.addAll(findFiles(path));
			} else if(Files.exists(path)) {
				files.add(path.toAbsolutePath().toFile());
			} else {
				System.err.format("%s does not exist!%n", path);
			}
		}
		
		//new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("cry-text3");
		injector = new SystemDescriptorStandaloneSetup().createInjectorAndDoEMFRegistration();
		
		printIssues(getIssues(files));
	}
	
	private static Collection<File> findFiles(Path projectDirectory) {
		Path src = projectDirectory.resolve(SD_SOURCE_PATH);
		Preconditions.checkArgument(Files.isDirectory(src), "%s is not a directory or does not exist!", src);
		
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.sd");
        Collection<File> sdFiles;
		try {
			sdFiles = Files.find(
			      src,
			      Integer.MAX_VALUE,
			      (path, basicFileAttributes) -> matcher.matches(path.getFileName()))
				  .map(p -> p.toAbsolutePath().toFile())
			      .collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
		System.out.format("Found %d SD files:%n", sdFiles.size());
		sdFiles.forEach(f -> System.out.println(f.getAbsolutePath()));
        return sdFiles;
	}
	
	private Collection<Issue> getIssues(Collection<File> files) {
		XtextResourceSet resourceSet = injector.getProvider(XtextResourceSet.class).get();
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		
		Collection<XtextResource> resources = new ArrayList<>(files.size());
		for(File f : files) {
			resources.add((XtextResource) resourceSet.getResource(URI.createFileURI(f.getAbsolutePath()), true));
		}
		
		Collection<Issue> issues = new ArrayList<>();
		for(XtextResource resource : resources) {
			printContents(resource);
			
			IResourceValidator validator = resource.getResourceServiceProvider().getResourceValidator();
			issues.addAll(validator.validate(resource, CheckMode.ALL, null));
		}
		
		return issues;
	}
	
	private Collection<File> orderResources(Collection<File> files) {
		XtextResourceSet resourceSet = injector.getProvider(XtextResourceSet.class).get();
		resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		
		Collection<File> dataFiles = new ArrayList<>();
		Collection<File> modelFiles = new ArrayList<>();
		Collection<File> enumFiles = new ArrayList<>();
		Collection<File> everythingElse = new ArrayList<>();
		
		for(File f : files) {
			XtextResource r = (XtextResource) resourceSet.getResource(URI.createFileURI(f.getAbsolutePath()), true);
			if(r.getContents().isEmpty()) {
				everythingElse.add(f);
			} else {
				EObject object = r.getContents().get(0);
				if(object instanceof Package) {
					Element element = ((Package) object).getElement();
					if(element instanceof Enumeration) {
						enumFiles.add(f);
					} else if(element instanceof Data) {
						dataFiles.add(f);
					} else if(element instanceof Model) {
						modelFiles.add(f);
					} else {
						everythingElse.add(f);
					}
				} else {
					everythingElse.add(f);
				}
			}
		}
		// Throw the parsed files away.
		resourceSet.getResources().clear();
		
		Collection<File> ordered = new ArrayList<>(files.size());
		ordered.addAll(enumFiles);
		ordered.addAll(dataFiles);
		ordered.addAll(modelFiles);
		ordered.addAll(everythingElse);
		return ordered;
	}
	
	private static void printIssues(Collection<Issue> issues) {
		System.out.format("Found %d issues:%n", issues.size());
		issues.forEach(i -> System.out.format("%s: %s: line %d - %s%n", i.getSeverity(), i.getUriToProblem(), i.getLineNumber(), i.getMessage()));
	}
	
	private static void printContents(XtextResource resource) {
		System.out.println("Contents of " + resource.getURI());
		for(EObject object : resource.getContents()) {
			System.out.println(object);
			if(object instanceof Package) {
				Element element = ((Package)object).getElement();
				System.out.println("element = " + element);
			}
		}
		System.out.println("----");
	}
}
