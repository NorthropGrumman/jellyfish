package com.ngc.seaside.systemdescriptor.model.impl.basic.model.link;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.impl.basic.metadata.Metadata;

import java.util.Objects;
import java.util.Optional;

/**
 * Implements the IModelLink interface.  Maintains the source of the link and the target of the link.
 *
 * @param <T> IReferenceField type
 * @author psnell
 */
public class ModelLink<T extends IReferenceField> implements IModelLink<T> {

   private T source;
   private T target;
   private final IModel parent;
   private String name;

   public ModelLink(IModel p) {
      parent = p;
   }

   @Override
   public T getSource() {
      return source;
   }

   @Override
   public IModelLink<T> setSource(T source) {
      this.source = source;
      return this;
   }

   @Override
   public T getTarget() {
      return target;
   }

   @Override
   public IModelLink<T> setTarget(T target) {
      this.target = target;
      return this;
   }

   @Override
   public Optional<String> getName() {
	   return Optional.ofNullable(name);
   }

   @Override
   public IModelLink<T> setName(String name) {
	   this.name = name;
	   return this;
   }

   @Override
   public IModel getParent() {
      return parent;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof ModelLink)) {
         return false;
      }

      ModelLink<?> link = (ModelLink<?>) o;
      return Objects.equals(name, link.name) &&
             source == link.source &&
             target == link.target &&
             parent == link.parent;
   }

   @Override
   public int hashCode() {
      return Objects.hash(System.identityHashCode(source),
                          System.identityHashCode(target),
                          System.identityHashCode(parent));
   }

   @Override
   public String toString() {
      return "ModelLink[" +
             "name='" + name +
             "source='" + (source == null ? "null" : source) +
             "target='" + (target == null ? "null" : target) +
             "parent='" + (parent == null ? "null" : parent) +
             ']';
   }
}
