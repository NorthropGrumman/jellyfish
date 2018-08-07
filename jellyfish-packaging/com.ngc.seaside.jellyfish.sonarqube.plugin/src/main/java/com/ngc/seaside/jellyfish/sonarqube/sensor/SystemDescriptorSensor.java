package com.ngc.seaside.jellyfish.sonarqube.sensor;

import com.google.common.base.Preconditions;
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
import com.ngc.seaside.jellyfish.sonarqube.rule.SystemDescriptorRulesDefinition;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 * The plugin component that is responsible for parsing System Descriptor files and reporting errors and warnings to
 * Sonarqube. This component is scoped per project so each project gets its own instance.
 */
@InstantiationStrategy(InstantiationStrategy.PER_PROJECT)
public class SystemDescriptorSensor implements Sensor {

   private static final Logger LOGGER = Loggers.get(SystemDescriptorSensor.class);

   private final SystemDescriptorRulesDefinition rules;

   private SensorContext context;

   public SystemDescriptorSensor() {
      this.rules = new SystemDescriptorRulesDefinition();
   }

   @Override
   public void describe(@Nonnull SensorDescriptor d) {
      // The naming conventions appear to use the classname as the sensor name.
      d.name(getClass().getSimpleName());
      d.onlyOnLanguage(SystemDescriptorLanguage.KEY);
      d.onlyOnFileType(Type.MAIN);
   }

   @Override
   public void execute(@Nonnull SensorContext c) {
      Preconditions.checkNotNull(c, "cannot use null sensor context!");
      this.context = c;

      Path baseDir = c.fileSystem().baseDir().toPath();
      LOGGER.debug("Beginning scan of project {}.", baseDir);

      Map<String, String> commandLineArgs = getCommandLineArgs();

      executeValidation(commandLineArgs);
      executeAnalyses(commandLineArgs);

      LOGGER.debug("Scan complete.");
   }

   private Map<String, String> getCommandLineArgs() {
      Configuration config = context.config();
      Path baseDir = context.fileSystem().baseDir().toPath();

      Map<String, String> args = new LinkedHashMap<>();

      args.put(CommonParameters.INPUT_DIRECTORY.getName(), baseDir.toString());
      config.get("sonar.projectBaseDir").ifPresent(name -> args.put(CommonParameters.INPUT_DIRECTORY.getName(), name));
      config.get("sonar.projectName").ifPresent(name -> args.put(CommonParameters.ARTIFACT_ID.getName(), name));

      args.putAll(getExtraCliArgs());
      args.putAll(getAnalysisArgs());

      return args;
   }

   private Map<String, String> getAnalysisArgs() {
      Configuration config = context.config();
      Map<String, String> args = new LinkedHashMap<>();

      String[] analysisCommands = config.getStringArray(SystemDescriptorProperties.JELLYFISH_ANALYSIS_KEY);
      if (analysisCommands.length > 0) {
         String analysisCommandString = Stream.of(analysisCommands).collect(Collectors.joining(","));
         args.put(AnalyzeCommand.ANALYSES_PARAMETER_NAME, analysisCommandString);
      }

      return args;
   }

   private Map<String, String> getExtraCliArgs() {
      Configuration config = context.config();
      Map<String, String> args = new LinkedHashMap<>();

      String[] argString = config.getStringArray(SystemDescriptorProperties.JELLYFISH_CLI_EXTRA_ARGUMENTS_KEY);
      for (String arg : argString) {
         String[] keyValue = arg.split("=");

         if (keyValue.length != 2) {
            throw new IllegalArgumentException("Invalid argument for "
                                               + SystemDescriptorProperties.JELLYFISH_CLI_EXTRA_ARGUMENTS_KEY
                                               + ": " + arg);
         }

         args.put(keyValue[0], keyValue[1]);
      }

      return args;
   }

   private void executeValidation(Map<String, String> commandLineArgs) {
      IParsingResult r = runJellyfishCommand("validate", commandLineArgs).getParsingResult();

      for (IParsingIssue i : r.getIssues()) {
         saveSonarqubeIssue(i);
      }
   }

   private void executeAnalyses(Map<String, String> commandLineArgs) {
      if (!commandLineArgs.containsKey(AnalyzeCommand.ANALYSES_PARAMETER_NAME)) {
         return;
      }

      Injector injector = runJellyfishCommand(AnalyzeCommand.NAME, commandLineArgs).getInjector();
      IAnalysisService analysisService = injector.getInstance(IAnalysisService.class);

      for (SystemDescriptorFinding<? extends ISystemDescriptorFindingType> f : analysisService.getFindings()) {
         convertSystemDescriptorFindingToSonarqubeIssue(f);
      }
   }

   private IJellyfishExecution runJellyfishCommand(String commandName, Map<String, String> commandLineArgs) {
      return Jellyfish
            .getService()
            .run(commandName,
                 commandLineArgs,
                 Collections.singleton(JellyfishSonarqubePluginModule.withNormalLogging()));
   }

   private void saveSonarqubeIssue(IParsingIssue i) {
      NewIssue newIssue = getNewIssue(i.getSeverity());
      setIssueLocation(i.getLocation(), i.getMessage(), newIssue);
      newIssue.save();
   }

   private void setIssueLocation(ISourceLocation sourceLocation, String message, NewIssue issue) {
      InputFile f = getInputFileFromRealFile(sourceLocation.getPath());
      NewIssueLocation location = issue.newLocation()
            .on(f)
            .at(f.selectLine(sourceLocation.getLineNumber()))
            .message(message);
      issue.at(location);
   }

   private InputFile getInputFileFromRealFile(Path offendingFile) {
      FilePredicate p = context.fileSystem()
            .predicates()
            .hasFilename(offendingFile.getFileName().toString());
      return context.fileSystem().inputFile(p);
   }

   private void convertSystemDescriptorFindingToSonarqubeIssue(SystemDescriptorFinding finding) {
      Preconditions.checkState(finding.getLocation().isPresent(), "could not retrieve SD source location!");

      NewIssue newIssue = getNewIssue(finding.getType());
      setIssueLocation((ISourceLocation) finding.getLocation().get(), finding.getMessage(), newIssue);
      newIssue.save();
   }

   private NewIssue getNewIssue(Severity issueType) {
      return context.newIssue().forRule(createRuleKey(issueType));
   }

   private NewIssue getNewIssue(ISystemDescriptorFindingType findingType) {
      return context.newIssue().forRule(createRuleKey(findingType));
   }

   private RuleKey createRuleKey(Severity issueType) {
      switch (issueType) {
         case WARNING:
         default:
            return SyntaxWarningRule.KEY;
      }
   }

   private RuleKey createRuleKey(ISystemDescriptorFindingType findingType) {
      return rules.getRuleKey(findingType);
   }
}
