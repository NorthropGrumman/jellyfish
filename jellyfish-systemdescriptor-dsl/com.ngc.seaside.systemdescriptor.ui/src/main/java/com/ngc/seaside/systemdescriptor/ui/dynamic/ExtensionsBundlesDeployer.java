/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.ui.dynamic;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.ui.internal.SystemdescriptorActivator;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is a simple class that will install bundles in a certain directory into
 * Eclipse. Using the system property or environment variable
 * {@code JELLYFISH_USER_HOME}, bundles within the directory {@code JELLYFISH_USER_HOME/plugins} will be installed
 * when Eclipse starts. 
 * <p/>
 * This is useful when developing the Eclipse product and running Eclipse with
 * JellyFish within Eclipse. This can be used to include any JellyFish extension
 * bundles when debugging Eclipse.
 */
public class ExtensionsBundlesDeployer {

   /**
    * The name of the system property that points to a directory for jellyfish settings. Extra bundles should be added
    * to the {@code plugins} subdirectory.
    */
   public static final String JELLYFISH_USER_HOME = "JELLYFISH_USER_HOME";

   private static final String USER_HOME_PROPERTY_NAME = "user.home";
   private static final String DEFAULT_JELLYFISH_USER_HOME = ".jellyfish";
   private static final String PLUGINS_DIRECTORY = "plugins";

   private static final Logger LOGGER = Logger.getLogger(ExtensionsBundlesDeployer.class);

   /**
    * Uses the system property {@code BUNDLE_DIRECTORY_PROPERY_NAME} to find the bundles to load at runtime.
    */
   public boolean useSystemArgumentForBundleLocation() {
      Path bundleDirectory = getPluginsDirectory();
      
      if (Files.isDirectory(bundleDirectory)) {
         return true;
      }
      LOGGER.info(String.format("%s not set, extra bundles will not be installed.",
               JELLYFISH_USER_HOME));
      return false;
   }

   /**
    * Installs the bundles that are located in the directory given by the system property
    * {@code BUNDLE_DIRECTORY_PROPERY_NAME}.
    */
   public void installExtraBundles() {
      Path bundleDirectory = getPluginsDirectory();
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
      Path bundleDirectory = getPluginsDirectory();
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

   private Path getPluginsDirectory() {
      String jellyfishUserHome = System.getProperty(JELLYFISH_USER_HOME, System.getenv(JELLYFISH_USER_HOME));
      Path jellyfishUserHomePath = jellyfishUserHome == null
               ? Paths.get(System.getProperty(USER_HOME_PROPERTY_NAME), DEFAULT_JELLYFISH_USER_HOME)
               : Paths.get(jellyfishUserHome);
      return jellyfishUserHomePath.resolve(PLUGINS_DIRECTORY);
   }
}
