package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.XtextObjectNotFoundException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkNameDeclaration;

import org.eclipse.emf.ecore.util.EcoreUtil;

import java.util.Optional;

public abstract class WrappedReferenceLink<T extends IReferenceField> extends AbstractWrappedXtext<LinkDeclaration>
      implements IModelLink<T> {

   private IMetadata metadata;
   private IProperties properties;
   protected IModelLink<T> refinedLink;

   protected WrappedReferenceLink(IWrapperResolver resolver, LinkDeclaration wrapped) {
      super(resolver, wrapped);
      this.metadata = WrappedMetadata.fromXtext(wrapped.getDefinition());
      this.properties = WrappedDeclarationDefinition.propertiesFromXtext(resolver, wrapped.getDefinition());
   }

   @Override
   public IMetadata getMetadata() {
      return metadata;
   }

   @Override
   public IModelLink<T> setMetadata(IMetadata metadata) {
      this.metadata = metadata;
      return this;
   }

   @Override
   public IProperties getProperties() {
      return properties;
   }

   @Override
   public IModelLink<T> setProperties(IProperties properties) {
      wrapped.setDefinition(WrappedDeclarationDefinition.toXtext(resolver, null, properties));
      this.properties = properties;
      return this;
   }

   @Override
   public Optional<IModelLink<T>> getRefinedLink() {
      return Optional.ofNullable(refinedLink);
   }

   @Override
   public IModelLink<T> setRefinedLink(IModelLink<T> refinedLink) {
      throw new UnsupportedOperationException("refined link cannot be changed!");
   }

   @SuppressWarnings({"unchecked"})
   protected IModelLink<T> doGetRefinedLink(RefinedLinkDeclaration link) {
      IModelLink<T> refinedLink = null;

      IModel model = getParent().getRefinedModel().orElse(null);
      while (model != null && refinedLink == null) {
         // Get the refined model.
         Model xtext = resolver.findXTextModel(model.getName(), model.getParent().getName())
               .orElse(null);
         if (xtext == null) {
            throw XtextObjectNotFoundException.forModel(model);
         }

         // Does the refined model contain the link?
         LinkDeclaration xtextLink = xtext.getLinks().getDeclarations()
               .stream()
               .filter(l -> l instanceof BaseLinkDeclaration)
               .map(l -> (BaseLinkDeclaration) l)
               .filter(l -> EcoreUtil.equals(l.getSource(), link.getSource()))
               .filter(l -> EcoreUtil.equals(l.getTarget(), link.getTarget()))
               .findFirst()
               .orElse(null);
         if (xtextLink != null) {
            // If so, find the wrapped link in the wrapped model.
            for (IModelLink<?> wrappedLink : resolver.getWrapperFor(xtext).getLinks()) {
               // Note both WrappedDataReferenceLink and WrappedModelReferenceLink extend
               // AbstractWrappedXtext<LinkDeclaration> so this cast is safe.
               AbstractWrappedXtext<LinkDeclaration> casted = (AbstractWrappedXtext<LinkDeclaration>) wrappedLink;
               if (casted.unwrap().equals(xtextLink)) {
                  refinedLink = (IModelLink<T>) wrappedLink;
               }
            }
         }

         model = model.getRefinedModel().orElse(null);
      }

      Preconditions.checkState(refinedLink != null,
                               "unable to find refined link in refine hierarchy of %s!",
                               getParent().getFullyQualifiedName());
      return refinedLink;
   }

   @SuppressWarnings({"unchecked"})
   protected IModelLink<T> doGetRefinedLink(RefinedLinkNameDeclaration link) {
      IModelLink<T> refinedLink = null;

      IModel model = getParent().getRefinedModel().orElse(null);
      while (model != null && refinedLink == null) {
         refinedLink = (IModelLink<T>) model.getLinkByName(link.getName()).orElse(null);
         model = model.getRefinedModel().orElse(null);
      }

      Preconditions.checkState(refinedLink != null,
                               "unable to find refined link with name '%s' in refine hierarchy of %s!",
                               link.getName(),
                               getParent().getFullyQualifiedName());
      return refinedLink;
   }
}
