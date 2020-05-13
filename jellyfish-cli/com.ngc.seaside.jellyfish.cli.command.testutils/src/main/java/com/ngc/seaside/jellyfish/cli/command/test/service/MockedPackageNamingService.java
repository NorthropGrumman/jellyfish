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
package com.ngc.seaside.jellyfish.cli.command.test.service;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

/**
 * A basic implementation of {@link IPackageNamingService} for tests
 */
public class MockedPackageNamingService implements IPackageNamingService {

   private final String format = "%s.%s.%s";

   private String getPackageName(INamedChild<IPackage> element, String packageType) {
      return String
            .format(format, element.getParent().getName().toLowerCase(), element.getName().toLowerCase(), packageType);
   }

   @Override
   public String getDomainPackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      return getPackageName(data, "domain");
   }

   @Override
   public String getEventPackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      return getPackageName(data, "event");
   }

   @Override
   public String getMessagePackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      return getPackageName(data, "message");
   }

   @Override
   public String getConnectorPackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "connector");
   }

   @Override
   public String getServiceInterfacePackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "api");
   }

   @Override
   public String getServiceImplementationPackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "impl");
   }

   @Override
   public String getServiceBaseImplementationPackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "base");
   }

   @Override
   public String getTransportTopicsPackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "topics");
   }

   @Override
   public String getDistributionPackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "dist");
   }

   @Override
   public String getCucumberTestsPackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "tests");
   }

   @Override
   public String getCucumberTestsConfigPackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "testsconfig");
   }

   @Override
   public String getConfigPackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "config");
   }

   @Override
   public String getPubSubBridgePackageName(IJellyFishCommandOptions options, IModel model) {
      return getPackageName(model, "bridge.pubsub");
   }

}
