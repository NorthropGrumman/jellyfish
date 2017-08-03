package com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix;

import com.ngc.blocs.service.log.api.ILogService;
import com.ngc.seaside.bootstrap.utilities.console.api.ITableFormat;
import com.ngc.seaside.bootstrap.utilities.console.impl.stringtable.StringTable;
import com.ngc.seaside.command.api.DefaultParameter;
import com.ngc.seaside.command.api.DefaultUsage;
import com.ngc.seaside.command.api.IUsage;
import com.ngc.seaside.jellyfish.api.IJellyFishCommand;
import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.INamedChild;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.traversal.ModelPredicates;
import com.ngc.seaside.systemdescriptor.model.api.traversal.Traversals;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Component(service = IJellyFishCommand.class)
public class RequirementsAllocationMatrixCommand implements IJellyFishCommand {
   public static enum OutputFormatType {
      DEFAULT("default"), CSV("csv");

      public final String type;

      OutputFormatType(String type) {
         this.type = type;
      }
   }

   private static final String NAME = "requirements-allocation-matrix";
   private static final IUsage USAGE = createUsage();

   public static final String OUTPUT_FORMAT_PROPERTY = "outputFormat";
   public static final String OUTPUT_PROPERTY = "output";
   public static final String SCOPE_PROPERTY = "scope";
   public static final String VALUES_PROPERTY = "values";
   public static final String OPERATOR_PROPERTY = "operator";

   public static final String DEFAULT_OUTPUT_FORMAT_PROPERTY = OutputFormatType.DEFAULT.type;
   public static final String DEFAULT_OUTPUT_PROPERTY = "STDOUT";
   public static final String DEFAULT_SCOPE_PROPERTY = "model.metadata.json.stereotypes";
   public static final String DEFAULT_VALUES_PROPERTY = "service";
   public static final String DEFAULT_OPERATOR_PROPERTY = "OR";

   private ILogService logService;

   @Override
   public void run(IJellyFishCommandOptions commandOptions) {
      String outputFormat = evaluateOutputFormat(commandOptions);
      String output = evaluateOutput(commandOptions);
      String scope = evaluateScope(commandOptions); // default stereotypes
      String values = evaluateValues(commandOptions); // default service
      String operator = evaluateOperator(commandOptions);

      // Collection<IModel> models = searchModels(commandOptions, scope, values, operator);
      Collection<IModel> models = searchModelsByStereotypes(commandOptions, values, operator);
       Collection<Requirement> requirements = searchForRequirements(models);

      String report = "";
      if (outputFormat.equalsIgnoreCase("csv")) {
         // report = generateCsvVerificationMatrix(satisfiedRequirements, features.keySet());
      } else {
         report = String.valueOf(generateDefaultAllocationMatrix(requirements, models));
      }

      if (output.equalsIgnoreCase(DEFAULT_OUTPUT_PROPERTY)) {
         logService.info(RequirementsAllocationMatrixCommand.class, "Printing report to console...");
         System.out.println("\nOUTPUT:\n" + report);
      } else {
         Path outputPath = Paths.get(output);

         printReportToFile(report, output);
         logService.info(RequirementsAllocationMatrixCommand.class, "Printing report to location: %s", outputPath.toAbsolutePath().toString());
      }

      logService.info(RequirementsAllocationMatrixCommand.class, "%s requirements allocation matrix successfully created", values);

   }

   private Collection<Requirement> searchForRequirements(Collection<IModel> models) {
      final Map<String, Requirement> requirementsMap = new TreeMap<>();

      models.forEach(m -> searchForRequirements(m, requirementsMap));

      return requirementsMap.values();
   }

   private Map<String, Requirement> searchForRequirements(IModel model, Map<String, Requirement> requirementsMap) {
      Set<String> requirementsSet = new HashSet<>();

      requirementsSet.addAll(searchForRequirementsInMetadata(model.getMetadata()));

      model.getInputs().forEach(item -> requirementsSet.addAll(searchForRequirementsInMetadata(item.getMetadata())));
      model.getOutputs().forEach(item -> requirementsSet.addAll(searchForRequirementsInMetadata(item.getMetadata())));
      model.getParts().forEach(item -> requirementsSet.addAll(searchForRequirementsInMetadata(item.getMetadata())));
      model.getScenarios().forEach(item -> requirementsSet.addAll(searchForRequirementsInMetadata(item.getMetadata())));

      for (Object eachReqNameObj : requirementsSet.toArray()) {
         String eachReqName = (String) eachReqNameObj;
         if (!requirementsMap.containsKey(eachReqName) || 
             requirementsMap.get(eachReqName) == null) {
            requirementsMap.put(eachReqName, new Requirement(eachReqName));
         }

         requirementsMap.get(eachReqName).addModel(model);
      }
      
//      requirementsSet.forEach(eachReqName -> {
//         if (!requirementsMap.containsKey(eachReqName) || 
//             requirementsMap.get(eachReqName) == null) {
//            requirementsMap.put(eachReqName, new Requirement(eachReqName));
//         }
//
//         requirementsMap.get(eachReqName).addModel(model);
//      });

      return requirementsMap;
   }

