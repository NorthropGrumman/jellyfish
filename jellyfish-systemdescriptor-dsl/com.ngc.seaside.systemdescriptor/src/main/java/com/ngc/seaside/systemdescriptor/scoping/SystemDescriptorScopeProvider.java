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
package com.ngc.seaside.systemdescriptor.scoping;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpressionPathSegment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.utils.SdUtils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.Scopes;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The scope provider for the System Descriptor language.
 */
public class SystemDescriptorScopeProvider extends AbstractDeclarativeScopeProvider {

   /**
    * Places all declared properties of the current element as well as any
    * refined models in scope for property value expressions.
    *
    * @return the scope for a property value expression
    */
   public IScope scope_PropertyValueExpression_declaration(
            PropertyValueExpression context,
            EReference reference) {
      IScope scope = null;

      if (isExpressionForModelProperty(context)) {
         scope = getScopeForModelPropertyValueExpression(context);
      } else if (isExpressionForLinkProperty(context)) {
         scope = getScopeForLinkPropertyValueExpression(context);
      } else if (isExpressionForRequirementProperty(context)) {
         scope = getScopeForRequirementPropertyValueExpression(context);
      } else if (isExpressionForPartProperty(context)) {
         scope = getScopeForPartPropertyValueExpression(context);
      } else {
         scope = delegateGetScope(context, reference);
      }

      return scope;
   }

   /**
    * Places all data fields of a data type in scope for property path segment
    * where a value of a complex type is being set.
    *
    * @return the scope of the segment
    */
   public IScope scope_PropertyValueExpressionPathSegment_fieldDeclaration(
            PropertyValueExpressionPathSegment segment,
            EReference reference) {
      PropertyValueExpression exp = (PropertyValueExpression) segment.eContainer();
      Data data = getDataModelForProperty(exp.getDeclaration());

      for (int i = 0; i < exp.getPathSegments().indexOf(segment) && data != null; i++) {
         // Get the text value of the field. We have to do this because the
         // linking has not yet completed and proxy objects are set in the
         // data model.
         String fieldName = SdUtils.getRawSource(
                     exp.getPathSegments().get(i),
                     SystemDescriptorPackage.Literals.PROPERTY_VALUE_EXPRESSION_PATH_SEGMENT__FIELD_DECLARATION);

         // Get the field with that name. Then filter for fields that have a
         // complex data type (ie, not a primitive).
         DataFieldDeclaration dataField = SdUtils.findDataFieldDeclaration(data, fieldName);

         // Note that the data model can be an enumeration at this point
         // (if the user created an invalid path, the validators will
         // catch it after scoping is finished).
         data = dataField instanceof ReferencedDataModelFieldDeclaration
            && ((ReferencedDataModelFieldDeclaration) dataField).getDataModel() instanceof Data
                     ? (Data) ((ReferencedDataModelFieldDeclaration) dataField).getDataModel()
                     : null;
      }

      return data != null
               ? doCreateScope(SdUtils.getAllDataFields(data))
               : delegateGetScope(segment, reference);
   }

   /**
    * Places fields in scope for the given field reference. This is used to determine which fields of which model
    * should be placed in scope.
    *
    * @return the fields in the scope
    */
   public IScope scope_FieldReference_fieldDeclaration(FieldReference context, EReference reference) {
      IScope scope;
      if (context.eContainer() instanceof LinkDeclaration) {
         // This indicates the source or target of a link is directly
         // referencing an input or output. In the example below "a",
         // is the current context.
         // link a -> some.thing
         Model model = (Model) context.eContainer() // RefinedLinkDeclaration
                                      .eContainer() // Links
                                      .eContainer(); // Model
         scope = doCreateScope(getLinkableFieldsFrom(model));
      } else if (context.eContainer() instanceof LinkableExpression) {
         // This indicates the source or target of a link is an expression.
         // In the example below, "some", is the current context.
         // link a -> some.thing
         Model model = (Model) context.eContainer() // LinkableExpression
                                      .eContainer() // RefinedLinkDeclaration
                                      .eContainer() // Links
                                      .eContainer(); // Model
         scope = doCreateScope(getLinkableFieldsFrom(model));
      } else {
         scope = delegateGetScope(context, reference);
      }
      return scope;
   }

