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
package com.ngc.seaside.systemdescriptor.model.impl.xtext.data;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Data;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;

/**
 * Base class for types that adapts data field declarations to {@link IDataField}s.
 * This class is not threadsafe.
 *
 * @param <T> the type of XText field this class is wrapping
 */
public abstract class AbstractWrappedDataField<T extends DataFieldDeclaration> extends AbstractWrappedXtext<T>
      implements IDataField {

   protected IMetadata metadata;

   protected AbstractWrappedDataField(IWrapperResolver resolver, T wrapped) {
      super(resolver, wrapped);
      metadata = WrappedDeclarationDefinition.metadataFromXtext(wrapped.getDefinition());
   }

   @Override
   public FieldCardinality getCardinality() {
      return ConversionUtil.convertCardinalityFromXtext(wrapped.getCardinality());
   }

   @Override
   public IDataField setCardinality(FieldCardinality cardinality) {
      Preconditions.checkNotNull(cardinality, "cardinality may not be null!");
      wrapped.setCardinality(ConversionUtil.convertCardinalityToXtext(cardinality));
      return this;
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IDataField setMetadata(IMetadata metadata) {
      Preconditions.checkNotNull(metadata, "metadata may not be null!");
      this.metadata = metadata;
      wrapped.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, metadata, null));
      return this;
   }

   @Override
   public String getName() {
      return wrapped.getName();
   }

   @Override
   public IData getParent() {
      return resolver.getWrapperFor((Data) wrapped.eContainer());
   }
}
