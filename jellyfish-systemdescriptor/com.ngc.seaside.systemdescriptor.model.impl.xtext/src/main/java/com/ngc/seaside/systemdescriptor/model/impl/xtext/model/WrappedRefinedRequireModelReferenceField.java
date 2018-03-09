package com.ngc.seaside.systemdescriptor.model.impl.xtext.model;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Adapts an {@link RefinedRequireDeclaration} to an {@link IModelReferenceField}.
 *
 * This class is not threadsafe.
 */
public class WrappedRefinedRequireModelReferenceField extends AbstractWrappedModelReferenceField<RefinedRequireDeclaration, WrappedRefinedRequireModelReferenceField> {

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
   public static RefinedRequireDeclaration toXTextRequireDeclaration(IWrapperResolver resolver, IModelReferenceField field) {
      Preconditions.checkNotNull(resolver, "resolver may not be null!");
      Preconditions.checkNotNull(field, "field may not be null!");
      RefinedRequireDeclaration d = SystemDescriptorFactory.eINSTANCE.createRefinedRequireDeclaration();
      d.setName(field.getName());
      d.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, field.getMetadata(), null));
      return d;
   }

   @Override
   public Optional<IModelReferenceField> getRefinedField() {
      IModel refinedModel = getParent().getRefinedModel().orElseThrow(() -> new IllegalStateException("Refined model missing for refined required field " + getName()));
      IModelReferenceField field = refinedModel.getRequiredModels().getByName(getName()).orElseThrow(() -> new IllegalStateException("Required " + getName() + " missing from refined model"));
      return Optional.of(field);
   }
}
