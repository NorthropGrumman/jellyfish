package com.ngc.seaside.jellyfish.cli.command.analyze.inputsoutputs;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;

import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishCommand;
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

public abstract class AbstractJellyfishAnalysisCommand extends AbstractJellyfishCommand {

   protected IAnalysisService analysisService;

   protected ISourceLocatorService sourceLocatorService;

   private boolean findingsReported = false;

   protected AbstractJellyfishAnalysisCommand(String name) {
      super(name);
   }

   public void setAnalysisService(IAnalysisService ref) {
      this.analysisService = ref;
   }

   public void removeAnalysisService(IAnalysisService ref) {
      setAnalysisService(null);
   }

   public void setSourceLocatorService(ISourceLocatorService ref) {
      this.sourceLocatorService = ref;
   }

   public void removeSourceLocatorService(ISourceLocatorService ref) {
      setSourceLocatorService(null);
   }

   @Override
   protected void doRun() {
      // Analysis commands have 3 modes of operation:
      // 1) A specific model was specified with the "model" argument.  This means only scan the given model.
      // 2) The models which match the given stereotypes if the "stereotypes" argument is used.
      // 3) Every element in the project if none of the above options where used.

      Stopwatch sw = Stopwatch.createStarted();

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

   protected void analyzeEntireProject() {
      for (IPackage packagez : getOptions().getSystemDescriptor().getPackages()) {
         packagez.getModels().forEach(this::analyzeModel);
         packagez.getData().forEach(this::analyzeData);
         packagez.getEnumerations().forEach(this::analyzeEnumeration);
      }
   }

   protected void analyzeModel(IModel model) {
   }

   protected void analyzeData(IData data) {
   }

   protected void analyzeEnumeration(IEnumeration enumeration) {
   }

   protected void postAnalysis() {
   }

   protected boolean wereFindingsReported() {
      return findingsReported;
   }

   protected void reportFinding(SystemDescriptorFinding<?> finding) {
      analysisService.addFinding(finding);
   }

   public static String getResource(String resource, Class<?> clazz) {
      return getResource(resource, Preconditions.checkNotNull(clazz, "clazz may not be null!").getClassLoader());
   }

   public static String getResource(String resource, ClassLoader cl) {
      Preconditions.checkNotNull(resource, "resource may not be null!");
      Preconditions.checkArgument(!resource.trim().isEmpty(), "resource may not be empty!");
      Preconditions.checkNotNull(cl, "cl may not be null!");
      InputStream is = cl.getResourceAsStream(resource);
      Preconditions.checkArgument(is != null, "no resource named %s could be loaded from the classpath!", resource);
      try {
         return IOUtils.readLines(is, StandardCharsets.UTF_8)
               .stream()
               .collect(Collectors.joining(System.lineSeparator()));
      } catch (IOException e) {
         throw new RuntimeException("error while reading resource " + resource + " from classpath!", e);
      }
   }
}
