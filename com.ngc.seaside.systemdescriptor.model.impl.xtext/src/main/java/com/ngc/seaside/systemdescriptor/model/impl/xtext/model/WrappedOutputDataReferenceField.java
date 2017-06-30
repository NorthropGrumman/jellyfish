package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.FieldCardinality;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.util.ConversionUtil;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts an {@link OutputDeclaration} to an {@link IDataReferenceField}.
 *
 * This class is not threadsafe.
 */
public class WrappedOutputDataReferenceField
      extends AbstractWrappedDataReferenceField<OutputDeclaration, WrappedOutputDataReferenceField> {

   public WrappedOutputDataReferenceField(IWrapperResolver resolver, OutputDeclaration wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public IData getType() {
      return resolver.getWrapperFor(wrapped.getType());
   }

   @Override
   public IDataReferenceField setType(IData type) {
      Preconditions.checkNotNull(type, "type may not be null!");
      Preconditions.checkArgument(type.getParent() != null, "data must be contained within a package");
      wrapped.setType(findXtextData(type.getName(), type.getParent().getName()));
      return this;
   }

   @Override
   public FieldCardinality getCardinality() {
      return ConversionUtil.convertCardinalityFromXtext(wrapped.getCardinality());
   }

   @Override
   public IDataReferenceField setCardinality(FieldCardinality cardinality) {
      Preconditions.checkNotNull(cardinality, "cardinality may not be null!");
      wrapped.setCardinality(ConversionUtil.convertCardinalityToXtext(cardinality));
      return this;
   }

   /**
    * Creates a new {@code OutputDeclaration} from the given field.
    */
   public static OutputDeclaration toXTextOutputDeclaration(IWrapperResolver resolver, IDataReferenceField field) {
      Preconditions.checkNotNull(resolver, "resolver may not be null!");
      Preconditions.checkNotNull(field, "field may not be null!");
      OutputDeclaration d = SystemDescriptorFactory.eINSTANCE.createOutputDeclaration();
      d.setName(field.getName());
      d.setCardinality(ConversionUtil.convertCardinalityToXtext(field.getCardinality()));
      d.setType(doFindXtextData(resolver, field.getType().getName(), field.getType().getParent().getName()));
      return d;
   }
}
