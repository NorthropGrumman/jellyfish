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
