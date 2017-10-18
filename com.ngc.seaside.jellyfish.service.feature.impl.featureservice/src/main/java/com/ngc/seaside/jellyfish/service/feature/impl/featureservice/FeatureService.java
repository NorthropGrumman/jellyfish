package com.ngc.seaside.jellyfish.service.feature.impl.featureservice;

import com.google.common.base.Preconditions;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;

@Component(service = IFeatureService.class)
public class FeatureService implements IFeatureService {

   private ILogService logService;

   @Override
   public TreeMap<String, IFeatureInformation> getFeatures(Path sdPath, IModel model) {
      Preconditions.checkNotNull(sdPath, "sdPath may not be null!");
      Preconditions.checkNotNull(model, "model may not be null!");
   
      TreeMap<String, IFeatureInformation> features = new TreeMap<>(Collections.reverseOrder());
      final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.feature");
      final Path gherkin = sdPath.resolve(Paths.get("src", "test", "gherkin"))
                                         .toAbsolutePath();
      String packages = model.getParent().getName();
      Path modelPath = Paths.get(packages.replace('.', File.separatorChar));
   
      Path featureFilesRoot = gherkin.resolve(modelPath);
   
      try {
         Files.list(featureFilesRoot)
              .filter(Files::isRegularFile)
              .filter(matcher::matches)
              .filter(f -> f.getFileName().toString().startsWith(model.getName() + '.'))
              .map(Path::toAbsolutePath)
              .forEach(path -> features.put(path.toString(), new FeatureInformation(path, gherkin.relativize(path))));
      } catch (IOException e) {
         logService.warn(getClass(), "No feature files at %s", featureFilesRoot);
      }
      return features;
   
   }

   @Override
   public TreeMap<String, IFeatureInformation> getFeatures(Path sdPath, IScenario scenario) {
      Preconditions.checkNotNull(sdPath, "sdPath may not be null!");
      Preconditions.checkNotNull(scenario, "scenario may not be null!");   
      return getFeatures(sdPath, scenario.getParent()); 
   }

   @Override
   public TreeMap<String, IFeatureInformation> getAllFeatures(Path sdPath, Collection<IModel> models) {
      TreeMap<String, IFeatureInformation> features = new TreeMap<>(Collections.reverseOrder());

      models.forEach(model -> {
         features.putAll(getFeatures(sdPath, model));
      });
      return features;
   }
   
   @Activate
   public void activate() {
      logService.debug(getClass(), "activated");
   }

   @Deactivate
   public void deactivate() {
      logService.debug(getClass(), "deactivated");
   }

   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   public void removeLogService(ILogService ref) {
      setLogService(null);
   }



}
