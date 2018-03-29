package com.ngc.seaside.systemdescriptor.validation;

import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataModel;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DeclarationDefinition;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Enumeration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Parts;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitiveDataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PrimitivePropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Properties;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueAssignment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PropertyValueExpressionPathSegment;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedDataModelFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ReferencedPropertyFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Requires;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.xtext.validation.Check;

import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

public class UnsetPropertiesValidator extends AbstractUnregisteredSystemDescriptorValidator {

   /**
    * Compares strings as qualified names, where strings are first compared by the number of {@code '.'}'s they
    * have, then compares each individual unqualified part of the two strings in order.
    */
   private static final Comparator<String> QUALIFIED_NAME_COMPARISON = (str1, str2) -> {
      String[] split1 = str1.split("\\.");
      String[] split2 = str2.split("\\.");
      int lengthComparison = Integer.compare(split1.length, split2.length);
      if (lengthComparison != 0) {
         return lengthComparison;
      }
      for (int n = 0; n < split1.length; n++) {
         int elementComparison = split1[n].compareTo(split2[n]);
         if (elementComparison != 0) {
            return elementComparison;
         }
      }
      return 0;
   };

   /**
    * Returns the name for a link if it has one; otherwise the source -> target expression.
    */
   private static final Function<LinkDeclaration, String> LINK_NAME_FUNCTION = link -> {
      String name = link.getName();
      if (name != null) {
         return name;
      }
      final String source;
      final String target;
      if (link instanceof BaseLinkDeclaration) {
         source = ((BaseLinkDeclaration) link).getSource().toString();
         target = ((BaseLinkDeclaration) link).getTarget().toString();
      } else if (link instanceof RefinedLinkDeclaration) {
         source = ((RefinedLinkDeclaration) link).getSource().toString();
         target = ((RefinedLinkDeclaration) link).getTarget().toString();
      } else {
         throw new IllegalStateException("Unknown link type: " + link.eClass());
      }
      return String.format("%s -> %s", source, target);
   };

   @Check
   public void checkModelPropertiesSet(Model model) {
      if (model.getRefinedModel() == null) {
         return;
      }
      Collection<PropertyFieldDeclaration> declarations = getAllProperties(model, Properties::getDeclarations);
      Collection<PropertyValueAssignment> assignments = getAllProperties(model, Properties::getAssignments);

      List<String> declarationStrings = getDeclarationsAsStrings(declarations);
      List<String> assignmentStrings = getAssignmentDeclarationsAsStrings(assignments);
      comparedPropertyDeclarationsWithAssignments(declarationStrings,
         assignmentStrings,
         model,
         expected -> String.format(
            "Refined models must set all properties; missing property value %s in model properties",
            expected));
   }

   @Check
   public void checkPartPropertiesSet(Model model) {
      if (model.getRefinedModel() == null) {
         return;
      }
      checkElementPropertiesSet(model,
         m -> Optional.ofNullable(m.getParts()).map(Parts::getDeclarations).orElse(null),
         PartDeclaration::getName,
         part -> Optional.ofNullable(part.getDefinition()).map(DeclarationDefinition::getProperties).orElse(null),
         (expected, name) -> String.format(
            "Refined models must set all properties; missing property value %s for part %s",
            expected,
            name));
   }

   @Check
   public void checkRequirePropertiesSet(Model model) {
      if (model.getRefinedModel() == null) {
         return;
      }
      checkElementPropertiesSet(model,
         m -> Optional.ofNullable(m.getRequires()).map(Requires::getDeclarations).orElse(null),
         RequireDeclaration::getName,
         req -> Optional.ofNullable(req.getDefinition()).map(DeclarationDefinition::getProperties).orElse(null),
         (expected, name) -> String.format(
            "Refined models must set all properties; missing property value %s for required model %s",
            expected,
            name));
   }

   @Check
   public void checkLinkPropertiesSet(Model model) {
      if (model.getRefinedModel() == null) {
         return;
      }
      checkElementPropertiesSet(model,
         m -> Optional.ofNullable(m.getLinks()).map(Links::getDeclarations).orElse(null),
         LINK_NAME_FUNCTION,
         link -> Optional.ofNullable(link.getDefinition()).map(DeclarationDefinition::getProperties).orElse(null),
         (expected, name) -> String.format(
            "Refined models must set all properties; missing property value %s for link %s",
            expected,
            name));
   }

