package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.utilities.console.impl.stringtable.StringTable;
import com.ngc.seaside.command.api.CommandException;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities.MatrixUtils;
import com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities.ModelUtils;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
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
      return new DefaultUsage("Generates a requirements verification matrix for a given model stereotype.",
                              new DefaultParameter(OUTPUT_FORMAT_PROPERTY).setDescription(
                                       "Allows the user to define the output format. The possible values are default and csv.")
                                       .setRequired(false),
                              new DefaultParameter(OUTPUT_PROPERTY).setDescription(
                                       "Allows the user to define the file where the output will be stored. Default: prints to stdout."),
                              new DefaultParameter(VALUES_PROPERTY).setDescription(
                                       "The values in which to search as a comma separated string. Default: service.")
                                       .setRequired(false),
                              new DefaultParameter(OPERATOR_PROPERTY).setDescription(
                                       "AND, OR, NOT: determines if the items be AND'd together or OR'd together. Default: OR.")
                                       .setRequired(false));
   }

   /**
    * Retrieve the output format property value based on user input. Default is a table formatted string.
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private String evaluateOutputFormat(IJellyFishCommandOptions commandOptions) {
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
   protected Path evaluateOutput(IJellyFishCommandOptions commandOptions) {
      Path output;
      if (commandOptions.getParameters().containsParameter(OUTPUT_PROPERTY)) {
         String outputUri = commandOptions.getParameters().getParameter(OUTPUT_PROPERTY).getStringValue();
         output = Paths.get(outputUri);

         if (!output.isAbsolute()) {
            output = commandOptions.getSystemDescriptorProjectPath().toAbsolutePath().resolve(outputUri);
         }

         return output.toAbsolutePath();
      } else {
         return null;
      }
   }

   /**
    * Retrieve the values property value based on user input. Default is: "service"
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private String evaluateValues(IJellyFishCommandOptions commandOptions) {
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
   private String evaluateOperator(IJellyFishCommandOptions commandOptions) {
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

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      String outputFormat = evaluateOutputFormat(commandOptions);
      Path outputPath = evaluateOutput(commandOptions);
      String values = evaluateValues(commandOptions);
      String operator = evaluateOperator(commandOptions);
      Collection<IModel> models = searchModels(commandOptions, values, operator);
      
      Map<String, Feature> features = getAllFeatures(commandOptions, models, GHERKIN_URI);
      Collection<Requirement> satisfiedRequirements = verifyRequirements(models, features);

      String report;
      if (outputFormat.equalsIgnoreCase("csv")) {
         report = generateCsvVerificationMatrix(satisfiedRequirements, features.keySet());
      } else {
         report = String.valueOf(generateDefaultVerificationMatrix(satisfiedRequirements, features.keySet()));
      }

      if (outputPath == null) {

         logService.info(getClass(), "Printing report to console...");
         printVerificationConsole(report);

      } else {
         logService.info(getClass(), "Printing report to location: %s ...",
                         outputPath.toAbsolutePath().toString());
         printVerificationMatrixToFile(outputPath, report);
      }

      logService.info(getClass(), "%s requirements verification matrix successfully created", values);

   }

   /**
    * Retrieves a collection of features defined for a collection of system descriptor models
    *
    * @param commandOptions Jellyfish command options containing system descriptor
    * @param models         collection of system descriptor models to be processed
    * @param uri            location of feature files relative to the system descriptor
    */
   private Map<String, Feature> getAllFeatures(IJellyFishCommandOptions commandOptions, Collection<IModel> models,
                                               String uri) {
      return ModelUtils.getAllFeatures(commandOptions, models, uri);
   }

   /**
    * Prints the verification matrix report to the file provided by the output
    *
    * @param report verification matrix to be printed
    */
   private void printVerificationConsole(String report) {
      MatrixUtils.printVerificationConsole(report);
   }

   /**
    * Prints the verification matrix report to the file provided by the output
    *
    * @param outputPath file path to output
    * @param report     verification matrix to be printed
    */
   private void printVerificationMatrixToFile(Path outputPath, String report) {
      try {
         MatrixUtils.printVerificationMatrixToFile(outputPath, report);
      } catch (IOException e) {
         logService.error(getClass(), "Unable to write to the file specified by path %s",
                          outputPath.toAbsolutePath().toString());
         throw new CommandException(String.format("Unable to write to the file specified by path %s",
                                                  outputPath.toAbsolutePath().toString()), e);
      }
   }

   /**
    * Generates a comma delimited requirements verification matrix given a Collection of requirements and features
    *
    * @param requirements satisfied requirements
    * @param features     satisfied features
    * @return a {@link StringTable} containing verification matrix
    */
   protected String generateCsvVerificationMatrix(Collection<Requirement> requirements, Collection<String> features) {
      return MatrixUtils.generateCsvVerificationMatrix(requirements, features);
   }

   /**
    * Generates a requirements verification matrix given a Collection of requirements and features
    *
    * @param requirements satisfied requirements
    * @param features     satisfied features
    * @return a {@link StringTable} containing verification matrix
    */
   protected StringTable<Requirement> generateDefaultVerificationMatrix(Collection<Requirement> requirements,
                                                                        Collection<String> features) {
      return MatrixUtils.generateDefaultVerificationMatrix(requirements, features);
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
               feature.addRequirements(ModelUtils.getRequirementsFromModel(model, REQUIREMENTS_MEMBER_NAME));

               // if the scenario in the model has a "satisfies" metadata field
               feature.addRequirements(
                        ModelUtils.getRequirementsFromScenario(scenario, REQUIREMENTS_MEMBER_NAME));
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
      return ModelUtils.searchModels(commandOptions, values, operator);
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