   /**
    * Provides scope for a link expression of the form
    * {@code link someInput to somePart.someMoreInput}.
    *
    * @return scope for a link expression
    */
   public IScope scope_LinkableExpression_tail(LinkableExpression context, EReference reference) {
      // Get the field reference thus far. If we are parsing the expression
      // link someInput to somePart.someMoreInput
      // than ref will equal the part "somePart".
      FieldReference ref = (FieldReference) context.getRef();
      // Get the field declaration the reference is pointing to.
      FieldDeclaration fieldDeclaration = ref.getFieldDeclaration();

      IScope scope;

      // We need to determine the type of the field declaration. Right now we
      // only support nested expressions for models. More requires and parts
      // can declare other models as fields. Input and output must declare
      // data as fields. Since we can't yet reference the contents of data,
      // we don't have to worry about referencing input or output.
      if (fieldDeclaration instanceof RequireDeclaration) {
         RequireDeclaration casted = (RequireDeclaration) fieldDeclaration;
         // Include all field declarations of the referenced model in the
         // scope.
         scope = doCreateScope(getLinkableFieldsFrom(SdUtils.getTypeOfRequireDeclaration(casted)));
      } else if (fieldDeclaration instanceof PartDeclaration) {
         // Include all field declarations of the referenced model in the
         // scope.
         PartDeclaration casted = (PartDeclaration) fieldDeclaration;
         scope = doCreateScope(getLinkableFieldsFrom(SdUtils.getTypeOfPartDeclaration(casted)));
      } else {
         // Otherwise, do the default behavior.
         scope = delegateGetScope(context, reference);
      }

      return scope;
   }

   /**
    * Invoked to actually create the scope.
    */
   private IScope doCreateScope(Iterable<? extends EObject> elements) {
      // Use this template method to change how the scope is created if necessary later on.
      return Scopes.scopeFor(elements);
   }

   /**
    * Gets scope for a property value expression that is part of a property declared directly
    * on a model.
    */
   private IScope getScopeForModelPropertyValueExpression(PropertyValueExpression context) {
      Collection<PropertyFieldDeclaration> properties = new ArrayList<>();
      Model model = SdUtils.getContainingModel(context);
      // Get all properties declared on the model and any base refined models.
      SdUtils.traverseModelRefinementHierarchy(model, m -> {
         if (m.getProperties() != null) {
            properties.addAll(m.getProperties().getDeclarations());
         }
      });
      return doCreateScope(properties);
   }

   /**
    * Gets scope for a property value expression that is part of a property declared on a link.
    */
   private IScope getScopeForLinkPropertyValueExpression(PropertyValueExpression context) {
      Collection<PropertyFieldDeclaration> propertyDeclarations = new ArrayList<>();
      LinkDeclaration link = SdUtils.getContainerOfType(context, LinkDeclaration.class);
      Model model = SdUtils.getContainingModel(link);

      SdUtils.traverseModelRefinementHierarchy(model, m -> {
         LinkDeclaration currentLink = findLink(m, link);
         // There is an issue when refining an unnamed link and setting a value on
         // a property of that link. If the link is refining another link, we need
         // to find the base link where the property is declared. This works fine
         // for links with names. However, if the link has no name we can find the
         // base link using the target and source. However, the definition on that
         // base link (which contains the properties) is null even thought that link
         // has properties declared in the source. This seems to be an XText issue
         // since the definition object should definitely not be null.
         if (currentLink != null
                     && currentLink.getDefinition() != null
                     && currentLink.getDefinition().getProperties() != null) {
            propertyDeclarations.addAll(currentLink.getDefinition().getProperties().getDeclarations());
         }
      });

      return doCreateScope(propertyDeclarations);
   }

