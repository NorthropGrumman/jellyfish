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
package com.ngc.seaside.systemdescriptor.model.impl.basic.model.link;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.impl.basic.model.properties.Properties;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Implements the IModelLink interface.  Maintains the source of the link and the target of the link.
 *
 * @param <T> IReferenceField type
 */
public class ModelLink<T extends IReferenceField> implements IModelLink<T> {

   private T source;
   private T target;
   private final IModel parent;
   private String name;
   private IProperties properties;
   private IModelLink<T> refinedLink;
   private IMetadata metadata;

   /**
    * Creates a new link.
    */
   public ModelLink(IModel p) {
      parent = p;
      this.properties = new Properties();
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IModelLink<T> setMetadata(IMetadata metadata) {
      this.metadata = metadata;
      return this;
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
   public void traverseLinkSourceExpression(Consumer<IModelReferenceField> linkVisitor) {
      // These methods aren't supported for the basic impl.
      throw new UnsupportedOperationException("not implemented");
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
   public void traverseLinkTargetExpression(Consumer<IModelReferenceField> linkVisitor) {
      // These methods aren't supported for the basic impl.
      throw new UnsupportedOperationException("not implemented");
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
   public Optional<IModelLink<T>> getRefinedLink() {
      return Optional.ofNullable(refinedLink);
   }

   @Override
   public IModelLink<T> setRefinedLink(IModelLink<T> refinedLink) {
      this.refinedLink = refinedLink;
      return this;
   }

   @Override
   public IModel getParent() {
      return parent;
   }

   @Override
   public IProperties getProperties() {
      return properties;
   }

   @Override
   public IModelLink<T> setProperties(IProperties properties) {
      this.properties = properties;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof ModelLink)) {
         return false;
      }
      ModelLink<?> modelLink = (ModelLink<?>) o;
      return source == modelLink.source
             && target == modelLink.target
             && parent == modelLink.parent
             && Objects.equals(name, modelLink.name)
             && Objects.equals(properties, modelLink.properties)
             && refinedLink == modelLink.refinedLink
             && Objects.equals(metadata, modelLink.metadata);
   }

   @Override
   public int hashCode() {
      return Objects.hash(System.identityHashCode(source),
                          System.identityHashCode(target),
                          System.identityHashCode(parent),
                          name,
                          properties,
                          System.identityHashCode(refinedLink),
                          metadata);
   }

   @Override
   public String toString() {
      return "ModelLink{"
             + "source=" + source
             + ", target=" + target
             + ", parent=" + parent
             + ", name='" + name + '\''
             + ", properties=" + properties
             + ", refinedLink=" + refinedLink
             + '}';
   }
}