   private Collection<String> searchForRequirementsInMetadata(IMetadata metadata) {
      Collection<String> requirements = new ArrayList<>();
      if (null == metadata || null == metadata.getJson() || null == metadata.getJson().get("satisfies")) {
         return requirements;
      }

      @SuppressWarnings("unchecked")
      Collection<String> satisfiesList = (Collection<String>) metadata.getJson().get("satisfies");
      requirements.addAll(satisfiesList);

      return requirements;
   }

   /**
    * Generates a requirements allocation matrix given a Collection of requirements and models
    *
    * @param requirements satisfied requirements
    * @param models models satisfying requirements
    * @return a {@link StringTable} containing verification matrix
    */

   private StringTable<Requirement> generateDefaultAllocationMatrix(Collection<Requirement> requirements, Collection<IModel> models) {
      StringTable<Requirement> stringTable = createStringTable(models);

      requirements.forEach(eachRequirement -> stringTable.getModel().addItem(eachRequirement));

      stringTable.setRowSpacer("_");
      stringTable.setColumnSpacer("|");
      stringTable.setShowHeader(true);

      return stringTable;
   }

   /**
    * Creates a {@link StringTable} for {@link Requirement} objects
    *
    * @param models models to compare against each {@link Requirement}
    */
   protected StringTable<Requirement> createStringTable(Collection<IModel> models) {
      return new StringTable<>(createTableFormat(models));
   }

   /**
    * Creates a {@link ITableFormat} for {@link Requirement} objects
    *
    * @param models models to compare against each {@link Requirement}
    */
   private ITableFormat<Requirement> createTableFormat(Collection<IModel> models) {
      return new RequirementItemFormat(models);
   }

   // /**
   // * Verifies which feature files meets which requirement.
   // *
   // * @param models models to search through for requirements
   // * @param featureMap features to look for
   // * @return returns a Collection of {@link Requirement}s
   // */
   // private Collection<Requirement> verifyRequirements(Collection<IModel> models, Map<String, Feature> featureMap) {
   // TreeMultimap<String, String>
   // requirementsMap =
   // TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());
   //
   // featureMap.forEach((featureFileName, feature) -> {
   // IModel model = models.stream().filter(aModel -> feature.getFullyQualifiedName().startsWith(aModel.getName()))
   // .findAny().orElse(null);
   //
   // if (model != null) {
   //
   // IScenario scenario = model.getScenarios().getByName(feature.getName()).orElse(null);
   // // A feature file should be considered to verify a requirement:
   // if (scenario != null) {
   // // if the model that contains the scenario has a "satisfies" metadata file for some requirement
   // feature.addRequirements(RequirementsUtils.getRequirementsFromModel(model, REQUIREMENTS_MEMBER_NAME));
   //
   // // if the scenario in the model has a "satisfies" metadata field
   // feature.addRequirements(
   // RequirementsUtils.getRequirementsFromScenario(scenario, REQUIREMENTS_MEMBER_NAME));
   // }
   // feature.getRequirements()
   // .forEach(requirement -> requirementsMap.put(requirement, feature.getFullyQualifiedName()));
   // }
   // });
   //
   // return createVerifiedRequirements(requirementsMap);
   // }

   // /**
   // * Returns a collection of models that matches the search criteria
   // *
   // * @param commandOptions Jellyfish command options containing system descriptor
   // * @param stereotypes the stereotypes for which to search
   // * @param operator the operator to apply to search
   // */
   // private Collection<IModel> searchModels(IJellyFishCommandOptions commandOptions, String scope, String values, String operator) {
   // switch (scope) {
   // case "model.input": return searchModelsByInputs( commandOptions, values, operator);
   // case "model.output": return searchModelsByOutputs( commandOptions, values, operator);
   // case "model.parts": return searchModelsByParts( commandOptions, values, operator);
   // case "model.scenario": return searchModelsByScenarios( commandOptions, values, operator);
   // case "model.metadata.json.stereotypes": return searchModelsByStereotypes(commandOptions, values, operator);
   // // TODO how to handle invalid scope?
   // default: throw new IllegalArgumentException("Invalid scope: " + scope);
   // }
   // }

