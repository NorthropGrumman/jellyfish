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
package com.ngc.seaside.jellyfish.utilities.command;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.io.Closeables;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A base class for Jellyfish commands that perform one or more analysis.  Classes that extend this class typically
 * override {@link #analyzeModel(IModel)}, {@link #analyzeData(IData)}, or {@link #analyzeEnumeration(IEnumeration)}.
 * Command perform whatever analysis is necessary during this callbacks.  Additionally, a command can implement
 * {@link #postAnalysis()} which is invoked after the project has been scanned and after all other callbacks have been
 * issued.
 *
 * <p/> If no other parameters are provided when this command is executed, it issue callbacks for all elements in the
 * entire System Descriptor project.  This means that, by default, most commands will be executed on an entire project
 * unless otherwise configured.  If the {@link CommonParameters#MODEL model} parameter is supplied, {@code analyzeModel}
 * will be invoked only for that model.  If the {@link CommonParameters#STEREOTYPES stereotypes} parameter is supplied,
 * {@code analyzeModel} will only be invoked on models that contain the provided stereotypes.
 */
public abstract class AbstractJellyfishAnalysisCommand extends AbstractJellyfishCommand {

   /**
    * Used to report findings.
    */
   protected IAnalysisService analysisService;

   /**
    * Used to retrieve source information about a finding.
    */
   protected ISourceLocatorService sourceLocatorService;

   /**
    * True if this command has reported at least one finding.
    */
   private boolean findingsReported = false;

   /**
    * Creates a new analysis command with the given name.
    *
    * @param name the name of the command.
    */
   protected AbstractJellyfishAnalysisCommand(String name) {
      super(name);
   }

   /**
    * Sets the analysis service.
    */
   public void setAnalysisService(IAnalysisService ref) {
      this.analysisService = ref;
   }

   /**
    * Removes the analysis service.
    */
   public void removeAnalysisService(IAnalysisService ref) {
      setAnalysisService(null);
   }

   /**
    * Sets the source locator service.
    */
   public void setSourceLocatorService(ISourceLocatorService ref) {
      this.sourceLocatorService = ref;
   }

   /**
    * Removes the source locator service.
    */
   public void removeSourceLocatorService(ISourceLocatorService ref) {
      setSourceLocatorService(null);
   }

   @Override
   protected void doRun() {
      // Analysis commands have 3 modes of operation:
      // 1) A specific model was specified with the "model" argument.  This means only scan the given model.
      // 2) Scan only models which match the given stereotypes if the "stereotypes" argument is used.
      // 3) Scan every element in the project if none of the above options where used.

      // Keep track of how long analysis takes.  Its pretty easy to write an analysis that takes a noticeable amount of
      // time to complete.  This makes tracking that down easier.
      Stopwatch sw = Stopwatch.createStarted();

      preAnalysis();
      if (getOptions().getParameters().containsParameter(CommonParameters.MODEL.getName())) {
         analyzeModel(getModel());
      } else if (getOptions().getParameters().containsParameter(CommonParameters.STEREOTYPES.getName())) {
         analyzeStereotypedModels();
      } else {
         analyzeEntireProject();
      }
      postAnalysis();

      long elapsed = sw.elapsed(TimeUnit.MILLISECONDS);
      logService.debug(getClass(), "Analysis completed in %s.", DurationFormatUtils.formatDurationHMS(elapsed));
   }

   /**
    * Invoked when the command should only analyze models with a specified stereotype.  This method is invoked when
    * the {@link CommonParameters#STEREOTYPES stereotypes} parameter is used to invoke this command.  The default
    * implementation calls {@link #analyzeModel(IModel)} on each model that contains the stereotype.
    */
   protected void analyzeStereotypedModels() {
      String[] stereotypes = getOptions().getParameters()
            .getParameter(CommonParameters.STEREOTYPES.getName())
            .getStringValue()
            .split(",");
      Collection<IModel> models = Traversals.collectModels(
            getOptions().getSystemDescriptor(),
            ModelPredicates.withAnyStereotype(Arrays.asList(stereotypes)));
      models.forEach(this::analyzeModel);
   }

   /**
    * Invoked when the command should analyze an entire project.  This method is invoked when the command is invoked
    * without any additional parameters.  The default implementation calls {@link #analyzeModel(IModel)},
    * {@link #analyzeData(IData)}, and {@link #analyzeEnumeration(IEnumeration)} for every element in the project.
    */
   protected void analyzeEntireProject() {
      for (IPackage packagez : getOptions().getSystemDescriptor().getPackages()) {
         analyzePackage(packagez);
         packagez.getModels().forEach(this::analyzeModel);
         packagez.getData().forEach(this::analyzeData);
         packagez.getEnumerations().forEach(this::analyzeEnumeration);
      }
   }

   /**
    * Invoked to analyze a package.  The default implementation does nothing.  Extenders of this class will override
    * this method to perform analysis and report findings via {@link #reportFinding(SystemDescriptorFinding)}.
    */
   protected void analyzePackage(IPackage pkg) {
   }
   
   /**
    * Invoked to analyze a model.  The default implementation does nothing.  Extenders of this class will override this
    * method to perform analysis and report findings via {@link #reportFinding(SystemDescriptorFinding)}.
    */
   protected void analyzeModel(IModel model) {
   }

   /**
    * Invoked to analyze a data structure.  The default implementation does nothing.  Extenders of this class will
    * override this method to perform analysis and report findings via {@link #reportFinding(SystemDescriptorFinding)}.
    */
   protected void analyzeData(IData data) {
   }

   /**
    * Invoked to analyze an enumeration.  The default implementation does nothing.  Extenders of this class will
    * override this method to perform analysis and report findings via {@link #reportFinding(SystemDescriptorFinding)}.
    */
   protected void analyzeEnumeration(IEnumeration enumeration) {
   }

   /**
    * Invoked when before any analysis start.  The default implementation does nothing.  Extenders of this class can
    * use this method to perform any setup.
    */
   protected void preAnalysis() {
   }

   /**
    * Invoked when all analysis are complete.  The default implementation does nothing.  Extenders of this class can
    * use this method to perform any final analysis or reporting.  Use {@link #wereFindingsReported()} to determine if
    * at least one finding was reported during the analysis.
    */
   protected void postAnalysis() {
   }

   /**
    * Returns true if at least one finding was reported when this command was executed.
    */
   protected boolean wereFindingsReported() {
      return findingsReported;
   }

   /**
    * A convenience method to report a finding to the {@code IAnalysisService}.
    */
   protected void reportFinding(SystemDescriptorFinding<?> finding) {
      Preconditions.checkState(analysisService != null,
                               "analysis service not set!  This service must be set to report a finding.");
      findingsReported = true;
      analysisService.addFinding(finding);
   }

   /**
    * Reads the given resource from the classpath and returns its contents as a string.  This operation is useful for
    * reading markdown files from the classpath when creating new
    * {@link com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType finding types}.
    *
    * @param resource the name of the resource to read
    * @param clazz    the class that contains the classloader to use to read the resource
    * @return the contents of the resource as a string
    * @throws RuntimeException if no resource with the given name could be read from the classpath
    */
   public static String getResource(String resource, Class<?> clazz) {
      return getResource(resource, Preconditions.checkNotNull(clazz, "clazz may not be null!").getClassLoader());
   }

   /**
    * Reads the given resource from the classpath and returns its contents as a string.  This operation is useful for
    * reading markdown files from the classpath when creating new
    * {@link com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType finding types}.
    *
    * @param resource the name of the resource to read
    * @param cl       the classloader to use to load the resource.
    * @return the contents of the resource as a string
    * @throws RuntimeException if no resource with the given name could be read from the classpath
    */
   public static String getResource(String resource, ClassLoader cl) {
      Preconditions.checkNotNull(resource, "resource may not be null!");
      Preconditions.checkArgument(!resource.trim().isEmpty(), "resource may not be empty!");
      Preconditions.checkNotNull(cl, "cl may not be null!");
      InputStream is = null;
      try {
         is = cl.getResourceAsStream(resource);
         Preconditions.checkArgument(is != null, "no resource named %s could be loaded from the classpath!", resource);
         return IOUtils.readLines(is, StandardCharsets.UTF_8)
               .stream()
               .collect(Collectors.joining(System.lineSeparator()));
      } catch (IOException e) {
         throw new RuntimeException("error while reading resource " + resource + " from classpath!", e);
      } finally {
         // Tolerates null streams.
         Closeables.closeQuietly(is);
      }
   }
}
