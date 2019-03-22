/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