   private Collection<IModel> searchModelsByStereotypes(IJellyFishCommandOptions commandOptions, String sterotypes, String operator) {
      ISystemDescriptor sd = commandOptions.getSystemDescriptor();

      switch (operator) {
      case "AND":
         return Traversals.collectModels(sd, ModelPredicates.withAllStereotypes(valuesToCollection(sterotypes)));
      case "NOT":
         return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)).negate());
      default: // OR
         return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)));
      }
   }

   // private Collection<IModel> searchModelsByInputs(IJellyFishCommandOptions commandOptions, String inputs, String operator) {
   // ISystemDescriptor sd = commandOptions.getSystemDescriptor();
   //
   // switch (operator) {
   // case "AND":
   // //return Traversals.collectModels(sd, ModelPredicates.withAllStereotypes(valuesToCollection(sterotypes)));
   // case "NOT":
   // //return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)).negate());
   // default: // OR
   // //return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)));
   // }
   // }
   //
   // private Collection<IModel> searchModelsByOutputs(IJellyFishCommandOptions commandOptions, String outputs, String operator) {
   // ISystemDescriptor sd = commandOptions.getSystemDescriptor();
   //
   // switch (operator) {
   // case "AND":
   // //return Traversals.collectModels(sd, ModelPredicates.withAllStereotypes(valuesToCollection(sterotypes)));
   // case "NOT":
   // //return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)).negate());
   // default: // OR
   // //return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)));
   // }
   // }
   //
   // private Collection<IModel> searchModelsByParts(IJellyFishCommandOptions commandOptions, String parts, String operator) {
   // ISystemDescriptor sd = commandOptions.getSystemDescriptor();
   //
   // switch (operator) {
   // case "AND":
   // //return Traversals.collectModels(sd, ModelPredicates.withAllStereotypes(valuesToCollection(sterotypes)));
   // case "NOT":
   // //return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)).negate());
   // default: // OR
   // //return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)));
   // }
   // }
   //
   // private Collection<IModel> searchModelsByScenarios(IJellyFishCommandOptions commandOptions, String scenarios, String operator) {
   // ISystemDescriptor sd = commandOptions.getSystemDescriptor();
   //
   // switch (operator) {
   // case "AND":
   // //return Traversals.collectModels(sd, ModelPredicates.withAllStereotypes(valuesToCollection(sterotypes)));
   // case "NOT":
   // //return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)).negate());
   // default: // OR
   // //return Traversals.collectModels(sd, ModelPredicates.withAnyStereotype(valuesToCollection(sterotypes)));
   // }
   // }

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
    * Prints the allocation matrix report to the file provided by the output
    *
    * @param report allocation matrix to be printed
    * @param output file location for output
    */
   private void printReportToFile(String report, String output) {
      // TODO: implement
      // FileUtilities.addLinesToFile();
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public IUsage getUsage() {
      return USAGE;
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

   /**
    * Create the usage for this command.
    *
    * @return the usage.
    */
   @SuppressWarnings("rawtypes")
   private static IUsage createUsage() {
      return new DefaultUsage("Description of requirements-allocation-matrix command",
         new DefaultParameter(OUTPUT_FORMAT_PROPERTY).setDescription("Format of the output. The possible values are default and csv. Default: default.").setRequired(false),
         new DefaultParameter(OUTPUT_PROPERTY).setDescription("File where the output is sent. This file path may be a relative path relative to the SD project directory. Default: stdout.")
                  .setRequired(false),
         new DefaultParameter(SCOPE_PROPERTY).setDescription("Keyword scope (metadata, input, output, etc..). Default: model.metadata.json.stereotypes.").setRequired(false),
         new DefaultParameter(VALUES_PROPERTY).setDescription("The values in which to search for in the scope defined. This is a comma separated string. Default: service.").setRequired(false),
         new DefaultParameter(OPERATOR_PROPERTY).setDescription("AND, OR, NOT (or their lowercase counterparts): should the items be AND'd together or OR'd together: default: OR.")
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
    * Retrieve the output property value based on user input. Default is: {@value #DEFAULT_OUTPUT_PROPERTY}
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
    * Retrieve the scope property value based on user input. Default is: {@value #DEFAULT_SCOPE_PROPERTY}
    *
    * @param commandOptions Jellyfish command options containing user params
    */
   private static String evaluateScope(IJellyFishCommandOptions commandOptions) {
      String scope = DEFAULT_SCOPE_PROPERTY;
      if (commandOptions.getParameters().containsParameter(SCOPE_PROPERTY)) {
         scope = commandOptions.getParameters().getParameter(SCOPE_PROPERTY).getStringValue();
      }
      return scope;
   }

   /**
    * Retrieve the values property value based on user input. Default is: {@value #DEFAULT_VALUES_PROPERTY}
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
    * Retrieve the operator property value based on user input. Default is: {@value #DEFAULT_OPERATOR_PROPERTY}
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

}
