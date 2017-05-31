package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import java.util.Optional;

/**
 * Adapts a {@link LinkDeclaration} to an {@link IModelLink} that links together data elements.
 *
 * This class is not threadsafe.
 */
public class WrappedDataReferenceLink extends AbstractWrappedXtext<LinkDeclaration>
      implements IModelLink<IDataReferenceField> {

   private IDataReferenceField source;
   private IDataReferenceField target;

   /**
    * Creates a new wrapper for the given link declaration.  If the link declaration does not wrap data an {@code
    * IllegalArgumentException} is thrown.  Use {@link #tryToWrap(IWrapperResolver, LinkDeclaration)} as an alternative
    * to avoid the exception and determine if the declaration really does link data.
    *
    * @see #tryToWrap(IWrapperResolver, LinkDeclaration)
    */
   public WrappedDataReferenceLink(IWrapperResolver resolver, LinkDeclaration wrapped) {
      super(resolver, wrapped);
      source = getReferenceTo(wrapped.getSource());
      target = getReferenceTo(wrapped.getTarget());
   }

   @Override
   public IDataReferenceField getSource() {
      return source;
   }

   @Override
   public IModelLink<IDataReferenceField> setSource(IDataReferenceField source) {
      // TODO TH: figure out how to implement this.
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IDataReferenceField getTarget() {
      return target;
   }

   @Override
   public IModelLink<IDataReferenceField> setTarget(IDataReferenceField target) {
      // TODO TH: figure out how to implement this.
      throw new UnsupportedOperationException("not implemented");
   }

   @Override
   public IModel getParent() {
      return resolver.getWrapperFor((Model) wrapped.eContainer().eContainer());
   }

   /**
    * Tries to create an {@code IModelLink} that links data.  If the given link declaration does not link data the
    * returned {@code Optional} is empty.
    */
   public static Optional<IModelLink<IDataReferenceField>> tryToWrap(IWrapperResolver resolver,
                                                                     LinkDeclaration wrapper) {
      Optional<IModelLink<IDataReferenceField>> result = Optional.empty();
      try {
         result = Optional.of(new WrappedDataReferenceLink(resolver, wrapper));
      } catch (IllegalArgumentException e) {
         // Do nothing, this means the link is not linking data.
      }
      return result;
   }

   private IDataReferenceField getReferenceTo(LinkableReference ref) {
      // What kind of a link is this?
      switch (ref.eClass().getClassifierID()) {
         case SystemDescriptorPackage.FIELD_REFERENCE:
            // If this is a field reference, it must be referencing a field of the parent model.
            return getFieldOf(((FieldReference) ref).getFieldDeclaration());
         case SystemDescriptorPackage.LINKABLE_EXPRESSION:
            // Otherwise, this is an expression that may be pointing to another model.
            return getFieldOf(((LinkableExpression) ref).getTail());
         default:
            throw new UnrecognizedXtextTypeException(ref);
      }
   }

   private IDataReferenceField getFieldOf(FieldDeclaration declaration) {
      // Only models can have field declarations.
      IModel parent = resolver.getWrapperFor((Model) declaration.eContainer().eContainer());
      // Get the wrapper for the field.  Note that a model may not have duplicate field names.  Therefore, the declaration
      // is either for input or output.
      Optional<IDataReferenceField> field = parent.getInputs() == null
                                            ? Optional.empty()
                                            : parent.getInputs().getByName(declaration.getName());
      if (!field.isPresent()) {
         field = parent.getOutputs() == null ? Optional.empty()
                                             : parent.getOutputs().getByName(declaration.getName());
      }
      return field.orElseThrow(() -> new IllegalArgumentException(String.format(
            "could not find input or output field named %s in model %s!",
            declaration.getName(),
            parent)));
   }
}
