package com.ngc.seaside.jellyfish.service.name.packagez.impl;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.service.name.MetadataNames;
import com.ngc.seaside.jellyfish.service.name.api.IPackageNamingService;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component(service = IPackageNamingService.class)
public class PackageNamingService implements IPackageNamingService {

   final static String MODEL_PARAMETER_NAME = "model";

   private ILogService logService;

   @Override
   public String getDomainPackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(data, "data may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      return (fqn + ".domain" + getPackageNameMinusCommonPart(fqn, data.getParent().getName())).toLowerCase();
   }

   @Override
   public String getEventPackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(data, "data may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      return (fqn + ".event" + getPackageNameMinusCommonPart(fqn, data.getParent().getName())).toLowerCase();
   }

   @Override
   public String getMessagePackageName(IJellyFishCommandOptions options, INamedChild<IPackage> data) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(data, "data may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      // Construct the name using the fully qualified model name then add the remaining package names of the data type.
      // We want to skip any common prefixes the model's package and data's package have in common.
      return (fqn + getPackageNameMinusCommonPart(fqn, data.getParent().getName())).toLowerCase();
   }

   @Override
   public String getConnectorPackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "data may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      // Construct the name using the fully qualified model name then add the remaining package names of the data type.
      // We want to skip any common prefixes the model's package and data's package have in common.
      return (fqn + ".connector" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Override
   public String getServiceInterfacePackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      return (fqn + ".api" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Override
   public String getServiceImplementationPackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      return (fqn + ".impl" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Override
   public String getServiceBaseImplementationPackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      return (fqn + ".base.impl" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Override
   public String getTransportTopicsPackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      return (fqn + ".transport.topic" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Override
   public String getDistributionPackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "data may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      // Construct the name using the fully qualified model name then add the remaining package names of the data type.
      // We want to skip any common prefixes the model's package and data's package have in common.
      return (fqn + ".distribution" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Override
   public String getCucumberTestsPackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "data may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      // Construct the name using the fully qualified model name then add the remaining package names of the data type.
      // We want to skip any common prefixes the model's package and data's package have in common.
      return (fqn + ".tests" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Override
   public String getCucumberTestsConfigPackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "data may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      // Construct the name using the fully qualified model name then add the remaining package names of the data type.
      // We want to skip any common prefixes the model's package and data's package have in common.
      return (fqn + ".testsconfig" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Override
   public String getConfigPackageName(IJellyFishCommandOptions options, IModel model) {
      Preconditions.checkNotNull(options, "options may not be null!");
      Preconditions.checkNotNull(model, "data may not be null!");
      // parts[0] = the package name of the model, parts[1] = the model's name and parts[3] = the fully qualified name
      String fqn = getModelNameAndPackage(options)[2];
      // Construct the name using the fully qualified model name then add the remaining package names of the data type.
      // We want to skip any common prefixes the model's package and data's package have in common.
      return (fqn + ".config" + getPackageNameMinusCommonPart(fqn, model.getParent().getName())).toLowerCase();
   }

   @Activate
   public void activate() {
      logService.debug(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC)
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   private static String[] getModelNameAndPackage(IJellyFishCommandOptions options) {
      Preconditions.checkArgument(options.getParameters().containsParameter(MODEL_PARAMETER_NAME),
                                  "the parameter '%s' is required!",
                                  MODEL_PARAMETER_NAME);
      String fullName = options.getParameters().getParameter(MODEL_PARAMETER_NAME).getStringValue();
      int lastPeriodPosition = fullName.lastIndexOf('.');
      Preconditions.checkArgument(lastPeriodPosition > 0, "model name is not in the correct format!");
      String prefix = fullName.substring(0, lastPeriodPosition);
      String unqualifiedName = fullName.substring(lastPeriodPosition + 1);
      unqualifiedName = MetadataNames.getModelAlias(options, fullName).orElse(unqualifiedName);
      return new String[]{
            prefix,
            unqualifiedName,
            prefix + "." + unqualifiedName
      };
   }

   private static String getPackageNameMinusCommonPart(String modelPackageName, String dataPackageName) {
      Preconditions.checkState(
            !modelPackageName.equals(dataPackageName),
            "modelPackageName and dataPackageName can't be equal when calling getPackageNameMinusCommonPart!");

      String[] modelParts = modelPackageName.split("\\.");
      String[] dataParts = dataPackageName.split("\\.");
      
      // Find the point in the package names where they begin to be different.
      int maxLen = Math.min(modelParts.length, dataParts.length);
      int positionOfFirstDiff = -1;
      for (int i = 0; i < maxLen; i++) {
         if (!modelParts[i].equals(dataParts[i])) {
            positionOfFirstDiff = i;
            break;
         }
      }

      String name;
      if (positionOfFirstDiff < 0) {
         // This means the data package is contained in the model package or the model package is contained in the data
         // package.  In this case, don't add anything.
         name = "";
      } else {
         name = "." + Arrays.asList(dataParts).subList(positionOfFirstDiff, dataParts.length).stream().collect(Collectors.joining("."));
      }
      return name;
   }
}
