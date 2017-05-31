package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.ModelFieldCardinality;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnconvertableTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Cardinality;
import com.ngc.seaside.systemdescriptor.systemDescriptor.OutputDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts an {@link OutputDeclaration} to an {@link IDataReferenceField}.
 *
 * This class is not threadsafe.
 */
public class WrappedOutputDataReferenceField extends AbstractWrappedDataReferenceField<OutputDeclaration> {

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
   public ModelFieldCardinality getCardinality() {
      switch (wrapped.getCardinality()) {
         case DEFAULT:
            return ModelFieldCardinality.SINGLE;
         case MANY:
            return ModelFieldCardinality.MANY;
         default:
            throw new UnrecognizedXtextTypeException(wrapped.getCardinality());
      }
   }

   @Override
   public IDataReferenceField setCardinality(ModelFieldCardinality cardinality) {
      Preconditions.checkNotNull(cardinality, "cardinality may not be null!");
      wrapped.setCardinality(convertCardinality(cardinality));
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
      d.setCardinality(convertCardinality(field.getCardinality()));
      d.setType(doFindXtextData(resolver, field.getType().getName(), field.getType().getParent().getName()));
      return d;
   }

   private static Cardinality convertCardinality(ModelFieldCardinality cardinality) {
      switch (cardinality) {
         case SINGLE:
            return Cardinality.DEFAULT;
         case MANY:
            return Cardinality.MANY;
         default:
            throw new UnconvertableTypeException(cardinality);
      }
   }
}
