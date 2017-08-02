package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;
import com.ngc.seaside.bootstrap.utilities.console.impl.stringtable.StringTable;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

@Component(service = IJellyFishCommand.class)
public class RequirementsVerificationMatrixCommand implements IJellyFishCommand {

   /**
    * The JSON key refereed in the metadata of a model to declare requirements.  The value is either a string or an
    * array of strings.
    */
   private static final String REQUIREMENTS_MEMBER_NAME = "satisfies";
   private static final String NAME = "requirements-verification-matrix";
   private static final String GHERKIN_URI = "src/test/gherkin";
   static final String OUTPUT_FORMAT_PROPERTY = "outputFormat";
   static final String DEFAULT_OUTPUT_FORMAT_PROPERTY = "DEFAULT";
   static final String OUTPUT_PROPERTY = "output";
   static final String DEFAULT_OUTPUT_PROPERTY = "STDOUT";
   static final String VALUES_PROPERTY = "values";
   static final String DEFAULT_VALUES_PROPERTY = "service";
   static final String OPERATOR_PROPERTY = "operator";
   private static final IUsage USAGE = createUsage();
   static final String DEFAULT_OPERATOR_PROPERTY = "OR";
   private ILogService logService;

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @SuppressWarnings("rawtypes")
   private static IUsage createUsage() {
      return new DefaultUsage("A JellyFish command that can generate a requirements verification matrix.",
                              new DefaultParameter(OUTPUT_FORMAT_PROPERTY).setDescription(
                                       "Allows the user to define the output format. The possible values are default and csv")
                                       .setRequired(false),
                              new DefaultParameter(OUTPUT_PROPERTY).setDescription(
                                       "Allows the user to define the file where the output will be stored. Default: prints to stdout."),
                              new DefaultParameter(VALUES_PROPERTY).setDescription(
                                       "The values in which to search as a comma separated string. Default: service")
                                       .setRequired(false),
                              new DefaultParameter(OPERATOR_PROPERTY).setDescription(
                                       "AND, OR, NOT: determines if the items be AND'd together or OR'd together. Default: OR")
                                       .setRequired(false));
   }

