package com.ngc.seaside.systemdescriptor.ui.dynamic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.ui.internal.SystemdescriptorActivator;

/**
 * This is a simple class that will install bundles in a certain directory into
 * Eclipse. The directory name is configured with the system property
 * {@code com.ngc.seaside.jellyfish.extraBundles}. This should point to a
 * directory that contains bundle JAR files that should be installed when
 * Eclipse starts.
 * 
 * <p/>
 * 
 * This is useful when developing the Eclipse product and running Eclipse with
 * JellyFish within Eclipse. This can be used to include any JellyFish extension
 * bundles when debugging Eclipse. For example, if you
 * 
 * <ol>
 * <li>clone the jellyfish-systemdescriptor-ext repo to the location
 * C:\projects\ceacide\jellyfish-systemdescriptor-ext</li>
 * <li>run {@code gradle build}</li>
 * <li>Set the value
 * {@code -Dcom.ngc.seaside.jellyfish.extraBundles=C:\projects\ceacide\jellyfish-systemdescriptor-ext\com.ngc.seaside.systemdescriptor.ext.updatesite\build\\updatesite\plugins}
 * as a VM argument inside the Eclipse configuration
 * </ol>
 * 
 * then Eclipse will start with the extension bundles installed.
 *
 */
public class ExtensionsBundlesDeployer {

	/**
	 * The name of the system property that points to a directary that contains
	 * extra bundles to deploy when starting Eclipse.
	 */
	private final static String BUNDLE_DIRECTORY_PROPERY_NAME = "com.ngc.seaside.jellyfish.extraBundles";
	private final static Logger LOGGER = Logger.getLogger(ExtensionsBundlesDeployer.class);

	private Path bundleDirectory;

	public boolean useSystemArgumentForBundleLocation() {
		String bundleDirectoryName = System.getProperty(BUNDLE_DIRECTORY_PROPERY_NAME);
		boolean valid = bundleDirectoryName != null && !bundleDirectoryName.trim().isEmpty();
		if (valid) {
			bundleDirectory = Paths.get(bundleDirectoryName);
			valid = bundleDirectory.toFile().isDirectory();
			if (!valid) {
				LOGGER.warn(String.format(
						"System property %s does not point to a directory, extra bundles will not be installed.",
						BUNDLE_DIRECTORY_PROPERY_NAME));
			}
		} else {
			LOGGER.info(String.format("System property %s not set, extra bundles will not be installed.",
					BUNDLE_DIRECTORY_PROPERY_NAME));
		}
		return valid;
	}

	public void installExtraBundles() {
		Preconditions.checkState(bundleDirectory != null, "bundle directory not set!");
		Preconditions.checkState(bundleDirectory.toFile().isDirectory(), "%s is not a directory!", bundleDirectory);

		try {
			// Install all JAR files that are in the extra bundles directory.
			// Only search to a depth of 100.
			Files.find(bundleDirectory, 100, (file, attributes) -> file.toFile().getName().endsWith(".jar"))
					.forEach(this::installBundle);
		} catch (IOException e) {
			LOGGER.error(String.format("Exception while searching for bundles in directory %s!", bundleDirectory), e);
		}

	}

	private void installBundle(Path bundle) {
		BundleContext bundleContext = SystemdescriptorActivator.getInstance().getBundle().getBundleContext();
		File bundleFile = bundle.toFile();

		// OSGi requires the bundle location to take the form "file://" + path.
		// Windows note: in Windows, getAbsolutePath returns a path like
		// C:\Whatever
		// OSGi wants standard Unix paths, we will replace the separator with
		// '/'. Also, we want to ensure OSGi knows the path is absolute, so we
		// make sure it has an '/' in front. Get the absolute path in the manner
		// below to ensure it is correct.
		String path = new File(bundleDirectory.toFile(), bundleFile.getName()).getAbsolutePath().replaceAll("\\\\",
				"/");
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		path = "file://" + path;

		LOGGER.info(String.format("Installing extra bundle %s.", path));
		try {
			// Just install the bundle. There is no need to start it yet. If the
			// bundle has a META-INF/services/com.google.inject.Module service
			// loader file, the DynamicModuleLoader will start the bundle before
			// loading the module class.
			bundleContext.installBundle(path);
		} catch (BundleException e) {
			LOGGER.error(String.format("Error while installing bundle %s.", path), e);
		}
	}

}
