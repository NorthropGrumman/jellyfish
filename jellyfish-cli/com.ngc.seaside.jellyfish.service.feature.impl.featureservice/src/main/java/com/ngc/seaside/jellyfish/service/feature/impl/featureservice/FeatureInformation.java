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