   /**
    * Retrieve the output format property value based on user input. Default is a table formatted string.
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateOutputFormat(IJellyFishCommandOptions commandOptions) {
      String outputFormat = DEFAULT_OUTPUT_FORMAT_PROPERTY;
      if (commandOptions.getParameters().containsParameter(OUTPUT_FORMAT_PROPERTY)) {
         String helper = commandOptions.getParameters().getParameter(OUTPUT_FORMAT_PROPERTY).getStringValue();
         outputFormat = (helper.equalsIgnoreCase("CSV")) ? helper.toUpperCase() : DEFAULT_OUTPUT_FORMAT_PROPERTY;
      }
      return outputFormat;
   }

   /**
    * Retrieve the output property value based on user input. Default is standard output
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateOutput(IJellyFishCommandOptions commandOptions) {
      String output = DEFAULT_OUTPUT_PROPERTY;
      if (commandOptions.getParameters().containsParameter(OUTPUT_PROPERTY)) {
         output = commandOptions.getParameters().getParameter(OUTPUT_PROPERTY).getStringValue();
      }

      return output;
   }

   /**
    * Retrieve the values property value based on user input. Default is: "service"
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateValues(IJellyFishCommandOptions commandOptions) {
      String values = DEFAULT_VALUES_PROPERTY;
      if (commandOptions.getParameters().containsParameter(VALUES_PROPERTY)) {
         values = commandOptions.getParameters().getParameter(VALUES_PROPERTY).getStringValue();
      }
      return values;
   }

   /**
    * Retrieve the operator property value based on user input. Default is: "OR"
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateOperator(IJellyFishCommandOptions commandOptions) {
      String operator = DEFAULT_OPERATOR_PROPERTY;
      if (commandOptions.getParameters().containsParameter(OPERATOR_PROPERTY)) {
         String helper = commandOptions.getParameters().getParameter(OPERATOR_PROPERTY).getStringValue().toUpperCase();
         operator = (helper.equalsIgnoreCase("AND") || helper.equalsIgnoreCase("NOT")) ? helper : operator;
      }
      return operator;
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
   }

   protected Path getFeatureFilesDirectory(IJellyFishCommandOptions commandOptions) {
      return commandOptions.getSystemDescriptorProjectPath().toAbsolutePath().resolve(GHERKIN_URI);
   }

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      String outputFormat = evaluateOutputFormat(commandOptions);
      String output = evaluateOutput(commandOptions);
      String values = evaluateValues(commandOptions);
      String operator = evaluateOperator(commandOptions);

      Collection<IModel> models = searchModels(commandOptions, values, operator);
      Map<String, Feature> features = getAllFeatures(commandOptions, models);
      Collection<Requirement> satisfiedRequirements = verifyRequirements(models, features);

      String report = "";
      if (outputFormat.equalsIgnoreCase("csv")) {
         report = generateCsvVerificationMatrix(satisfiedRequirements, features.keySet());
      } else {
         report = String.valueOf(generateDefaultVerificationMatrix(satisfiedRequirements, features.keySet()));
      }

      if (output.equalsIgnoreCase(DEFAULT_OUTPUT_PROPERTY)) {
         System.out.println(report);
         logService.info(RequirementsVerificationMatrixCommand.class, "Printing report to console...");
      } else {
         printReportToFile(report, output);
         logService.info(RequirementsVerificationMatrixCommand.class, "Printing report to location: %s",
                         Paths.get(output).toAbsolutePath().toString());
      }

      logService.info(RequirementsVerificationMatrixCommand.class,
                      "%s requirements verification matrix successfully created", values);

   }

   /**
    * Prints the verification matrix report to the file provided by the output
    *
    * @param report verification matrix to be printed
    * @param output file location for output
    */
   private void printReportToFile(String report, String output) {
      //TODO: implement
      //FileUtilities.addLinesToFile();

   }

   /**
    * Generates a comma delimited requirements verification matrix given a Collection of requirements and features
    *
    * @param requirements satisfied requirements
    * @param features     satisfied features
    * @return a {@link StringTable} containing verification matrix
    */
   private String generateCsvVerificationMatrix(Collection<Requirement> requirements, Set<String> features) {
      //TODO: implement
      StringBuilder sb = new StringBuilder();
      return sb.toString();
   }

   /**
    * Generates a requirements verification matrix given a Collection of requirements and features
    *
    * @param requirements satisfied requirements
    * @param features     satisfied features
    * @return a {@link StringTable} containing verification matrix
    */
   private StringTable generateDefaultVerificationMatrix(Collection<Requirement> requirements,
                                                         Collection<String> features) {
      StringTable<Requirement> stringTable = createStringTable(features);

      requirements.forEach(requirement -> stringTable.getModel().addItem(requirement));

      stringTable.setRowSpacer("_");
      stringTable.setColumnSpacer("|");

      stringTable.setShowHeader(true);

      return stringTable;
   }

   /**
    * Creates a {@link StringTable} for {@link Requirement} objects
    *
    * @param features features features to compare against each {@link Requirement}
    */
   protected StringTable<Requirement> createStringTable(Collection<String> features) {
      return new StringTable<>(createTableFormat(features));
   }

   /**
    * Creates a {@link ITableFormat} for {@link Requirement} objects
    *
    * @param features features features to compare against each {@link Requirement}
    */
   private ITableFormat<Requirement> createTableFormat(Collection<String> features) {
      return new RequirementItemFormat(features);
   }

   /**
    * Verifies which feature files meets which requirement.
    *
    * @param models     models to search through for requirements
    * @param featureMap features to look for
    * @return returns a Collection of {@link Requirement}s
    */
   private Collection<Requirement> verifyRequirements(Collection<IModel> models, Map<String, Feature> featureMap) {
      TreeMultimap<String, String>
               requirementsMap =
               TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());

