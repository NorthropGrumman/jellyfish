package com.ngc.seaside.jellyfish.cli.command.report.requirementsverification;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;
import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.jellyfish.api.CommandException;
import com.ngc.seaside.jellyfish.api.DefaultParameter;
import com.ngc.seaside.jellyfish.api.DefaultUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.jellyfish.api.IUsage;
import com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities.MatrixUtils;
import com.ngc.seaside.jellyfish.cli.command.report.requirementsverification.utilities.ModelUtils;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureInformation;
import com.ngc.seaside.jellyfish.service.feature.api.IFeatureService;
import com.ngc.seaside.jellyfish.service.requirements.api.IRequirementsService;
import com.ngc.seaside.jellyfish.utilities.console.impl.stringtable.StringTable;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component(service = IJellyFishCommand.class)
public class RequirementsVerificationMatrixCommand implements IJellyFishCommand {

   /**
    * The JSON key refereed in the metadata of a model to declare requirements.  The value is either a string or an
    * array of strings.
    */
   private static final String NAME = "requirements-verification-matrix";
   static final String OUTPUT_FORMAT_PROPERTY = "outputFormat";
   static final String DEFAULT_OUTPUT_FORMAT_PROPERTY = "DEFAULT";
   static final String OUTPUT_PROPERTY = "output";
   static final String VALUES_PROPERTY = "values";
   static final String DEFAULT_VALUES_PROPERTY = "service";
   static final String OPERATOR_PROPERTY = "operator";
   private static final IUsage USAGE = createUsage();
   static final String DEFAULT_OPERATOR_PROPERTY = "OR";

   private ILogService logService;
   private IFeatureService featureService;
   private IRequirementsService requirementsService;

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @SuppressWarnings("rawtypes")
   private static IUsage createUsage() {
      return new DefaultUsage("Generates a requirements verification matrix for a given model stereotype.",
                              new DefaultParameter(OUTPUT_FORMAT_PROPERTY).setDescription(
                                    "Allows the user to define the output format. "
                                    + "The possible values are default and csv.")
                                    .setRequired(false),
                              new DefaultParameter(OUTPUT_PROPERTY).setDescription(
                                    "Allows the user to define the file where the output will be stored. "
                                    + "Default: prints to stdout."),
                              new DefaultParameter(VALUES_PROPERTY).setDescription(
                                    "The values in which to search as a comma separated string. Default: service.")
                                    .setRequired(false),
                              new DefaultParameter(OPERATOR_PROPERTY).setDescription(
                                    "AND, OR, NOT: determines if the items be AND'd together or OR'd together. "
                                    + "Default: OR.")
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
      if (commandOptions.getParameters().containsParameter(OUTPUT_PROPERTY)) {
         String outputUri = commandOptions.getParameters().getParameter(OUTPUT_PROPERTY).getStringValue();
         return Paths.get(outputUri);
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

      Map<Path, IFeatureInformation>
            features = featureService.getAllFeatures(commandOptions)
            .stream()
            .filter(feature -> models.contains(feature.getModel().orElse(null)))
            .collect(Collectors.toMap(IFeatureInformation::getPath, Function.identity()));
      ArrayList<String> fullyQualifiedFeatureNameList = new ArrayList<String>();
      for (Entry<Path, IFeatureInformation> featureInfo : features.entrySet()) {
         fullyQualifiedFeatureNameList.add(featureInfo.getValue().getFullyQualifiedName());
      }
      Collections.sort(fullyQualifiedFeatureNameList, Collections.reverseOrder());

      Collection<Requirement> satisfiedRequirements = verifyRequirements(commandOptions, models, features);

      String report;
      if (outputFormat.equalsIgnoreCase("csv")) {
         report = generateCsvVerificationMatrix(satisfiedRequirements, fullyQualifiedFeatureNameList);
      } else {
         report =
               String.valueOf(generateDefaultVerificationMatrix(satisfiedRequirements, fullyQualifiedFeatureNameList));
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
    * @param models   models to search through for requirements
    * @param features features to look for
    * @return returns a Collection of {@link Requirement}s
    */
   private Collection<Requirement> verifyRequirements(IJellyFishCommandOptions commandOptions,
                                                      Collection<IModel> models,
                                                      Map<Path, IFeatureInformation> features) {
      TreeMultimap<String, String>
            requirementsMap =
            TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());

      features.forEach((featureFileName, featureInfo) -> {
         IModel
               model =
               models.stream().filter(aModel -> featureInfo.getFullyQualifiedName().startsWith(aModel.getName()))
                     .findAny().orElse(null);

         if (model != null) {

            IScenario scenario = featureInfo.getScenario().orElse(null);

            // A feature file should be considered to verify a requirement:
            if (scenario != null) {

               for (String scenarioReq : requirementsService.getRequirements(commandOptions, scenario)) {
                  requirementsMap.put(scenarioReq, featureInfo.getFullyQualifiedName());
               }
               for (String modelReq : requirementsService.getRequirements(commandOptions, model)) {
                  requirementsMap.put(modelReq, featureInfo.getFullyQualifiedName());
               }
            }
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
      TreeSet<Requirement> requirements = new TreeSet<>(Collections.reverseOrder());
      requirementsMap.keySet().forEach(requirement -> {
         Requirement helper = new Requirement(requirement);
         helper.addFeatures(requirementsMap.get(requirement));
         requirements.add(helper);
      });
      return requirements;
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
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeLogService")
   public void setLogService(ILogService ref) {
      this.logService = ref;
   }

   /**
    * Remove log service.
    */
   public void removeLogService(ILogService ref) {
      setLogService(null);
   }

   /**
    * Sets feature service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeFeatureService")
   public void setFeatureService(IFeatureService ref) {
      this.featureService = ref;
   }

   /**
    * Remove feature service.
    */
   public void removeFeatureService(IFeatureService ref) {
      setFeatureService(null);
   }

   /**
    * Sets requirements service.
    *
    * @param ref the ref
    */
   @Reference(cardinality = ReferenceCardinality.MANDATORY,
         policy = ReferencePolicy.STATIC,
         unbind = "removeRequirementsService")
   public void setRequirementsService(IRequirementsService ref) {
      this.requirementsService = ref;
   }

   /**
    * Remove requirements service.
    */
   public void removeRequiremensService(IRequirementsService ref) {
      setRequirementsService(null);
   }
}
