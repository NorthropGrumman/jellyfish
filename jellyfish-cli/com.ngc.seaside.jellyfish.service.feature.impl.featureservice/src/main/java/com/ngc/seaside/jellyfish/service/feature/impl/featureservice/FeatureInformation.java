package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.google.common.base.Preconditions;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FeatureInformation implements IFeatureInformation {

   private final Path path;
   private final IModel model;
   private final IScenario scenario;

   /**
    * Constructor.
    * 
    * @param path path to feature file
    * @param model model, may be null
    * @param scenario scenario, may be null
    */
   public FeatureInformation(Path path, IModel model, IScenario scenario) {
      Preconditions.checkNotNull(path, "path cannot be null");
      Preconditions.checkArgument(Files.isRegularFile(path), "Path " + path + " does not exist");
      Preconditions.checkArgument(path.getFileName().toString().endsWith(".feature"),
               "File " + path + " is not a feature file");
      this.path = path;
      this.model = model;
      this.scenario = scenario;
   }

   @Override
   public Path getPath() {
      return path;
   }

   @Override
   public String getFullyQualifiedName() {
      String name = getScenario()
               .map(scenario -> scenario.getParent().getFullyQualifiedName() + "." + scenario.getName() + ".feature")
               .orElse(null);
      if (name != null) {
         return name;
      }
      return getPath().toAbsolutePath().toString();
   }

   @Override
   public Optional<IModel> getModel() {
      return Optional.ofNullable(model);
   }

   @Override
   public Optional<IScenario> getScenario() {
      return Optional.ofNullable(scenario);
   }

   @Override
   public String toString() {
      return "Feature[path=" + path + ",model=" + getModel().map(IModel::getFullyQualifiedName).orElse("null")
               + ",scenario=" + getScenario().map(IScenario::getName).orElse("null") + "]";
   }

}
