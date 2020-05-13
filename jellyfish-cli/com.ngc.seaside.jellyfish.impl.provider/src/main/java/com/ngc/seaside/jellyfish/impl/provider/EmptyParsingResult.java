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
package com.ngc.seaside.jellyfish.impl.provider;

import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.api.IGherkinParsingResult;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IFeature;
import com.ngc.seaside.systemdescriptor.service.gherkin.model.api.IGherkinScenario;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class EmptyParsingResult implements IParsingResult, IGherkinParsingResult {

   public static final EmptyParsingResult UNSUCCESSFUL_INSTANCE = new EmptyParsingResult(false);
   
   public static final EmptyParsingResult SUCCESSFUL_INSTANCE = new EmptyParsingResult(true);

   private final boolean successful;
   
   private EmptyParsingResult(boolean successful) {
      this.successful = successful;
   }

   @Override
   public ISystemDescriptor getSystemDescriptor() {
      return null;
   }

   @Override
   public boolean isSuccessful() {
      return successful;
   }

   @Override
   public Collection<IParsingIssue> getIssues() {
      return Collections.emptyList();
   }

   @Override
   public Path getMainSourcesRoot() {
      return null;
   }

   @Override
   public Path getTestSourcesRoot() {
      return null;
   }

   @Override
   public Collection<IFeature> getFeatures() {
      return Collections.emptyList();
   }

   @Override
   public Optional<IFeature> findFeature(String fullyQualifiedName) {
      return Optional.empty();
   }

   @Override
   public Optional<IFeature> findFeature(String packageName, String featureName) {
      return Optional.empty();
   }

   @Override
   public Optional<IFeature> findFeature(IScenario scenario) {
      return Optional.empty();
   }

   @Override
   public Collection<IFeature> findFeatures(IModel model) {
      return Collections.emptyList();
   }

   @Override
   public Collection<IFeature> findFeatures(Collection<String> tags) {
      return Collections.emptyList();
   }

   @Override
   public Collection<IFeature> findFeatures(String tag, String... tags) {
      return Collections.emptyList();
   }

   @Override
   public Collection<IGherkinScenario> findScenarios(Collection<String> tags) {
      return Collections.emptyList();
   }

   @Override
   public Collection<IGherkinScenario> findScenarios(String tag, String... tags) {
      return Collections.emptyList();
   }
}
