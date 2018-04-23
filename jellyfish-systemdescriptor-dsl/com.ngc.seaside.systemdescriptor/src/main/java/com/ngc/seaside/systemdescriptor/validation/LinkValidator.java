package com.ngc.seaside.systemdescriptor.validation;

import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Input;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Output;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Requires;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

/**
 * Validates links are valid.
 */
public class LinkValidator extends AbstractUnregisteredSystemDescriptorValidator {

   @Inject
   private IQualifiedNameProvider nameProvider;

   /**
    * Validates the basic link declaration.
    */
   @Check
   public void checkLinkDeclaration(BaseLinkDeclaration link) {
      checkForValidLinks(link);
      checkForTypeSafeLinks(link);
      checkForDuplicateLinks(link);

      // If the link doesn't have a name no reason to check for duplicates
      // names
      if (link.getName() != null) {
         checkUsageOfEscapeHatCharacterInLinkName(link);
         checkForDuplicateLinkNames(link);
         checkForLinkNameThatDuplicatesRequirementName(link);
         checkForLinkNameThatDuplicatesInputName(link);
         checkForLinkNameThatDuplicatesOutputName(link);
         checkForLinkNameThatDuplicatesPartName(link);
         checkForLinkNameThatDuplicatesScenarioName(link);
      }

      // Part -> Part or Requirement -> Requirement
      if (isLinkableExpression(link.getSource()) && isLinkableExpression(link.getTarget())) {
         checkForPartOutputField_To_PartOutputField(link);
         checkForPartInputField_To_PartOutputField(link);
      }

      checkForPartOutputField_To_InputField(link);
      checkForInputField_To_PartOutputField(link);
      checkForOutputField_To_PartOutputField(link);
      checkForInputField_To_OutputField(link);
      checkForOutputField_To_PartInputField(link);
   }

   /**
    * Ensures the given link is valid and references a valid source and target.
    */
   protected void checkForValidLinks(BaseLinkDeclaration link) {
      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      // Make sure the field does not link to itself.
      if (sourceField.equals(targetField)) {
         String msg = String.format(
               "The field '%s' cannot be linked to itself.",
               sourceField.getName());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);
      }

