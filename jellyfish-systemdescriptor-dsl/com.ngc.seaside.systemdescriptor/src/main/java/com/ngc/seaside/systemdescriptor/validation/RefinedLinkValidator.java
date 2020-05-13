/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.systemdescriptor.validation;

import com.google.common.base.Objects;
import com.google.inject.Inject;

import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.LinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Links;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.RefinedLinkNameDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.utils.SdUtils;

import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.validation.Check;

import java.util.HashSet;
import java.util.Set;

public class RefinedLinkValidator extends AbstractUnregisteredSystemDescriptorValidator {

   @Inject
   private IQualifiedNameProvider nameProvider;

   /**
    * Validates the refined link actually refines a link.
    */
   @Check
   public void checkRefinedLinkedDeclaration(RefinedLinkDeclaration link) {
      Model model = (Model) link
            .eContainer() // Links
            .eContainer(); // Model

      if (checkModelRefinesAnotherModel(link, model)) {
         // Only do these checks if a model is being refined.
         checkLinkDoesNotDeclareANewName(link, model);
         checkLinkRefinesABaseLink(link, model);
      }
   }

   /**
    * Validates the refined link actually refines a link.
    */
   @Check
   public void checkRefinedLinkedDeclaration(RefinedLinkNameDeclaration link) {
      Model model = (Model) link
            .eContainer() // Links
            .eContainer(); // Model
      if (checkModelRefinesAnotherModel(link, model)) {
         // Only do these checks if a model is being refined.
         checkLinkIsDeclaredInRefinedModel(link, model);
      }
   }

   private boolean checkModelRefinesAnotherModel(LinkDeclaration link, Model model) {
      boolean refinesModel = model.getRefinedModel() != null;

      if (!refinesModel) {
         Links links = (Links) link.eContainer();
         String msg = String.format(
               "Cannot refine a link because the model '%s' does not refine another model.",
               nameProvider.getFullyQualifiedName(model));
         int index = links.getDeclarations().indexOf(link);
         error(msg, links, SystemDescriptorPackage.Literals.LINKS__DECLARATIONS, index);
      }

      return refinesModel;
   }

   private void checkLinkIsDeclaredInRefinedModel(RefinedLinkNameDeclaration link, Model model) {
      Set<String> linkNames = new HashSet<>();

      Model refinedModel = model.getRefinedModel();
      while (refinedModel != null) {
         if (refinedModel.getLinks() != null) {
            refinedModel.getLinks().getDeclarations()
                  .stream()
                  .filter(l -> l.getName() != null)
                  .forEach(l -> linkNames.add(l.getName()));
         }

         refinedModel = refinedModel.getRefinedModel();
      }

      if (!linkNames.contains(link.getName())) {
         String msg = String.format(
               "No link named '%s' declared in the refinement hierarchy of '%s'.",
               link.getName(),
               nameProvider.getFullyQualifiedName(model));
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }
   }

   private void checkLinkDoesNotDeclareANewName(RefinedLinkDeclaration link, Model model) {
      BaseLinkDeclaration baseLink = getBaseLinkDeclaration(link, model);
      if (baseLink != null
            && !Objects.equal(link.getName(), baseLink.getName())) {
         Model baseLinkModel = (Model) baseLink
               .eContainer() // Links
               .eContainer(); // Model
         String msg = String.format(
               "Cannot change the name of a refined link; the model '%s'"
                     + " declares the same link with the name '%s'.",
               nameProvider.getFullyQualifiedName(baseLinkModel),
               baseLink.getName());
         error(msg, link, SystemDescriptorPackage.Literals.LINK_DECLARATION__NAME);
      }
   }

   private void checkLinkRefinesABaseLink(RefinedLinkDeclaration link, Model model) {
      if (getBaseLinkDeclaration(link, model) == null) {
         Links links = (Links) link.eContainer();
         String msg = String.format(
               "Cannot refine a link that is not declared in the refinement"
                     + " hierarchy of '%s'.",
               nameProvider.getFullyQualifiedName(model));
         int index = links.getDeclarations().indexOf(link);
         error(msg, links, SystemDescriptorPackage.Literals.LINKS__DECLARATIONS, index);
      }
   }

   private static BaseLinkDeclaration getBaseLinkDeclaration(
         RefinedLinkDeclaration refinedLink,
         Model model) {
      BaseLinkDeclaration baseLink = null;

      String refinedLinkRawSource = SdUtils.getRawSource(
            refinedLink,
            SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__SOURCE);
      String refinedLinkRawTarget = SdUtils.getRawSource(
            refinedLink,
            SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);

      model = model.getRefinedModel();
      while (model != null && baseLink == null) {
         if (model.getLinks() != null) {
            baseLink = model.getLinks().getDeclarations()
                  .stream()
                  .filter(l -> l instanceof BaseLinkDeclaration)
                  .map(l -> (BaseLinkDeclaration) l)
                  .filter(l -> areLinksEqual(l, refinedLinkRawSource, refinedLinkRawTarget))
                  .findFirst()
                  .orElse(null);
         }

         model = model.getRefinedModel();
      }

      return baseLink;
   }

   private static boolean areLinksEqual(
         BaseLinkDeclaration baseLink,
         String refinedLinkRawSource,
         String refinedLinkRawTarget) {
      String baseLinkRawSource = SdUtils.getRawSource(
            baseLink,
            SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__SOURCE);
      String baseLinkRawTarget = SdUtils.getRawSource(
            baseLink,
            SystemDescriptorPackage.Literals.BASE_LINK_DECLARATION__TARGET);
      return refinedLinkRawSource.equals(baseLinkRawSource) && refinedLinkRawTarget.equals(baseLinkRawTarget);
   }
}
