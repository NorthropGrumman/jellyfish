package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
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

public class WrappedModelReferenceLink extends AbstractWrappedXtext<LinkDeclaration>
    implements IModelLink<IModelReferenceField> {

  private IModelReferenceField source;
  private IModelReferenceField target;

  public WrappedModelReferenceLink(IWrapperResolver resolver, LinkDeclaration wrapped) {
    super(resolver, wrapped);
    source = getReferenceTo(wrapped.getSource());
    target = getReferenceTo(wrapped.getTarget());
  }

  @Override
  public IModelReferenceField getSource() {
    return source;
  }

  @Override
  public IModelLink<IModelReferenceField> setSource(IModelReferenceField source) {
    // TODO TH: figure out how to implement this.
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IModelReferenceField getTarget() {
    return target;
  }

  @Override
  public IModelLink<IModelReferenceField> setTarget(IModelReferenceField target) {
    // TODO TH: figure out how to implement this.
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IModel getParent() {
    return resolver.getWrapperFor((Model) wrapped.eContainer().eContainer());
  }

  public static Optional<IModelLink<IModelReferenceField>> tryToWrap(IWrapperResolver resolver,
                                                                     LinkDeclaration wrapper) {
    Optional<IModelLink<IModelReferenceField>> result = Optional.empty();
    try {
      result = Optional.of(new WrappedModelReferenceLink(resolver, wrapper));
    } catch (IllegalArgumentException e) {
      // Do nothing, this means the link is not linking models.
    }
    return result;
  }

  private IModelReferenceField getReferenceTo(LinkableReference ref) {
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

  private IModelReferenceField getFieldOf(FieldDeclaration declaration) {
    // Only models can have field declarations.
    IModel parent = resolver.getWrapperFor((Model) declaration.eContainer().eContainer());
    // Get the wrapper for the field.  Note that a model may not have duplicate field names.  Therefore, the declaration
    // is either for a part or requirement.
    Optional<IModelReferenceField> field = parent.getParts() == null
                                           ? Optional.empty()
                                           : parent.getParts().getByName(declaration.getName());
    if (!field.isPresent()) {
      field = parent.getRequiredModels() == null ? Optional.empty()
                                                 : parent.getRequiredModels().getByName(declaration.getName());
    }
    return field.orElseThrow(() -> new IllegalArgumentException(String.format(
        "could not find part or requirement field named %s in model %s!",
        declaration.getName(),
        parent)));
  }
}