   /**
    * Gets scope for a property value expression that is part of a property declared on a part.
    */
   private IScope getScopeForPartPropertyValueExpression(PropertyValueExpression context) {
      Collection<PropertyFieldDeclaration> propertyDeclarations = new ArrayList<>();
      PartDeclaration part = SdUtils.getContainerOfType(context, PartDeclaration.class);
      Model model = SdUtils.getContainingModel(part);

      SdUtils.traverseModelRefinementHierarchy(model, m -> {
         if (m.getParts() != null) {
            PartDeclaration currentPart = m.getParts()
                                           .getDeclarations()
                                           .stream()
                                           .filter(d -> d.getName().equals(part.getName()))
                                           .findFirst()
                                           .orElse(null);
            if (currentPart != null
                        && currentPart.getDefinition() != null
                        && currentPart.getDefinition().getProperties() != null) {
               propertyDeclarations.addAll(currentPart.getDefinition().getProperties().getDeclarations());
            }
         }
      });

      // Get properties declared on the model of the part itself.
      Model type = SdUtils.getTypeOfPartDeclaration(part);
      if (type.getProperties() != null) {
         propertyDeclarations.addAll(type.getProperties().getDeclarations());
      }

      return doCreateScope(propertyDeclarations);
   }

   /**
    * Gets scope for a property value expression that is part of a property declared on a requirement.
    */
   private IScope getScopeForRequirementPropertyValueExpression(PropertyValueExpression context) {
      Collection<PropertyFieldDeclaration> propertyDeclarations = new ArrayList<>();
      RequireDeclaration requirement = SdUtils.getContainerOfType(context, RequireDeclaration.class);
      Model model = SdUtils.getContainingModel(requirement);

      SdUtils.traverseModelRefinementHierarchy(model, m -> {
         if (m.getRequires() != null) {
            RequireDeclaration currentRequirement = m.getRequires()
                                                     .getDeclarations()
                                                     .stream()
                                                     .filter(d -> d.getName().equals(requirement.getName()))
                                                     .findFirst()
                                                     .orElse(null);
            if (currentRequirement != null
                        && currentRequirement.getDefinition() != null
                        && currentRequirement.getDefinition().getProperties() != null) {
               propertyDeclarations.addAll(currentRequirement.getDefinition().getProperties().getDeclarations());
            }
         }
      });

      // Get properties declared on the model of the requirement itself.
      Model type = SdUtils.getTypeOfRequireDeclaration(requirement);
      if (type.getProperties() != null) {
         propertyDeclarations.addAll(type.getProperties().getDeclarations());
      }

      return doCreateScope(propertyDeclarations);
   }

   /**
    * Gets all field declarations that can be referenced that are contained by
    * the given model.
    *
    * @return all field declarations that can be referenced that are contained
    *         by the given model
    */
   private static Collection<FieldDeclaration> getLinkableFieldsFrom(Model model) {
      Collection<FieldDeclaration> fields = new ArrayList<>();

      do {
         if (model.getInput() != null) {
            fields.addAll(model.getInput().getDeclarations());
         }
         if (model.getOutput() != null) {
            fields.addAll(model.getOutput().getDeclarations());
         }
         if (model.getRequires() != null) {
            fields.addAll(model.getRequires().getDeclarations());
         }
         if (model.getParts() != null) {
            fields.addAll(model.getParts().getDeclarations());
         }
         model = model.getRefinedModel();
      } while (model != null);

      return fields;
   }

   /**
    * Returns true if the given expression has been applied to a model property.
    */
   private static boolean isExpressionForModelProperty(PropertyValueExpression exp) {
      return exp.eContainer() // PropertyValueAssignment
                .eContainer() // Properties
                .eContainer() instanceof Model;
   }

   /**
    * Returns true if the given expression has been applied to a property of a link.
    */
   private static boolean isExpressionForLinkProperty(PropertyValueExpression exp) {
      return exp.eContainer() // PropertyValueAssignment
                .eContainer() // Properties
                .eContainer() // DeclarationsDefinition
                .eContainer() instanceof LinkDeclaration;
   }

