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
package com.ngc.seaside.jellyfish.cli.command.analyzestyle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IParameter;
import com.ngc.seaside.jellyfish.api.IParameterCollection;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.service.analysis.api.IAnalysisService;
import com.ngc.seaside.jellyfish.service.analysis.api.SystemDescriptorFinding;
import com.ngc.seaside.jellyfish.service.execution.api.JellyfishExecutionException;
import com.ngc.seaside.jellyfish.utilities.command.AbstractJellyfishAnalysisCommand;
import com.ngc.seaside.systemdescriptor.model.api.IPackage;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperty;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.log.api.ILogService;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocatorService;

/**
 * An analysis that checks that System Descriptor types follow standard naming conventions. 
 */
public class AnalyzeStyleCommand extends AbstractJellyfishAnalysisCommand {

   /**
    * Default package section style is lower case letters and numbers.
    */
   private static final Pattern DEFAULT_PACKAGE_SECTION_STYLE = Pattern.compile("[a-z][a-z0-9]*");

   /**
    * Default type name (model, enum, data name) style is upper camelcase.
    */
   private static final Pattern DEFAULT_TYPE_STYLE = Pattern.compile("(?:[A-Z][a-z0-9]*)+");

   /**
    * Default name (field, link, scenario, property) style is lower camelcase.
    */
   private static final Pattern DEFAULT_NAME_STYLE = Pattern.compile("[a-z][a-z0-9]*(?:[A-Z][a-z0-9]*)*");

   /**
    * Default style for enum values is all uppercase and numbers separated by underscores
    */
   private static final Pattern DEFAULT_ENUM_STYLE = Pattern.compile("[A-Z][A-Z0-9]*(?:_[A-Z0-9]+)*");

   /**
    * The name of the command.
    */
   public static final String NAME = "analyze-style";

   private NamingConventions conventions;

   /**
    * Creates a new command.
    */
   @Inject
   public AnalyzeStyleCommand(ILogService logService, ISourceLocatorService sourceLocatorService,
                              IAnalysisService analysisService) {
      super(NAME);
      super.setLogService(logService);
      super.setSourceLocatorService(sourceLocatorService);
      super.setAnalysisService(analysisService);
   }

   @Override
   protected void preAnalysis() {
      conventions = new NamingConventions(getOptions().getParameters());
   }

   @Override
   protected void analyzeEntireProject() {
      super.analyzeEntireProject();
      Path mainRoot = getOptions().getParsingResult().getMainSourcesRoot();
      Path testRoot = getOptions().getParsingResult().getTestSourcesRoot();
      // count == 0 when the project is parsed from a zip, which means
      // it doesn't have the src/main/sd structure and has extra jar-like files
      if (mainRoot.getNameCount() > 0 && testRoot.getNameCount() > 0) {
         checkProjectStructure(mainRoot, testRoot);
         checkUnwantedFiles(mainRoot, testRoot);
         checkFilesWithPackages(mainRoot);
      }
   }

   @Override
   protected void analyzePackage(IPackage pkg) {
      for (String section : pkg.getName().split("\\.")) {
         if (checkForNamingIssue(conventions.packageSection, section, pkg.getName(), pkg)) {
            break;
         }
      }
   }

   @Override
   protected void analyzeModel(IModel model) {
      checkForNamingIssue(conventions.modelType, model.getName(), model);
      for (IProperty property : model.getProperties()) {
         checkForNamingIssue(conventions.propertyField, property.getName(), property);
      }
      for (IDataReferenceField field : model.getInputs()) {
         checkForNamingIssue(conventions.modelInputField, field.getName(), field);
      }
      for (IDataReferenceField field : model.getOutputs()) {
         checkForNamingIssue(conventions.modelOutputField, field.getName(), field);
      }
      for (IScenario scenario : model.getScenarios()) {
         checkForNamingIssue(conventions.modelScenario, scenario.getName(), scenario);
      }
      for (IModelReferenceField field : model.getParts()) {
         checkForNamingIssue(conventions.modelPartField, field.getName(), field);
         for (IProperty property : field.getProperties()) {
            checkForNamingIssue(conventions.propertyField, property.getName(), property);
         }
      }
      for (IModelReferenceField field : model.getRequiredModels()) {
         checkForNamingIssue(conventions.modelRequireField, field.getName(), field);
         for (IProperty property : field.getProperties()) {
            checkForNamingIssue(conventions.propertyField, property.getName(), property);
         }
      }
      for (IModelLink<?> link : model.getLinks()) {
         link.getName().ifPresent(name -> checkForNamingIssue(conventions.modelLinkField, name, link));
      }
   }

