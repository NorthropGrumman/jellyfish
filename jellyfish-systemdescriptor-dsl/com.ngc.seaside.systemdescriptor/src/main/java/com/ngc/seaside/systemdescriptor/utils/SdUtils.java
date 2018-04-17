package com.ngc.seaside.systemdescriptor.utils;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.InputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Contains various utilities for interacting directly with System Descriptor objects.
 */
public class SdUtils {

   private SdUtils() {
   }

   /**
    * Gets the raw System Descriptor source for given attribute of the given object.
    *
    * @return the raw System Descriptor source for given attribute of the given object
    */
   public static String getRawSource(EObject semanticObject, EStructuralFeature structuralFeature) {
      List<INode> nodes = NodeModelUtils.findNodesForFeature(semanticObject, structuralFeature);
      Preconditions.checkArgument(!nodes.isEmpty(),
                                  "could not find source for %s of object of type %s whose value is %s!",
                                  structuralFeature,
                                  semanticObject.getClass().getName(),
                                  semanticObject);
      return NodeModelUtils.getTokenText(nodes.get(0));
   }

   /**
    * Retrieves a container of the given type.
    *
    * @param obj   the object that is contained an instance of {@code clazz}
    * @param clazz the type of container to find
    * @return the container of the given type the given object belongs to
    * @throws IllegalArgumentException if the given object no container of the given type
    */
   public static <T extends EObject> T getContainerOfType(EObject obj, Class<T> clazz) {
      T container = EcoreUtil2.getContainerOfType(obj, clazz);
      Preconditions.checkArgument(container != null, "the object %s has no container of type %s!", obj, clazz);
      return container;
   }

   /**
    * Retrieves the model that this object belongs to.
    *
    * @param obj the object that is contained by a model
    * @return the model for this object
    * @throws IllegalArgumentException if the given object has not model container
    */
   public static Model getContainingModel(EObject obj) {
      return getContainerOfType(obj, Model.class);
   }

   /**
    * Traverse the refinement hierarchy of a model, invoking the given consumer on each model.
    *
    * @param model    the model whose hierarchy is being traversed
    * @param consumer the consumer to invoke for each model
    */
   public static void traverseModelRefinementHierarchy(Model model, Consumer<Model> consumer) {
      do {
         consumer.accept(model);
         model = model.getRefinedModel();
      } while (model != null);
   }

   /**
    * Traverse the extension hierarchy of a data type, invoking the given consumer on each data.
    *
    * @param data     the data whose hierarchy is being traversed
    * @param consumer the consumer to invoke for each data
    */
   public static void traverseDataExtensionHierarchy(Data data, Consumer<Data> consumer) {
      do {
         consumer.accept(data);
         data = data.getExtendedDataType();
      } while (data != null);
   }

   /**
    * Gets all the input declarations declared in the given model and all refined models.
    *
    * @param model the model to get the inputs for
    * @return all the input declarations declared in the given model and all refined models
    */
   public static Collection<InputDeclaration> getInputDeclarations(Model model) {
      Collection<InputDeclaration> inputs = new ArrayList<>();
      traverseModelRefinementHierarchy(model, m -> {
         if (m.getInput() != null) {
            inputs.addAll(m.getInput().getDeclarations());
         }
      });
      return inputs;
   }

   /**
    * Gets the input declaration from the given model with the given name.
    *
    * @param model     the model to search for the field
    * @param fieldName the name of the field
    * @return the input declaration or {@code null} if no declaration with the given name exists
    */
   public static InputDeclaration findInputDeclaration(Model model, String fieldName) {
      return getInputDeclarations(model).stream()
            .filter(p -> p.getName().equals(fieldName))
            .findFirst()
            .orElse(null);
   }

   /**
    * Gets all the output declarations declared in the given model and all refined models.
    *
    * @param model the model to get the outputs for
    * @return all the output declarations declared in the given model and all refined models
    */
   public static Collection<OutputDeclaration> getOutputDeclarations(Model model) {
      Collection<OutputDeclaration> outputs = new ArrayList<>();
      traverseModelRefinementHierarchy(model, m -> {
         if (m.getOutput() != null) {
            outputs.addAll(m.getOutput().getDeclarations());
         }
      });
      return outputs;
   }

   /**
    * Gets the output declaration from the given model with the given name.
    *
    * @param model     the model to search for the field
    * @param fieldName the name of the field
    * @return the output declaration or {@code null} if no declaration with the given name exists
    */
   public static OutputDeclaration findOutputDeclaration(Model model, String fieldName) {
      return getOutputDeclarations(model).stream()
            .filter(p -> p.getName().equals(fieldName))
            .findFirst()
            .orElse(null);
   }

   /**
    * Gets all the part declarations declared in the given model and all refined models.
    *
    * @param model the model to get the parts for
    * @return all the part declarations declared in the given model and all refined models
    */
   public static Collection<PartDeclaration> getAllPartDeclarations(Model model) {
      Collection<PartDeclaration> parts = new ArrayList<>();
      traverseModelRefinementHierarchy(model, m -> {
         if (m.getParts() != null) {
            parts.addAll(m.getParts().getDeclarations());
         }
      });
      return parts;
   }

