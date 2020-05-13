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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

import java.util.Optional;

/**
 * Adapts an {@link RefinedRequireDeclaration} to an {@link IModelReferenceField}.
 * This class is not threadsafe.
 */
public class WrappedRefinedRequireModelReferenceField
      extends AbstractWrappedModelReferenceField<RefinedRequireDeclaration, WrappedRefinedRequireModelReferenceField> {

   public WrappedRefinedRequireModelReferenceField(IWrapperResolver resolver, RefinedRequireDeclaration wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public IModel getType() {
      return getRefinedField().get().getType();
   }

   @Override
   public IModelReferenceField setType(IModel type) {
      throw new UnsupportedOperationException("The type of refined field cannot be changed");
   }

   /**
    * Creates a new {@code RefinedRequireDeclaration} from the given field.
    */
   public static RefinedRequireDeclaration toXTextRequireDeclaration(IWrapperResolver resolver,
                                                                     IModelReferenceField field) {
      Preconditions.checkNotNull(resolver, "resolver may not be null!");
      Preconditions.checkNotNull(field, "field may not be null!");
      RefinedRequireDeclaration d = SystemDescriptorFactory.eINSTANCE.createRefinedRequireDeclaration();
      d.setName(field.getName());
      d.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, field.getMetadata(), null));
      return d;
   }

   @Override
   public Optional<IModelReferenceField> getRefinedField() {
      IModel refinedModel = getParent().getRefinedModel()
            .orElseThrow(() -> new IllegalStateException("Refined model missing for refined required field "
                                                               + getName()));
      IModelReferenceField field = refinedModel.getRequiredModels()
            .getByName(getName())
            .orElseThrow(() -> new IllegalStateException("Required " + getName() + " missing from refined model"));
      return Optional.of(field);
   }
}
