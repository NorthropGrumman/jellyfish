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
      return getPackageName(model, "pubsubbridge");
   }

}