   /**
    * Returns true if the given expression has been applied to a property if a part field.
    */
   private static boolean isExpressionForPartProperty(PropertyValueExpression exp) {
      return exp.eContainer() // PropertyValueAssignment
                .eContainer() // Properties
                .eContainer() // DeclarationsDefinition
                .eContainer() instanceof PartDeclaration;
   }

   /**
    * Returns true if the given expression has been applied to a property of a requirement field.
    */
   private static boolean isExpressionForRequirementProperty(PropertyValueExpression exp) {
      return exp.eContainer() // PropertyValueAssignment
                .eContainer() // Properties
                .eContainer() // DeclarationsDefinition
                .eContainer() instanceof RequireDeclaration;
   }

   /**
    * Gets the data type of the given property. This method can only be called for properties
    * whose type is a complex data type, not an enum or primitive.
    */
   private static Data getDataModelForProperty(PropertyFieldDeclaration declaration) {
      // Note it is possible that the declaration is a proxy. If so, abort.
      if (declaration.eIsProxy()) {
         return null;
      }

      Preconditions.checkState(
                  declaration instanceof ReferencedPropertyFieldDeclaration,
                  "expected the declaration to be an instance of ReferencedPropertyFieldDeclaration!"
                     + "  Otherwise, why would the declaration need scoping help?");
      ReferencedPropertyFieldDeclaration referencedDeclaration = (ReferencedPropertyFieldDeclaration) declaration;

      // More proxy checking.
      if (referencedDeclaration.getDataModel().eIsProxy()) {
         return null;
      }
      Preconditions.checkState(
                  referencedDeclaration.getDataModel() instanceof Data,
                  "expected the declaration to have a data model of data instead of an enumeration!"
                     + "  Otherwise, you can't have a complex expression!");
      return (Data) referencedDeclaration.getDataModel();
   }

   /**
    * Attempts to find the a link in the given model that matches the given link.
    *
    * @param model the model to search
    * @param linkDeclaration the link to use for reference when searching
    * @return a link in the model that matches the name or the source and target of
    *         the given link or {@code null} if there is no such link
    */
   private static LinkDeclaration findLink(Model model, LinkDeclaration linkDeclaration) {
      if (model.getLinks() == null) {
         return null;
      }

      if (linkDeclaration.getName() != null) {
         for (LinkDeclaration link : model.getLinks().getDeclarations()) {
            if (linkDeclaration.getName().equals(link.getName())) {
               return link;
            }

         }

         return null;
      }

      String sourcePath = getPathOfSource(linkDeclaration);
      String targetPath = getPathOfTarget(linkDeclaration);
      // Note sourcePat and targetPath must be non-null here because
      // the link does not have a name so the target and source must be
      // set.

      for (LinkDeclaration link : model.getLinks().getDeclarations()) {
         if (sourcePath.equals(getPathOfSource(link)) && targetPath.equals(getPathOfTarget(link))) {
            return link;
         }
      }

      return null;
   }

   /**
    * Gets the text value of the source of a link.
    */
   private static String getPathOfSource(LinkDeclaration link) {
      List<INode> nodes;

      if (link instanceof BaseLinkDeclaration) {
         nodes = NodeModelUtils.findNodesForFeature(link,
            SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__SOURCE);
      } else {
         nodes = NodeModelUtils.findNodesForFeature(link,
            SystemDescriptorPackage.Literals.REFINED_LINK_DECLARATION__SOURCE);
      }

      return nodes.isEmpty() ? null : NodeModelUtils.getTokenText(nodes.get(0));
   }

   /**
    * Gets the text value of the target of a link.
    */
   private static String getPathOfTarget(LinkDeclaration link) {
      List<INode> nodes;

      if (link instanceof BaseLinkDeclaration) {
         nodes = NodeModelUtils.findNodesForFeature(link,
            SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);
      } else {
         nodes = NodeModelUtils.findNodesForFeature(link,
            SystemDescriptorPackage.Literals.REFINED_LINK_DECLARATION__TARGET);
      }

      return nodes.isEmpty() ? null : NodeModelUtils.getTokenText(nodes.get(0));
   }
}
