package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.PartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedPartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts an {@link PartDeclaration} to an {@link IModelReferenceField}.
 *
 * This class is not threadsafe.
 */
public class WrappedRefinedPartModelReferenceField extends AbstractWrappedModelReferenceField<RefinedPartDeclaration, WrappedRefinedPartModelReferenceField> {

   public WrappedRefinedPartModelReferenceField(IWrapperResolver resolver, RefinedPartDeclaration wrapped) {
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
    * Creates a new {@code BasePartDeclaration} from the given field.
    */
   public static RefinedPartDeclaration toXTextPartDeclaration(IWrapperResolver resolver, IModelReferenceField field) {
      Preconditions.checkNotNull(resolver, "resolver may not be null!");
      Preconditions.checkNotNull(field, "field may not be null!");
      RefinedPartDeclaration d = SystemDescriptorFactory.eINSTANCE.createRefinedPartDeclaration();
      d.setName(field.getName());
      d.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, field.getMetadata(), null));
      return d;
   }

   @Override
   public Optional<IModelReferenceField> getRefinedField() {
      IModel model = Preconditions.checkNotNull(getParent());
      IModel refinedModel = model.getRefinedModel().orElseThrow(() -> new IllegalStateException("Refined model missing for refined part field" + getName()));
      IModelReferenceField field = refinedModel.getParts().getByName(getName()).orElseThrow(() -> new IllegalStateException("Part " + getName() + " missing from refined model"));
      return Optional.of(field);
   }
}