   @Override
   protected void analyzeData(IData data) {
      checkForNamingIssue(conventions.dataType, data.getName(), data);
      for (IDataField field : data.getFields()) {
         checkForNamingIssue(conventions.dataField, field.getName(), field);
      }
   }

   @Override
   protected void analyzeEnumeration(IEnumeration enumeration) {
      checkForNamingIssue(conventions.enumType, enumeration.getName(), enumeration);
      for (String value : enumeration.getValues()) {
         checkForNamingIssue(conventions.enumValue, value, enumeration);
      }
   }

   private void checkProjectStructure(Path mainRoot, Path testRoot) {
      if (mainRoot.getNameCount() > 0 && !mainRoot.endsWith(Paths.get("src", "main", "sd"))) {
         String message = "The System Descriptor project's `sd` directory should be `<project-folder>/src/main/sd`: "
                  + mainRoot;
         SystemDescriptorFinding<?> finding = StyleFindingTypes.BAD_PROJECT.createFinding(message, null, 1);
         reportFinding(finding);
      }
      if (testRoot.getNameCount() > 0 && !testRoot.endsWith(Paths.get("src", "test", "gherkin"))) {
         String message =
                  "The System Descriptor project's `gherkin` directory should be `<project-folder>/src/test/gherkin`: "
                           + testRoot;
         SystemDescriptorFinding<?> finding = StyleFindingTypes.BAD_PROJECT.createFinding(message, null, 1);
         reportFinding(finding);
      }
   }

   private void checkUnwantedFiles(Path mainRoot, Path testRoot) {
      List<Path> nonSdFiles;
      List<Path> nonGherkinFiles;
      try {
         nonSdFiles = Files.walk(mainRoot)
                  .filter(Files::isRegularFile)
                  .filter(path -> !path.getFileName().toString().toLowerCase().endsWith(".sd"))
                  .map(path -> mainRoot.relativize(path))
                  .collect(Collectors.toList());
         nonGherkinFiles = Files.walk(testRoot)
                  .filter(Files::isRegularFile)
                  .filter(path -> !path.getFileName().toString().toLowerCase().endsWith(".feature"))
                  .map(path -> testRoot.relativize(path))
                  .collect(Collectors.toList());
      } catch (IOException e) {
         throw new JellyfishExecutionException("Unable to access main and test sources for SD project", e);
      }
      if (!nonSdFiles.isEmpty()) {
         String message =
                  "The following files were found in the System Descriptor project's `sd` directory "
                           + "that were not `.sd` files: \n\n"
                           + nonSdFiles.stream().map(Object::toString).map(str -> "* " + str)
                                    .collect(Collectors.joining("\n"));
         SystemDescriptorFinding<?> finding = StyleFindingTypes.BAD_PROJECT.createFinding(message, null, 1);
         reportFinding(finding);
      }
      if (!nonGherkinFiles.isEmpty()) {
         String message =
                  "The following files were found in the "
                           + "System Descriptor's `gherkin` directory that were not `.feature` files: \n\n"
                           + nonGherkinFiles.stream().map(Object::toString).map(str -> "* " + str)
                                    .collect(Collectors.joining("\n"));
         SystemDescriptorFinding<?> finding = StyleFindingTypes.BAD_PROJECT.createFinding(message, null, 1);
         reportFinding(finding);
      }
   }

