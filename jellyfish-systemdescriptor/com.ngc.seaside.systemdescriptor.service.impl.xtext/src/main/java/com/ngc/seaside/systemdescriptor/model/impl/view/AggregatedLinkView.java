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
package com.ngc.seaside.systemdescriptor.model.impl.view;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;

import java.util.Optional;
import java.util.function.Consumer;

public class AggregatedLinkView<T extends IReferenceField> implements IModelLink<T> {

   private final IModelLink<T> wrapped;
   private String name;
   private T source;
   private T target;
   private IMetadata aggregatedMetadata;
   private IProperties aggregatedProperties;

   /**
    * Creates a new aggregated view that wraps the given link.
    */
   public AggregatedLinkView(IModelLink<T> wrapped) {
      this.wrapped = wrapped;
      this.aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      this.aggregatedProperties = AggregatedPropertiesView.getAggregatedProperties(wrapped);
      this.name = doGetName();
      this.source = doGetSource();
      this.target = doGetTarget();
   }

   @Override
   public IMetadata getMetadata() {
      return aggregatedMetadata;
   }

   @Override
   public IModelLink<T> setMetadata(IMetadata metadata) {
      wrapped.setMetadata(metadata);
      aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      return this;
   }

   @Override
   public IProperties getProperties() {
      return aggregatedProperties;
   }

   @Override
   public IModelLink<T> setProperties(IProperties properties) {
      wrapped.setProperties(properties);
      aggregatedMetadata = AggregatedMetadataView.getAggregatedMetadata(wrapped);
      return this;
   }

   @Override
   public T getSource() {
      return source;
   }

   @Override
   public IModelLink<T> setSource(T source) {
      wrapped.setSource(source);
      this.source = doGetSource();
      return this;
   }

   @Override
   public void traverseLinkSourceExpression(Consumer<IModelReferenceField> linkVisitor) {
      wrapped.traverseLinkSourceExpression(linkVisitor);
   }

   @Override
   public T getTarget() {
      return target;
   }

   @Override
   public IModelLink<T> setTarget(T target) {
      wrapped.setTarget(target);
      this.target = doGetTarget();
      return this;
   }

   @Override
   public void traverseLinkTargetExpression(Consumer<IModelReferenceField> linkVisitor) {
      wrapped.traverseLinkTargetExpression(linkVisitor);
   }

   @Override
   public Optional<String> getName() {
      return Optional.ofNullable(name);
   }

   @Override
   public IModelLink<T> setName(String name) {
      wrapped.setName(name);
      this.name = doGetName();
      return this;
   }

   @Override
   public Optional<IModelLink<T>> getRefinedLink() {
      return wrapped.getRefinedLink();
   }

   @Override
   public IModelLink<T> setRefinedLink(IModelLink<T> refinedLink) {
      wrapped.setRefinedLink(refinedLink);
      this.name = doGetName();
      this.source = doGetSource();
      this.target = doGetTarget();
      return this;
   }

   @Override
   public IModel getParent() {
      return wrapped.getParent();
   }

   private String doGetName() {
      String name = wrapped.getName().orElse(null);
      if (wrapped.getRefinedLink().isPresent()) {
         IModelLink<T> link = wrapped.getRefinedLink().orElse(null);
         while (link != null && name == null) {
            name = link.getName().orElse(null);
            link = link.getRefinedLink().orElse(null);
         }
      } else {
         name = wrapped.getName().orElse(null);
      }
      return name;
   }

   private T doGetSource() {
      T source = null;
      if (wrapped.getRefinedLink().isPresent()) {
         IModelLink<T> link = wrapped.getRefinedLink().orElse(null);
         while (link != null && source == null) {
            source = link.getSource();
            link = link.getRefinedLink().orElse(null);
         }
      } else {
         source = wrapped.getSource();
      }
      return source;
   }

   private T doGetTarget() {
      T target = null;
      if (wrapped.getRefinedLink().isPresent()) {
         IModelLink<T> link = wrapped.getRefinedLink().orElse(null);
         while (link != null && target == null) {
            target = link.getTarget();
            link = link.getRefinedLink().orElse(null);
         }
      } else {
         target = wrapped.getTarget();
      }
      return target;
   }
}
