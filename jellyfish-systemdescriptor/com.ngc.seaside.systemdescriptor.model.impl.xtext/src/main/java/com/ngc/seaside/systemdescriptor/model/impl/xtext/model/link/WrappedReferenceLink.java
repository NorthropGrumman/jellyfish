package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;

public abstract class WrappedReferenceLink<T extends IReferenceField> extends AbstractWrappedXtext<LinkDeclaration>
         implements IModelLink<T> {

   protected WrappedReferenceLink(IWrapperResolver resolver, LinkDeclaration wrapped) {
      super(resolver, wrapped);
   }

   @Override
   public IProperties getProperties() {
      return WrappedDeclarationDefinition.propertiesFromXtext(resolver, wrapped.getDefinition());
   }

   @Override
   public IModelLink<T> setProperties(IProperties properties) {
      wrapped.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, null, properties));
      return this;
   }

}
