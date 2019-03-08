/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.systemdescriptor.model.impl.xtext.model.link;

import com.google.common.base.Preconditions;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.AbstractWrappedXtext;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration.WrappedDeclarationDefinition;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.UnrecognizedXtextTypeException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.exception.XtextObjectNotFoundException;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.FieldReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableExpression;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkableReference;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkNameDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.utils.SdUtils;

import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

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

   @Override
   public void traverseLinkSourceExpression(Consumer<IModelReferenceField> linkVisitor) {
      Preconditions.checkNotNull(linkVisitor, "linkVisitor may not be null!");
      Preconditions.checkArgument(!getRefinedLink().isPresent(), "refined links do not have expressions!");
      // Safe because of the getRefinedLinkedCheck above.
      doLinkTraverse(((BaseLinkDeclaration) wrapped).getSource(), linkVisitor);
   }

   @Override
   public void traverseLinkTargetExpression(Consumer<IModelReferenceField> linkVisitor) {
      Preconditions.checkNotNull(linkVisitor, "linkVisitor may not be null!");
      Preconditions.checkArgument(!getRefinedLink().isPresent(), "refined links do not have expressions!");
      // Safe because of the getRefinedLinkedCheck above.
      doLinkTraverse(((BaseLinkDeclaration) wrapped).getTarget(), linkVisitor);
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
         LinkDeclaration xtextLink = findXtextBaseLinkDeclarationForRefinedLink(xtext, link);

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

      if (refinedLink == null) {
         String sourcePath = getPathOfSource(link);
         String targetPath = getPathOfTarget(link);
         String msg = String.format("unable to find the base link for %s -> %s in refinement hierarchy of %s!",
                                    sourcePath,
                                    targetPath,
                                    getParent().getFullyQualifiedName());
         throw new IllegalStateException(msg);
      }
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

   private void doLinkTraverse(LinkableReference start, Consumer<IModelReferenceField> linkVisitor) {
      LinkableReference ref = start;

      // Ignore sources that are not expressions.
      if (ref.eClass().getClassifierID() == SystemDescriptorPackage.LINKABLE_EXPRESSION) {
         while (ref != null) {
            switch (ref.eClass().getClassifierID()) {
               case SystemDescriptorPackage.FIELD_REFERENCE:
                  // If this is a field reference, there are no more expressions and we are done.
                  IModelReferenceField field = getFieldOf(((FieldReference) ref).getFieldDeclaration());
                  linkVisitor.accept(field);
                  ref = null;
                  break;
               case SystemDescriptorPackage.LINKABLE_EXPRESSION:
                  // Otherwise, this is an expression that may be pointing to another model.
                  // We don't care about the last tail, which is the tail of the first reference.
                  if (ref != start) {
                     field = getFieldOf(((FieldReference) ref).getFieldDeclaration());
                     linkVisitor.accept(field);
                  }
                  ref = ((LinkableExpression) ref).getRef();
                  break;
               default:
                  throw new UnrecognizedXtextTypeException(ref);
            }
         }
      }
   }

   private IModelReferenceField getFieldOf(FieldDeclaration declaration) {
      // Only models can have field declarations.
      IModel parent = resolver.getWrapperFor(SdUtils.getContainingModel(declaration));
      // Get the wrapper for the field.  Note that a model may not have duplicate field names.  Therefore, the
      // declaration is either for a part or requirement.
      Optional<IModelReferenceField> field = parent.getParts() == null
                                             ? Optional.empty()
                                             : parent.getParts().getByName(declaration.getName());
      if (!field.isPresent()) {
         field = parent.getOutputs() == null ? Optional.empty()
                                             : parent.getRequiredModels().getByName(declaration.getName());
      }
      return field.orElseThrow(() -> new IllegalArgumentException(String.format(
            "could not find part or requirement field named %s in model %s!",
            declaration.getName(),
            parent)));
   }

   private static LinkDeclaration findXtextBaseLinkDeclarationForRefinedLink(Model xtext,
                                                                             RefinedLinkDeclaration refinedLink) {
      String sourcePath = getPathOfSource(refinedLink);
      String targetPath = getPathOfTarget(refinedLink);

      if (xtext.getLinks() != null) {
         for (LinkDeclaration link : xtext.getLinks().getDeclarations()) {
            if (Objects.equals(sourcePath, getPathOfSource(link))
                   && Objects.equals(targetPath, getPathOfTarget(link))) {
               return link;
            }
         }
      }

      return null;
   }

   /**
    * Gets the text value of the source of a link.
    */
   private static String getPathOfSource(LinkDeclaration link) {
      List<INode> nodes;
      if (link instanceof BaseLinkDeclaration) {
         nodes = NodeModelUtils.findNodesForFeature(link,
                                                    SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__SOURCE);
      } else {
         nodes = NodeModelUtils.findNodesForFeature(link,
                                                    SystemDescriptorPackage.Literals.REFINED_LINK_DECLARATION__SOURCE);
      }
      return nodes.isEmpty() ? null : NodeModelUtils.getTokenText(nodes.get(0));
   }

   /**
    * Gets the text value of the target of a link.
    */
   private static String getPathOfTarget(LinkDeclaration link) {
      List<INode> nodes;
      if (link instanceof BaseLinkDeclaration) {
         nodes = NodeModelUtils.findNodesForFeature(link,
                                                    SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);
      } else {
         nodes = NodeModelUtils.findNodesForFeature(link,
                                                    SystemDescriptorPackage.Literals.REFINED_LINK_DECLARATION__TARGET);
      }
      return nodes.isEmpty() ? null : NodeModelUtils.getTokenText(nodes.get(0));
   }
}