   /**
    * Checks that all of the properties for each element within the model are set.
    * 
    * @param model model
    * @param getCollection function for getting the collection of elements from the model (such as parts or requires elements)
    * @param getName function for getting the name of the element within the model (such as part name or require name)
    * @param getProperties function for getting the properties from the element within the model
    * @param getErrorMessage function for getting the error message, given the missing declaration and the element name
    * @param <T> the type of element within the model (such as {@link PartDeclaration} or {@link RequireDeclaration})
    * @param errorMessageFunction
    */
   private <T> void checkElementPropertiesSet(Model model, Function<Model, Collection<T>> getCollection,
            Function<T, String> getName, Function<T, Properties> getProperties,
            BiFunction<String, String, String> getErrorMessage) {
      Map<String, Collection<PropertyFieldDeclaration>> declarationsMap = getAllProperties(model,
         getCollection,
         getName,
         element -> getProperties.apply(element).getDeclarations());
      Map<String, Collection<PropertyValueAssignment>> assignmentsMap = getAllProperties(model,
         getCollection,
         getName,
         element -> getProperties.apply(element).getAssignments());

      for (Map.Entry<String, Collection<PropertyFieldDeclaration>> entry : declarationsMap.entrySet()) {
         String name = entry.getKey();
         Collection<PropertyFieldDeclaration> declarations = entry.getValue();
         Collection<PropertyValueAssignment> assignments = assignmentsMap.getOrDefault(name, Collections.emptySet());
         List<String> declarationsAsStrings = getDeclarationsAsStrings(declarations);
         List<String> assignmentsAsStrings = getAssignmentDeclarationsAsStrings(assignments);
         comparedPropertyDeclarationsWithAssignments(declarationsAsStrings,
            assignmentsAsStrings,
            model,
            expected -> getErrorMessage.apply(expected, name));
      }
   }

   /**
    * Returns all of the properties for elements within the model, grouped by name.
    * 
    * @param model model
    * @param getCollection function for getting the collection of elements from the model (such as parts or requires elements)
    * @param getName function for getting the name of the element within the model (such as part name or require name)
    * @param getProperties function for getting the collection of properties from the element within the model
    * @param <T> the type of element within the model (such as {@link PartDeclaration} or {@link RequireDeclaration})
    * @param <U> the type of property (such as {@link PropertyFieldDeclaration} or {@link #PropertyValueAssignment})
    * @return all of the properties for elements within the model, grouped by name
    */
   private static <T, U> Map<String, Collection<U>> getAllProperties(
            Model model,
            Function<Model, Collection<T>> getCollection,
            Function<T, String> getName,
            Function<T, Collection<U>> getProperties) {
      Map<String, Collection<U>> map = new LinkedHashMap<>();

      Model current = model;
      while (current != null) {
         Collection<T> collection = getCollection.apply(current);
         if (collection != null) {
            for (T element : collection) {
               String name = getName.apply(element);
               Collection<U> properties = getProperties.apply(element);
               if (properties != null) {
                  map.computeIfAbsent(name, __ -> new ArrayList<>()).addAll(properties);
               }
            }
         }
         current = current.getRefinedModel();
      }
      return map;
   }

   /**
    * Returns all of the model properties.
    * 
    * @param model model
    * @param getProperties function for getting the collection of properties from the model
    * @param <U> the type of property (such as {@link PropertyFieldDeclaration} or {@link #PropertyValueAssignment})
    * @return all of the model properties
    */
   private static <U> Collection<U> getAllProperties(Model model, Function<Properties, Collection<U>> getProperties) {
      Collection<U> allProperties = new ArrayList<>();

      Model current = model;
      while (current != null) {
         Properties properties = current.getProperties();
         if (properties != null) {
            Collection<U> collection = getProperties.apply(properties);
            if (collection != null) {
               allProperties.addAll(collection);
            }
         }
         current = current.getRefinedModel();
      }

      return allProperties;
   }