   /**
    * Gets the part declaration from the given model with the given name.
    *
    * @param model     the model to search for the field
    * @param fieldName the name of the field
    * @return the part declaration or {@code null} if no declaration with the given name exists
    */
   public static PartDeclaration findPartDeclaration(Model model, String fieldName) {
      return getAllPartDeclarations(model).stream()
            .filter(p -> p.getName().equals(fieldName))
            .findFirst()
            .orElse(null);
   }

   /**
    * Gets all the requirement declarations declared in the given model and all refined models.
    *
    * @param model the model to get the parts for
    * @return all the requirement declarations declared in the given model and all refined models
    */
   public static Collection<RequireDeclaration> getAllRequireDeclarations(Model model) {
      Collection<RequireDeclaration> requires = new ArrayList<>();
      traverseModelRefinementHierarchy(model, m -> {
         if (m.getRequires() != null) {
            requires.addAll(m.getRequires().getDeclarations());
         }
      });
      return requires;
   }

   /**
    * Gets the requirement declaration from the given model with the given name.
    *
    * @param model     model the model to search for the field
    * @param fieldName the name of the field
    * @return the requirement declaration or {@code null} if no declaration with the given name exists
    */
   public static RequireDeclaration findRequireDeclaration(Model model, String fieldName) {
      return getAllRequireDeclarations(model).stream()
            .filter(p -> p.getName().equals(fieldName))
            .findFirst()
            .orElse(null);
   }

   /**
    * Gets the data field with the given name.
    *
    * @param data      the data type to get the field for
    * @param fieldName the name of the field
    * @return the field or {@code null} if the field could not be found
    */
   public static DataFieldDeclaration findDataFieldDeclaration(Data data, String fieldName) {
      return getAllDataFields(data).stream()
            .filter(f -> f.getName().equals(fieldName))
            .findFirst()
            .orElse(null);
   }

   /**
    * Gets all the data fields declared in the given data type and all extended data types.
    *
    * @param data the data type to get the fields for
    * @return all the data fields declared in the given data type and all extended data types
    */
   public static Collection<DataFieldDeclaration> getAllDataFields(Data data) {
      Collection<DataFieldDeclaration> fields = new ArrayList<>();
      traverseDataExtensionHierarchy(data, d -> fields.addAll(d.getFields()));
      return fields;
   }
   
   /**
    * Gets the type of the given part declaration regardless if the declaration is a
    * {@code BasePartDeclaration} or a {@code RefinedPartDeclaration}.
    * 
    * @param declaration the part declaration to get the type of
    * @return the type of the part declaration
    */
   public static Model getTypeOfPartDeclaration(PartDeclaration declaration) {
      switch (declaration.eClass().getClassifierID()) {
         case SystemDescriptorPackage.BASE_PART_DECLARATION:
            return ((BasePartDeclaration) declaration).getType();
         case SystemDescriptorPackage.REFINED_PART_DECLARATION:
            BasePartDeclaration[] holder = new BasePartDeclaration[1];
            Model containingModel = getContainingModel(declaration);
            traverseModelRefinementHierarchy(containingModel, m -> {
               if (m.getParts() != null) {
                  m.getParts()
                   .getDeclarations()
                   .stream()
                   .filter(p -> p.getName().equals(declaration.getName()))
                   .filter(p -> p instanceof BasePartDeclaration)
                   .map(p -> BasePartDeclaration.class.cast(p))
                   .findFirst()
                      .map(p -> holder[0] = p);
               }
            });
            return holder[0].getType();
         default:
            throw new IllegalStateException("unknown part declaration subclass "
               + declaration.getClass().getName());
      }
   }
   
   /**
    * Gets the type of the given require declaration regardless if the declaration is a
    * {@code BaseRequireDeclaration} or a {@code RefinedRequireDeclaration}.
    * 
    * @param declaration the part declaration to get the type of
    * @return the type of the part declaration
    */
   public static Model getTypeOfRequireDeclaration(RequireDeclaration declaration) {
      switch (declaration.eClass().getClassifierID()) {
         case SystemDescriptorPackage.BASE_REQUIRE_DECLARATION:
            return ((BaseRequireDeclaration) declaration).getType();
         case SystemDescriptorPackage.REFINED_REQUIRE_DECLARATION:
            BaseRequireDeclaration[] holder = new BaseRequireDeclaration[1];
            Model containingModel = getContainingModel(declaration);
            traverseModelRefinementHierarchy(containingModel, m -> {
               if (m.getRequires() != null) {
                  m.getRequires()
                   .getDeclarations()
                   .stream()
                   .filter(p -> p.getName().equals(declaration.getName()))
                   .filter(p -> p instanceof BaseRequireDeclaration)
                   .map(p -> BaseRequireDeclaration.class.cast(p))
                   .findFirst()
                      .map(p -> holder[0] = p);
               }
            });
            return holder[0].getType();
         default:
            throw new IllegalStateException("unknown require declaration subclass "
               + declaration.getClass().getName());
      }
   }
}
