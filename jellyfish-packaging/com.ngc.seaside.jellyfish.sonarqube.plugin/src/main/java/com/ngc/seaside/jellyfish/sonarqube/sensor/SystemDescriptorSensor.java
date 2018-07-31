package com.ngc.seaside.jellyfish.sonarqube.sensor;

import com.google.inject.Injector;
import com.ngc.seaside.jellyfish.Jellyfish;
import com.ngc.seaside.jellyfish.api.CommonParameters;
import com.ngc.seaside.jellyfish.cli.command.analyze.AnalyzeCommand;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.ISystemDescriptorFindingType;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.service.execution.api.IJellyfishExecution;
import com.ngc.seaside.jellyfish.sonarqube.language.SystemDescriptorLanguage;
import com.ngc.seaside.jellyfish.sonarqube.module.JellyfishSonarqubePluginModule;
import com.ngc.seaside.jellyfish.sonarqube.properties.SystemDescriptorProperties;
import com.ngc.seaside.jellyfish.sonarqube.rule.SyntaxWarningRule;
import com.ngc.seaside.systemdescriptor.service.api.IParsingIssue;
import com.ngc.seaside.systemdescriptor.service.api.IParsingResult;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
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
import org.sonar.api.config.Configuration;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The plugin component that is responsible for parsing System Descriptor files and reporting errors and warnings to
 * Sonarqube. This component is scoped per project so each project gets its own instance.
 */
@InstantiationStrategy(InstantiationStrategy.PER_PROJECT)
public class SystemDescriptorSensor implements Sensor {

   private static final Logger LOGGER = Loggers.get(SystemDescriptorSensor.class);

   @Override
   public void describe(SensorDescriptor d) {
      // The naming conventions appear to use the classname as the sensor name.
      d.name(getClass().getSimpleName());
      d.onlyOnLanguage(SystemDescriptorLanguage.KEY);
      d.onlyOnFileType(Type.MAIN);
   }

   @Override
   public void execute(SensorContext c) {
      Path baseDir = c.fileSystem().baseDir().toPath();
      LOGGER.debug("Beginning scan of project {}.", baseDir);

      Collection<String> commandLineArgs = getCommandLineArgs(c);

      executeValidation(c, commandLineArgs);
      executeAnalyses(c, commandLineArgs);

      LOGGER.debug("Scan complete.");
   }

   private Collection<String> getCommandLineArgs(SensorContext c) {
      Configuration config = c.config();
      Path baseDir = c.fileSystem().baseDir().toPath();

      Collection<String> commandLineArgs = new ArrayList<>();

      Map<String, String> args = new LinkedHashMap<>();

      args.put(CommonParameters.INPUT_DIRECTORY.getName(), baseDir.toString());
      config.get("sonar.projectBaseDir").ifPresent(name -> args.put(CommonParameters.INPUT_DIRECTORY.getName(), name));
      config.get("sonar.projectName").ifPresent(name -> args.put(CommonParameters.ARTIFACT_ID.getName(), name));

      String[] argString = config.getStringArray(SystemDescriptorProperties.JELLYFISH_CLI_EXTRA_ARGUMENTS_KEY);
      for (String arg : argString) {
         String[] keyValue = arg.split("=");
         if (keyValue.length != 2) {
            throw new IllegalArgumentException("Invalid argument for "
                     + SystemDescriptorProperties.JELLYFISH_CLI_EXTRA_ARGUMENTS_KEY + ": " + arg);
         }
         args.put(keyValue[0], keyValue[1]);
      }
      String[] analysisCommands = config.getStringArray(SystemDescriptorProperties.JELLYFISH_ANALYSIS_KEY);
      String analysisCommandString = Stream.of(analysisCommands).collect(Collectors.joining(","));
      args.put(AnalyzeCommand.ANALYSES_PARAMETER_NAME, analysisCommandString);

      for (Entry<String, String> entry : args.entrySet()) {
         String key = entry.getKey();
         String value = entry.getValue();
         commandLineArgs.add(formatCommandLineArg(key, value));
      }

      return commandLineArgs;
   }

   private void executeValidation(SensorContext c, Collection<String> commandLineArgs) {
      IJellyfishExecution result = Jellyfish
            .getService()
            .run("validate",
                 commandLineArgs,
                 Collections.singleton(JellyfishSonarqubePluginModule.withNormalLogging()));

      IParsingResult r = result.getParsingResult();

      for (IParsingIssue i : r.getIssues()) {
         saveIssue(c, i);
      }
   }

   private void executeAnalyses(SensorContext c, Collection<String> commandLineArgs) {
      IJellyfishExecution result = Jellyfish.getService().run(AnalyzeCommand.NAME, commandLineArgs,
               Collections.singleton(JellyfishSonarqubePluginModule.withNormalLogging()));
      Injector injector = result.getInjector();
      IAnalysisService analysisService = injector.getInstance(IAnalysisService.class);

      for (SystemDescriptorFinding<? extends ISystemDescriptorFindingType> find : analysisService.getFindings()) {
         // TODO convert to sonarqube issue
      }
   }

   private void saveIssue(SensorContext c, IParsingIssue i) {
      NewIssue newIssue = getNewIssue(c, i.getSeverity());
      setIssueLocation(c, i, newIssue);
      newIssue.save();
   }

   private void setIssueLocation(SensorContext c, IParsingIssue parsingIssue, NewIssue issue) {
      ISourceLocation sourceLocation = parsingIssue.getLocation();
      InputFile f = getInputFileFromRealFile(c, sourceLocation.getPath());
      NewIssueLocation location = issue.newLocation()
            .on(f)
            .at(f.selectLine(sourceLocation.getLineNumber()))
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
         default:
            return SyntaxWarningRule.KEY;
      }
   }

   private String formatCommandLineArg(String parameterName, String parameterValue) {
      return String.format("%s=%s", parameterName, parameterValue);
   }
}
