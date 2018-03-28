package com.ngc.seaside.systemdescriptor.utils;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RequireDeclaration;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import java.util.List;
import java.util.function.Consumer;

public class SdUtils {

   private SdUtils() {}

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
    * @param obj the object that is contained an instance of {@code clazz}
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
    * 
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
    * @param model the model whose hierarchy is being traversed
    * @param consumer the consumer to invoke for each model
    */
   public static void traverseModelRefinementHierarchy(Model model, Consumer<Model> consumer) {
      do {
         consumer.accept(model);
         model = model.getRefinedModel();
      } while (model != null);
   }

   /**
    * 
    * Looks for the part declaration thats being refined in the Model hierarchy
    * 
    * @param model used as a starting point for the Model hierarchy
    * @param fieldName the name of the field
    * @return the part declaration or {@code null} if no declaration with the given name exists
    */
   public static PartDeclaration findPartDeclarationName(Model model, String fieldName) {
      PartDeclaration found = null;
      Model parentModel = model.getRefinedModel();

      while (parentModel != null) {
         // Part Declaration
         if (parentModel.getParts() != null &&
            parentModel.getParts().getDeclarations() != null) {
            for (PartDeclaration fieldDec : parentModel.getParts().getDeclarations()) {
               if (fieldDec.getName().equals(fieldName)) {
                  found = fieldDec;
                  break;
               }
            }
         }
         parentModel = parentModel.getRefinedModel();
      }

      return found;
   }

   /**
    * 
    * Looks for the require declaration thats being refined in the Model hierarchy
    * 
    * @param model used as a starting point for the Model hierarchy
    * @param fieldName the name of the field
    * @return the requirement declaration or {@code null} if no declaration with the given name exists
    */
   public static RequireDeclaration findRequireDeclarationName(Model model, String fieldName) {
      RequireDeclaration found = null;
      Model parentModel = model.getRefinedModel();

      while (parentModel != null) {
         if (parentModel.getRequires() != null &&
            parentModel.getRequires().getDeclarations() != null) {
            for (RequireDeclaration fieldDec : parentModel.getRequires().getDeclarations()) {
               if (fieldDec.getName().equals(fieldName)) {
                  found = fieldDec;
                  break;
               }
            }
         }
         parentModel = parentModel.getRefinedModel();
      }

      return found;
   }
}