      featureMap.forEach((featureFileName, feature) -> {
         IModel model = models.stream().filter(aModel -> feature.getFullyQualifiedName().startsWith(aModel.getName()))
                  .findAny().orElse(null);

         if (model != null) {

            IScenario scenario = model.getScenarios().getByName(feature.getName()).orElse(null);
            // A feature file should be considered to verify a requirement:
            if (scenario != null) {
               // if the model that contains the scenario has a "satisfies" metadata file for some requirement
               feature.addRequirements(RequirementsUtils.getRequirementsFromModel(model, REQUIREMENTS_MEMBER_NAME));

               // if the scenario in the model has a "satisfies" metadata field
               feature.addRequirements(
                        RequirementsUtils.getRequirementsFromScenario(scenario, REQUIREMENTS_MEMBER_NAME));
            }
            feature.getRequirements()
                     .forEach(requirement -> requirementsMap.put(requirement, feature.getFullyQualifiedName()));
         }
      });

      return createVerifiedRequirements(requirementsMap);
   }

   /**
    * This is a helper method that goes through a multimap of requirement to value(s) and creates a {@link Requirement}
    * objects.
    *
    * @param requirementsMap a map where key = a requirement and value(s) = all features that that satisfy it
    * @return returns a Collection of {@link Requirement}s
    */
   private Collection<Requirement> createVerifiedRequirements(Multimap<String, String> requirementsMap) {
      TreeSet<Requirement> features = new TreeSet<>(Collections.reverseOrder());
      requirementsMap.keySet().forEach(requirement -> {
         Requirement helper = new Requirement(requirement);
         helper.addFeatures(requirementsMap.get(requirement));
         features.add(helper);
      });
      return features;
   }

   /**
    * Returns a collection of models that matches the search criteria
    *
    * @param commandOptions Jellyfish command options containing system descriptor
    * @param values         the values in which to search
    * @param operator       the operator to apply to search
    */
   private Collection<IModel> searchModels(IJellyFishCommandOptions commandOptions, String values, String operator) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();

      switch (operator) {
      case "AND":
         return Traversals.collectModels(sd, ModelPredicates.withAllStereotypes(valuesToCollection(values)));
      case "NOT":
         return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(values)).negate());
      default: // OR
         return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(values)));
      }
   }

   /**
    * Converts comma delimited values to a collection
    */
   private Collection<String> valuesToCollection(String values) {
      List<String> valueCollection = new ArrayList<>();
      if (values.contains(",")) {
         valueCollection.addAll(Arrays.asList(values.split(",")));
      } else {
         valueCollection.add(values);
      }
      return valueCollection;
   }

   /**
    * Retrieves a collection of features defined for a collection of system descriptor models
    *
    * @param commandOptions Jellyfish command options containing system descriptor
    * @param models         collection of system descriptor models to be processed
    */
   private Map<String, Feature> getAllFeatures(IJellyFishCommandOptions commandOptions, Collection<IModel> models) {
      HashSet<String> packages = new HashSet<>();
      TreeMap<String, Feature> features = new TreeMap<>(Collections.reverseOrder());

      models.forEach(model -> {
         String packagez = model.getParent().getName();
         if (packages.add(packagez)) {
            String modelPathURI = packagez.replace(".", "/");

            File
                     featureFilesRoot =
                     getFeatureFilesDirectory(commandOptions).toAbsolutePath().resolve(modelPathURI).toFile();

            for (File file : featureFilesRoot.listFiles()) {
               if (file.isFile()) {
                  String qualifiedName = RequirementsUtils.substringBetween(file.getName(), "", ".feature");
                  String name = RequirementsUtils.substringBetween(file.getName(), ".", ".");
                  features.put(qualifiedName, new Feature(qualifiedName, name));
               }
            }
         }
      });

      return features;
   }

   @Activate
   public void activate() {
      logService.trace(getClass(), "Activated");
   }

   @Deactivate
   public void deactivate() {
      logService.trace(getClass(), "Deactivated");
   }

   /**
    * Sets log service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC, unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }
}