      // Make sure the fields are not both inputs, outputs, or
      // requirements of the same model.  Note linking parts to 
      // parts is allowed.
      if (sourceField.eContainer().equals(targetField.eContainer())
            && !(sourceField instanceof PartDeclaration
                       && targetField instanceof PartDeclaration)) {
         String msg = String.format(
               "Cannot link %s '%s' to another %s of the same model.",
               sourceField.eContainer().eClass().getName().toLowerCase(),
               sourceField.getName(),
               targetField.eContainer().eClass().getName().toLowerCase());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);
      }
   }

   /**
    * Ensures the types of the source and target of the link match.
    */
   protected void checkForTypeSafeLinks(BaseLinkDeclaration link) {
      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      String sourceTypeName = getFieldTypeName(sourceField);
      String targetTypeName = getFieldTypeName(targetField);

      if (!sourceTypeName.equals(targetTypeName)) {
         String msg = String.format(
               "Type of link target ('%s') must match type of link source ('%s'): %s != %s.",
               targetField.getName(),
               sourceField.getName(),
               targetTypeName,
               sourceTypeName);
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);
      }
   }

   /**
    * Ensures duplicate links have not been declared.
    */
   protected void checkForDuplicateLinks(LinkDeclaration link) {
      // TODO TH: declare duplicate links as a warning, not an error.
   }

   /**
    * Ensures the link name is not a keyword.
    */
   protected void checkUsageOfEscapeHatCharacterInLinkName(LinkDeclaration link) {
      // Verify the link name doesn't not have the escape hat
      if (link.getName().indexOf('^') >= 0) {
         String msg = String.format(
               "Cannot use '^' to escape the link name %s.",
               link.getName());
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }

   }

   /**
    * Ensures a link with the same name is not already declared.
    */
   protected void checkForDuplicateLinkNames(LinkDeclaration link) {
      Links links = (Links) link.eContainer();
      Model model = (Model) links.eContainer();

      if (getNumberOfLinksNamed(model, link.getName()) > 1) {
         String msg = String.format(
               "Invalid link name: a link named '%s' is already defined for the model '%s'.",
               link.getName(),
               nameProvider.getFullyQualifiedName(model));
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }
   }

   /**
    * Ensures a link does not duplicate the name of a requirement.
    */
   protected void checkForLinkNameThatDuplicatesRequirementName(LinkDeclaration link) {
      Links links = (Links) link.eContainer();
      Model model = (Model) links.eContainer();

      if (getNumberOfRequirementsNamed(model, link.getName()) > 0) {
         String msg = String.format(
               "Invalid link name: a requirement named '%s' is already defined for the model '%s'.",
               link.getName(),
               nameProvider.getFullyQualifiedName(model));
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }
   }

   /**
    * Ensures a link does not duplicate the name of an input.
    */
   protected void checkForLinkNameThatDuplicatesInputName(LinkDeclaration link) {
      Links links = (Links) link.eContainer();
      Model model = (Model) links.eContainer();

      if (getNumberOfInputsNamed(model, link.getName()) > 0) {
         String msg = String.format(
               "Invalid link name: an input named '%s' is already defined for the model '%s'.",
               link.getName(),
               nameProvider.getFullyQualifiedName(model));
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }
   }

   /**
    * Ensures a link does not duplicate the name of an output.
    */
   protected void checkForLinkNameThatDuplicatesOutputName(LinkDeclaration link) {
      Links links = (Links) link.eContainer();
      Model model = (Model) links.eContainer();

      if (getNumberOfOutputsNamed(model, link.getName()) > 0) {
         String msg = String.format(
               "Invalid link name: an output named '%s' is already defined for the model '%s'.",
               link.getName(),
               nameProvider.getFullyQualifiedName(model));
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }
   }

   /**
    * Ensures a link does not duplicate the name of a part.
    */
   protected void checkForLinkNameThatDuplicatesPartName(LinkDeclaration link) {
      Links links = (Links) link.eContainer();
      Model model = (Model) links.eContainer();

      if (getNumberOfPartsNamed(model, link.getName()) > 0) {
         String msg = String.format(
               "Invalid link name: a part named '%s' is already defined for the model '%s'.",
               link.getName(),
               nameProvider.getFullyQualifiedName(model));
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }
   }

   /**
    * Ensures a link does not duplicate the name of a scenario.
    */
   protected void checkForLinkNameThatDuplicatesScenarioName(LinkDeclaration link) {
      Links links = (Links) link.eContainer();
      Model model = (Model) links.eContainer();

      if (getNumberOfScenariosNamed(model, link.getName()) > 0) {
         String msg = String.format(
               "Invalid link name: a scenario named '%s' is already defined for the model '%s'.",
               link.getName(),
               nameProvider.getFullyQualifiedName(model));
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }
   }

   /**
    * Ensures a link is semantically correct.
    */
   protected void checkForPartOutputField_To_PartOutputField(BaseLinkDeclaration link) {

      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      // Do not allow outputs to be linked to other outputs.
      if (sourceField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)
            && targetField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)) {
         String msg = String.format(
               "Cannot link the output field '%s' to another output field '%s'.",
               sourceField.getName(),
               targetField.getName());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);

      }
   }

   /**
    * Ensures a link is semantically correct.
    */
   protected void checkForPartInputField_To_PartOutputField(BaseLinkDeclaration link) {
      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      // Do not allow linking an input to an output.
      if (sourceField.eClass().equals(SystemDescriptorPackage.Literals.INPUT_DECLARATION)
            && targetField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)) {

         String msg = String.format(
               "Cannot link the input field '%s' directly to an output field '%s'.",
               sourceField.getName(),
               targetField.getName());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);

      }
   }

   /**
    * Ensures a link is semantically correct.
    */
   protected void checkForPartOutputField_To_InputField(BaseLinkDeclaration link) {
      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      // Do not allow an expression of an output field to link to an input
      // field of
      // the model that contains the link.
      if (isLinkableExpression(source)
            && isFieldReference(target)
            && sourceField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)
            && targetField.eClass().equals(SystemDescriptorPackage.Literals.INPUT_DECLARATION)) {

         String msg = String.format(
               "Cannot link the output '%s' to this model's own input '%s'.",
               sourceField.getName(),
               targetField.getName());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);

      }
   }

   /**
    * Ensures a link is semantically correct.
    */
   protected void checkForInputField_To_PartOutputField(BaseLinkDeclaration link) {
      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      // Do not allow an input field of the model to link to an expression of
      // an outout field.
      if (isFieldReference(source)
            && isLinkableExpression(target)
            && sourceField.eClass().equals(SystemDescriptorPackage.Literals.INPUT_DECLARATION)
            && targetField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)) {

         String msg = String.format(
               "Cannot link the input field '%s' directly to an output field '%s'.",
               sourceField.getName(),
               targetField.getName());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);

      }
   }

   /**
    * Ensures a link is semantically correct.
    */
   protected void checkForOutputField_To_PartOutputField(BaseLinkDeclaration link) {
      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      // Do not allow outputs to be linked to outputs.
      if (isFieldReference(source)
            && isLinkableExpression(target)
            && sourceField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)
            && targetField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)) {

         String msg = String.format(
               "Cannot link the output field '%s' to another output field '%s'.",
               sourceField.getName(),
               targetField.getName());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);

      }
   }

   /**
    * Ensures a link is semantically correct.
    */
   protected void checkForOutputField_To_PartInputField(BaseLinkDeclaration link) {
      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      if (isFieldReference(source)
            && isLinkableExpression(target)
            && sourceField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)
            && targetField.eClass().equals(SystemDescriptorPackage.Literals.INPUT_DECLARATION)) {

         String msg = String.format(
               "Cannot link the output field '%s' of the current model to any input field of a part or requirement.",
               sourceField.getName());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__SOURCE);
      }
   }

   /**
    * Ensures a link is semantically correct.
    */
   protected void checkForInputField_To_OutputField(BaseLinkDeclaration link) {
      LinkableReference source = link.getSource();
      LinkableReference target = link.getTarget();

      FieldDeclaration sourceField = resolveField(source);
      FieldDeclaration targetField = resolveField(target);

      // Do not allow linking inputs to outputs.
      if (isFieldReference(source)
            && isFieldReference(target)
            && sourceField.eClass().equals(SystemDescriptorPackage.Literals.INPUT_DECLARATION)
            && targetField.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)) {

         String msg = String.format(
               "Cannot link the input field '%s' directly to an output field '%s'.",
               sourceField.getName(),
               targetField.getName());
         error(msg, link, SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);

      }
   }

   private String getFieldTypeName(FieldDeclaration field) {
      if (field.eClass().equals(SystemDescriptorPackage.Literals.INPUT_DECLARATION)) {
         return nameProvider.getFullyQualifiedName(((InputDeclaration) field).getType()).toString();
      } else if (field.eClass().equals(SystemDescriptorPackage.Literals.OUTPUT_DECLARATION)) {
         return nameProvider.getFullyQualifiedName(((OutputDeclaration) field).getType()).toString();
      }
      return "";
   }

   private static FieldDeclaration resolveField(LinkableReference ref) {
      FieldDeclaration field;
      if (ref.eClass().equals(SystemDescriptorPackage.Literals.FIELD_REFERENCE)) {
         field = ((FieldReference) ref).getFieldDeclaration();
      } else {
         // Must be an expression.
         LinkableExpression casted = (LinkableExpression) ref;
         field = casted.getTail();
      }
      return field;
   }

   private static boolean isLinkableExpression(LinkableReference ref) {
      return ref.eClass().equals(SystemDescriptorPackage.Literals.LINKABLE_EXPRESSION);
   }

   private static boolean isFieldReference(LinkableReference ref) {
      return !isLinkableExpression(ref);
   }

   private static int getNumberOfLinksNamed(
         Model model,
         String linkName) {
      Links links = model.getLinks();
      return links == null
             ? 0
             : (int) links.getDeclarations()
                   .stream()
                   .filter(d -> d.getName().equals(linkName))
                   .count();
   }

   private static int getNumberOfRequirementsNamed(
         Model model,
         String name) {
      Requires requirements = model.getRequires();
      return requirements == null
             ? 0
             : (int) requirements.getDeclarations()
                   .stream()
                   .filter(d -> d.getName().equals(name))
                   .count();
   }

   private static int getNumberOfInputsNamed(
         Model model,
         String name) {
      Input inputs = model.getInput();
      return inputs == null
             ? 0
             : (int) inputs.getDeclarations()
                   .stream()
                   .filter(d -> d.getName().equals(name))
                   .count();
   }

   private static int getNumberOfOutputsNamed(
         Model model,
         String name) {
      Output outputs = model.getOutput();
      return outputs == null
             ? 0
             : (int) outputs.getDeclarations()
                   .stream()
                   .filter(d -> d.getName().equals(name))
                   .count();
   }

   private static int getNumberOfPartsNamed(
         Model model,
         String name) {
      Parts parts = model.getParts();
      return parts == null
             ? 0
             : (int) parts.getDeclarations()
                   .stream()
                   .filter(d -> d.getName().equals(name))
                   .count();
   }

   private static int getNumberOfScenariosNamed(
         Model model,
         String name) {
      return model.getScenarios().isEmpty()
             ? 0
             : (int) model.getScenarios()
                   .stream()
                   .filter(d -> d.getName().equals(name))
                   .count();
   }

}
