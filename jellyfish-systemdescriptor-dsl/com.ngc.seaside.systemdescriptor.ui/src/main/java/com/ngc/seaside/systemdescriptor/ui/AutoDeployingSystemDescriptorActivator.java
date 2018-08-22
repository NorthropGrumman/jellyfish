package com.ngc.seaside.systemdescriptor.ui;

import com.ngc.seaside.systemdescriptor.ui.dynamic.ExtensionsBundlesDeployer;
import com.ngc.seaside.systemdescriptor.ui.internal.SystemdescriptorActivator;

import org.osgi.framework.BundleContext;

/**
 * The activator for the System Descriptor UI bundle.  This activator differs from the stock activator in that it
 * installs any extra bundles into Eclipse when the UI bundle is activated.
 * 
 * @see ExtensionsBundlesDeployer
 */
public class AutoDeployingSystemDescriptorActivator extends SystemdescriptorActivator {

   @Override
   public void start(BundleContext context) throws Exception {
      super.start(context);
      // Install any extra bundles given by the system property (if it is set).
      // This provides an easy way for users to get their own verbs installed
      // without an update site. See the Javadoc for ExtensionsBundlesDeployer for more details.
      ExtensionsBundlesDeployer extensions = new ExtensionsBundlesDeployer();
      if (extensions.useSystemArgumentForBundleLocation()) {
         extensions.installExtraBundles();
      }
   }

}