   private void checkFilesWithPackages(Path mainRoot) {
      try {
         Files.walk(mainRoot)
                  .filter(Files::isRegularFile)
                  .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".sd"))
                  .forEach(path -> checkFileWithPackage(path, mainRoot));
      } catch (IOException e) {
         throw new JellyfishExecutionException("Unable to access main sources for SD project", e);
      }
   }

   private void checkFileWithPackage(Path file, Path mainRoot) {
      Path relative = mainRoot.relativize(file);
      if (relative.getNameCount() <= 1) {
         String message =
                  "The file " + file + " should not be at the root of the System Descriptor project's `sd` folder";
         SystemDescriptorFinding<?> finding = StyleFindingTypes.BAD_PROJECT.createFinding(message, null, 1);
         reportFinding(finding);
      }
      String pkgName = "";
      for (Path part : relative.subpath(0, relative.getNameCount() - 1)) {
         pkgName += part + ".";
      }
      pkgName = pkgName.substring(0, pkgName.length() - 1);
      ISystemDescriptor sd = getOptions().getSystemDescriptor();
      Optional<IPackage> optionalPkg = sd.getPackages().getByName(pkgName);

      if (optionalPkg.isPresent()) {
         IPackage pkg = optionalPkg.get();
         String typeName = file.getFileName().toString();
         typeName = typeName.substring(0, typeName.lastIndexOf('.'));
         if (pkg.getData().getByName(typeName).isPresent() || pkg.getModels().getByName(typeName).isPresent()
                  || pkg.getEnumerations().getByName(typeName).isPresent()) {
            return;
         }
      }
      String message =
               "A System Descriptor type for the file " + file + " could not be found";
      SystemDescriptorFinding<?> finding = StyleFindingTypes.BAD_PROJECT.createFinding(message, null, 1);
      reportFinding(finding);
   }

   private boolean checkForNamingIssue(NamingConvention convention, String name, Object type) {
      return checkForNamingIssue(convention, name, name, type);
   }

   private boolean checkForNamingIssue(NamingConvention convention, String conventionName, String actualName,
            Object type) {
      if (!convention.matches(conventionName)) {
         String message = convention.getDescription(actualName);
         ISourceLocation location = sourceLocatorService.getLocation(type, false);
         SystemDescriptorFinding<?> finding = StyleFindingTypes.BAD_STYLE.createFinding(message, location, 1);
         reportFinding(finding);
         return true;
      }
      return false;
   }

   private static class NamingConvention {

      private final Pattern pattern;
      private final String descriptionFormat;

      public NamingConvention(Pattern pattern, String descriptionFormat) {
         this.pattern = pattern;
         this.descriptionFormat = descriptionFormat;
      }

      public boolean matches(String text) {
         return pattern.matcher(text).matches();
      }

      public String getDescription(String name) {
         return String.format(descriptionFormat, name);
      }

   }

   private static class NamingConventions {
      private final NamingConvention packageSection;
      private final NamingConvention dataType;
      private final NamingConvention modelType;
      private final NamingConvention enumType;
      private final NamingConvention dataField;
      private final NamingConvention enumValue;
      private final NamingConvention propertyField;
      private final NamingConvention modelInputField;
      private final NamingConvention modelOutputField;
      private final NamingConvention modelPartField;
      private final NamingConvention modelRequireField;
      private final NamingConvention modelScenario;
      private final NamingConvention modelLinkField;

      public NamingConventions(IParameterCollection collection) {
         packageSection = initConvention(collection, "package name", "be only lowercase letters or numbers",
                  DEFAULT_PACKAGE_SECTION_STYLE, "packageSectionStyleRegex");
         dataType = initConvention(collection, "data type name", "be upper CamelCase", DEFAULT_TYPE_STYLE,
                  "typeStyleRegex", "dataTypeStyleRegex");
         modelType = initConvention(collection, "model type name", "be upper CamelCase", DEFAULT_TYPE_STYLE,
                  "typeStyleRegex", "modelTypeStyleRegex");
         enumType = initConvention(collection, "enumeration type name", "be upper CamelCase", DEFAULT_TYPE_STYLE,
                  "typeStyleRegex", "enumTypeStyleRegex");
         enumValue = initConvention(collection, "enum value name", "be only uppercase letters, numbers or underscores",
                  DEFAULT_ENUM_STYLE, "enumValueStyleRegex");
         dataField = initConvention(collection, "data field name", "be lower camelCase", DEFAULT_NAME_STYLE,
                  "nameStyleRegex", "dataFieldStyleRegex");
         propertyField = initConvention(collection, "property field name", "be lower camelCase", DEFAULT_NAME_STYLE,
                  "nameStyleRegex", "propertyStyleRegex");
         modelInputField =
                  initConvention(collection, "model input field name", "be lower camelCase", DEFAULT_NAME_STYLE,
                           "nameStyleRegex", "modelFieldStyleRegex", "modelIoStyleRegex", "modelInputStyleRegex");
         modelOutputField =
                  initConvention(collection, "model output field name", "be lower camelCase", DEFAULT_NAME_STYLE,
                           "nameStyleRegex", "modelFieldStyleRegex", "modelIoStyleRegex", "modelOutputStyleRegex");
         modelPartField = initConvention(collection, "model part name", "be lower camelCase", DEFAULT_NAME_STYLE,
                  "nameStyleRegex", "modelFieldStyleRegex", "modelReferenceStyleRegex", "partStyleRegex");
         modelRequireField = initConvention(collection, "required model name", "be lower camelCase", DEFAULT_NAME_STYLE,
                  "nameStyleRegex", "modelFieldStyleRegex", "modelReferenceStyleRegex", "requireStyleRegex");
         modelLinkField = initConvention(collection, "model link field name", "be lower camelCase", DEFAULT_NAME_STYLE,
                  "nameStyleRegex", "modelFieldStyleRegex", "linkStyleRegex");
         modelScenario = initConvention(collection, "model scenario name", "be lower camelCase", DEFAULT_NAME_STYLE,
                  "nameStyleRegex", "modelFieldStyleRegex", "scenarioStyleRegex");
      }

      private static NamingConvention initConvention(IParameterCollection collection, String name,
               String defaultDescription, Pattern defaultPattern, String... parameterOverrides) {
         Pattern pattern = getPattern(collection, defaultPattern, parameterOverrides);
         String descriptionFormat = "The " + name + " %s does not follow standard naming conventions. It should ";
         if (pattern == defaultPattern) {
            descriptionFormat += defaultDescription;
         } else {
            descriptionFormat += "match the regular expression " + pattern.pattern();
         }
         return new NamingConvention(pattern, descriptionFormat);
      }

      private static Pattern getPattern(IParameterCollection collection, Pattern defaultPattern,
               String... parameterOverrides) {
         Pattern pattern = defaultPattern;
         for (String parameter : parameterOverrides) {
            IParameter<?> param = collection.getParameter(parameter);
            if (param == null) {
               continue;
            }
            String regex = param.getStringValue();
            if (regex == null) {
               continue;
            }
            try {
               pattern = Pattern.compile(regex);
            } catch (PatternSyntaxException e) {
               throw new JellyfishExecutionException("Invalid regex for " + NAME + " command parameter: " + regex, e);
            }
         }
         return pattern;
      }
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Checks that System Descriptor files follow standard naming conventions",
               new DefaultParameter<>("packageSectionStyleRegex").advanced()
                        .setDescription("The regular expression for matching individual sections of a package"),
               new DefaultParameter<>("typeStyleRegex").advanced()
                        .setDescription("The regular expression for matching type names"),
               new DefaultParameter<>("dataTypeStyleRegex").advanced()
                        .setDescription("The regular expression for matching data type names"),
               new DefaultParameter<>("modelTypeStyleRegex").advanced()
                        .setDescription("The regular expression for matching model type names"),
               new DefaultParameter<>("enumTypeStyleRegex").advanced()
                        .setDescription("The regular expression for matching enum type names"),
               new DefaultParameter<>("enumValueStyleRegex").advanced()
                        .setDescription("The regular expression for matching enum value names"),
               new DefaultParameter<>("nameStyleRegex").advanced()
                        .setDescription("The regular expression for matching non-type names"),
               new DefaultParameter<>("modelFieldStyleRegex").advanced()
                        .setDescription("The regular expression for matching model field names"),
               new DefaultParameter<>("modelIoStyleRegex").advanced()
                        .setDescription("The regular expression for matching model input/output field names"),
               new DefaultParameter<>("modelInputStyleRegex").advanced()
                        .setDescription("The regular expression for matching model input field names"),
               new DefaultParameter<>("modelOutputStyleRegex").advanced()
                        .setDescription("The regular expression for matching model output field names"),
               new DefaultParameter<>("modelReferenceStyleRegex").advanced()
                        .setDescription("The regular expression for matching model part/require field names"),
               new DefaultParameter<>("requireStyleRegex").advanced()
                        .setDescription("The regular expression for matching model require field names"),
               new DefaultParameter<>("partStyleRegex").advanced()
                        .setDescription("The regular expression for matching model part field names"),
               new DefaultParameter<>("linkStyleRegex").advanced()
                        .setDescription("The regular expression for matching model link field names"),
               new DefaultParameter<>("scenarioStyleRegex").advanced()
                        .setDescription("The regular expression for matching model scenario names"),
               new DefaultParameter<>("dataFieldStyleRegex").advanced()
                        .setDescription("The regular expression for matching data field names"),
               new DefaultParameter<>("propertyStyleRegex").advanced()
                        .setDescription("The regular expression for matching property field names"));
   }
}
