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

public class WrappedDataReferenceLink extends AbstractWrappedXtext<LinkDeclaration>
    implements IModelLink<IDataReferenceField> {

  private IDataReferenceField source;
  private IDataReferenceField target;

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
    Optional<IDataReferenceField> field = parent.getInputs().getByName(declaration.getName());
    if (!field.isPresent()) {
      field = parent.getOutputs().getByName(declaration.getName());
    }
    return field.orElseThrow(() -> new IllegalStateException(String.format(
        "could not find input or output field named %s in model %s!",
        declaration.getName(),
        parent)));
  }
}