   /**
    * Converts the given collection of property declarations to a sorted unique list of strings. The set will include all
    * declaration names and (for data property declarations) their nested field names, represented as qualified names.
    * 
    * @param declarations collection of property declaration
    * @return a sorted unique list of all assignments needed, in string form
    */
   private static List<String> getDeclarationsAsStrings(Collection<PropertyFieldDeclaration> declarations) {
      Set<String> declarationStrings = new HashSet<>();
      for (PropertyFieldDeclaration declaration : declarations) {
         if (declaration.getCardinality() == Cardinality.MANY) {
            // TODO: remove when cardinality is implemented
            continue;
         }
         if (declaration instanceof PrimitivePropertyFieldDeclaration) {
            declarationStrings.add(declaration.getName());
         } else if (declaration instanceof ReferencedPropertyFieldDeclaration) {
            ReferencedPropertyFieldDeclaration referencedDeclaration = (ReferencedPropertyFieldDeclaration) declaration;
            DataModel dataModel = referencedDeclaration.getDataModel();
            if (dataModel instanceof Data) {
               Queue<Map.Entry<String, Data>> datas = new ArrayDeque<>();
               datas.add(new AbstractMap.SimpleImmutableEntry<>(declaration.getName(), (Data) dataModel));
               while (!datas.isEmpty()) {
                  Map.Entry<String, Data> entry = datas.poll();
                  String suffix = entry.getKey();
                  Data data = entry.getValue();

                  for (DataFieldDeclaration field : data.getFields()) {
                     if (field.getCardinality() == Cardinality.MANY) {
                        // TODO: remove when cardinality is implemented
                        continue;
                     }
                     if (field instanceof PrimitiveDataFieldDeclaration) {
                        declarationStrings.add(suffix + '.' + field.getName());
                     } else if (field instanceof ReferencedDataModelFieldDeclaration) {
                        ReferencedDataModelFieldDeclaration referencedField = (ReferencedDataModelFieldDeclaration) field;
                        DataModel fieldModel = referencedField.getDataModel();
                        if (fieldModel instanceof Enumeration) {
                           declarationStrings.add(suffix + '.' + field.getName());
                        } else if (fieldModel instanceof Data) {
                           datas.add(
                              new AbstractMap.SimpleImmutableEntry<>(suffix + '.' + field.getName(), (Data) referencedField.getDataModel()));
                        } else {
                           throw new IllegalStateException("Unknown data model type: " + fieldModel.eClass());
                        }
                     } else {
                        throw new IllegalStateException("Unknown data field declaration: " + field.eClass());
                     }
                  }

               }
            } else if (dataModel instanceof Enumeration) {
               declarationStrings.add(declaration.getName());
            } else {
               throw new IllegalStateException("Unknown data model type: " + dataModel.eClass());
            }
         } else {
            throw new IllegalStateException("Unknown declaration type: " + declaration.eClass());
         }
      }
      List<String> list = new ArrayList<>(declarationStrings);
      Collections.sort(list, QUALIFIED_NAME_COMPARISON);
      return list;
   }

   /**
    * Converts the given collection of property assignments to a sorted unique list of strings. The set will follow the same
    * syntax as {@link #getDeclarationsAsStrings(Collection)} but only consist of the given assignments.
    * 
    * @param assignments collection of property assignments
    * @return a sorted unique list of all assignment declarations, in string form
    */
   private static List<String> getAssignmentDeclarationsAsStrings(
            Collection<PropertyValueAssignment> assignments) {
      Set<String> assignmentStrings = new HashSet<>();
      for (PropertyValueAssignment assignment : assignments) {
         PropertyValueExpression expression = assignment.getExpression();
         StringBuilder str = new StringBuilder();
         str.append(expression.getDeclaration().getName());
         Collection<PropertyValueExpressionPathSegment> segments = expression.getPathSegments();
         for (PropertyValueExpressionPathSegment segment : segments) {
            str.append('.').append(segment.getFieldDeclaration().getName());
         }
         assignmentStrings.add(str.toString());
      }
      List<String> list = new ArrayList<>(assignmentStrings);
      Collections.sort(list, QUALIFIED_NAME_COMPARISON);
      return list;
   }

   /**
    * Compares the given declarations to the given assignments (in the format returned by
    * {@link #getDeclarationsAsStrings(Collection)}). Reports at most one error if a discrepancy is found.
    * 
    * @param model model (used for error reporting)
    * @param declarations property declarations
    * @param assignments property assignment declarations
    */
   private void comparedPropertyDeclarationsWithAssignments(List<String> declarations,
            List<String> assignments, Model model, Function<String, String> errorMessage) {
      Iterator<String> expectedStrings = declarations.iterator();
      Iterator<String> actualStrings = assignments.iterator();

      while (expectedStrings.hasNext() && actualStrings.hasNext()) {
         String expected = expectedStrings.next();
         String actual = actualStrings.next();
         if (!expected.equals(actual)) {
            error(errorMessage.apply(expected),
               model,
               SystemDescriptorPackage.Literals.ELEMENT__NAME);
            return;
         }
      }

      if (expectedStrings.hasNext()) {
         String expected = expectedStrings.next();
         error("Refined models must set all properties; missing value for " + expected,
            model,
            SystemDescriptorPackage.Literals.ELEMENT__NAME);
      }
   }

}
