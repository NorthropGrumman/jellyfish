package com.ngc.seaside.jellyfish.sonarqube.sensor;

import com.google.inject.Guice;

import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.module.JellyfishPluginModule;
import com.ngc.seaside.jellyfish.sonarqube.rules.SystemDescriptorRulesDefinition;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.api.ISystemDescriptorService;
import com.ngc.seaside.systemdescriptor.validation.api.Severity;

import org.sonar.api.batch.InstantiationStrategy;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

import java.nio.file.Path;

/**
 * The plugin component that is responsible for parsing System Descriptor files and reporting errors and warnings to
 * Sonarqube.  This component is scoped per project so each project gets its own instance.
 */
@InstantiationStrategy(InstantiationStrategy.PER_PROJECT)
public class SystemDescriptorSensor implements Sensor {

   private final ISystemDescriptorService systemDescriptorService;

   /**
    * Creates a new sesnor.
    */
   public SystemDescriptorSensor() {
      // Create a single instance of the system descriptor service and reuse it for all modules in the project.
      systemDescriptorService = getServiceInstance();
   }

   @Override
   public void describe(SensorDescriptor d) {
      // The naming conventions appear to use the classname as the sensor name.
      d.name(getClass().getSimpleName());
      d.onlyOnLanguage(SystemDescriptorLanguage.KEY);
      d.onlyOnFileType(Type.MAIN);
   }

   @Override
   public void execute(SensorContext c) {
      // Note the baseDir value will point to the base directory of Gradle project when scanning a project with Gradle.
      IParsingResult r = systemDescriptorService
            .parseProject(
                  c.fileSystem()
                        .baseDir()
                        .toPath()
            );

      for (IParsingIssue i : r.getIssues()) {
         saveIssue(c, i);
      }
   }

   private void saveIssue(SensorContext c, IParsingIssue i) {
      NewIssue newIssue = getNewIssue(c, i.getSeverity());
      setIssueLocation(c, i, newIssue);
      newIssue.save();
   }

   private void setIssueLocation(SensorContext c, IParsingIssue parsingIssue, NewIssue issue) {
      InputFile f = getInputFileFromRealFile(c, parsingIssue.getOffendingFile());
      NewIssueLocation location = issue.newLocation()
            .on(f)
            .at(f.selectLine(parsingIssue.getLineNumber()))
            .message(parsingIssue.getMessage());
      issue.at(location);
   }

   private InputFile getInputFileFromRealFile(SensorContext c, Path offendingFile) {
      FilePredicate p = c.fileSystem()
            .predicates()
            .hasFilename(offendingFile.getFileName().toString());
      return c.fileSystem().inputFile(p);
   }

   private NewIssue getNewIssue(SensorContext c, Severity errorType) {
      return c.newIssue().forRule(createRuleKey(errorType));
   }

   private RuleKey createRuleKey(Severity errorType) {
      switch (errorType) {
         case WARNING:
            return SystemDescriptorRulesDefinition.Rules.SYNTAX_WARNINGS;
         case ERROR:
            // Intentionally fall through.
         default:
            return SystemDescriptorRulesDefinition.Rules.SYNTAX_ERRORS;
      }
   }

   private static ISystemDescriptorService getServiceInstance() {
      return Guice.createInjector(new JellyfishPluginModule())
            .getInstance(ISystemDescriptorService.class);
   }
}
