package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;

public class WrappedLink<T  extends IReferenceField> extends AbstractWrappedXtext<LinkDeclaration> implements IModelLink<T>{

  public WrappedLink(IWrapperResolver resolver, LinkDeclaration wrapped) {
    super(resolver, wrapped);
  }

  @Override
  public T getSource() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IModelLink<T> setSource(T source) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public T getTarget() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IModelLink<T> setTarget(T target) {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IModel getParent() {
    throw new UnsupportedOperationException("not implemented");
  }
}
