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
package com.ngc.seaside.systemdescriptor.ui.contentassist;

import com.ngc.seaside.systemdescriptor.extension.IScenarioStepCompletionExtension;
import com.ngc.seaside.systemdescriptor.systemDescriptor.GivenDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Step;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;
import com.ngc.seaside.systemdescriptor.systemDescriptor.ThenDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.WhenDeclaration;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.util.ITextRegion;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

/**
 * Provides completion suggestions for scenario steps.
 */
public class SystemDescriptorProposalProvider extends AbstractSystemDescriptorProposalProvider {

   private IScenarioStepCompletionExtension scenarioStepCompletion;
   private ILocationInFileProvider locationInFileProvider;

   @Inject
   public SystemDescriptorProposalProvider(ILocationInFileProvider locationInFileProvider,
                                           IScenarioStepCompletionExtension extension) {
      this.locationInFileProvider = locationInFileProvider;
      this.scenarioStepCompletion = extension;
   }

   /**
    * Removes keyword suggestions like {@code and} and <code>}</code> within a scenario when completing a scenario step.
    * 
    * @param keyword  keyword
    * @param context  context
    * @param acceptor acceptor
    */
   public void completeKeyword(Keyword keyword, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
      EObject model = context.getCurrentModel();
      while (model != null) {
         if (SystemDescriptorPackage.Literals.SCENARIO.isInstance(model)) {
            // Remove keyword suggestions (like `and` and `}`) within a scenario when we are one a new line)
            INode currentNode = context.getCurrentNode();
            INode previousNode = currentNode.getPreviousSibling();
            int lineOfOffset;
            try {
               lineOfOffset = context.getDocument().getLineOfOffset(context.getOffset());
            } catch (BadLocationException e) {
               throw new IllegalStateException(e);
            }
            if (previousNode == null || lineOfOffset < previousNode.getEndLine()) {
               return;
            }
         }
         model = model.eContainer();
      }
      super.completeKeyword(keyword, context, acceptor);
   }

   @Override
   public void completeGivenStep_Keyword(EObject model, Assignment assignment, ContentAssistContext context,
            ICompletionProposalAcceptor acceptor) {
      getStep(context).ifPresent(step -> completeStepKeyword(step, context, acceptor));
   }

   @Override
   public void completeGivenStep_Parameters(EObject model, Assignment assignment, ContentAssistContext context,
            ICompletionProposalAcceptor acceptor) {
      getStep(context).ifPresent(step -> completeStepParameter(step, context, acceptor));
   }

   @Override
   public void completeWhenStep_Keyword(EObject model, Assignment assignment, ContentAssistContext context,
            ICompletionProposalAcceptor acceptor) {
      getStep(context).ifPresent(step -> completeStepKeyword(step, context, acceptor));
   }

   @Override
   public void completeWhenStep_Parameters(EObject model, Assignment assignment, ContentAssistContext context,
            ICompletionProposalAcceptor acceptor) {
      getStep(context).ifPresent(step -> completeStepParameter(step, context, acceptor));
   }

   @Override
   public void completeThenStep_Keyword(EObject model, Assignment assignment, ContentAssistContext context,
            ICompletionProposalAcceptor acceptor) {
      getStep(context).ifPresent(step -> completeStepKeyword(step, context, acceptor));
   }

   @Override
   public void completeThenStep_Parameters(EObject model, Assignment assignment, ContentAssistContext context,
            ICompletionProposalAcceptor acceptor) {
      getStep(context).ifPresent(step -> completeStepParameter(step, context, acceptor));
   }

   private void completeStepKeyword(Step step, ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
      Set<String> potentialKeywords = scenarioStepCompletion.completeKeyword(step, context.getPrefix());

      for (String potentialKeyword : potentialKeywords) {
         acceptor.accept(createCompletionProposal(potentialKeyword, context));
      }
   }

   private void completeStepParameter(Step step, ContentAssistContext context,
            ICompletionProposalAcceptor acceptor) {
      int index = 0;
      final int offset = context.getOffset();
      boolean insertParameter = false;
      for (; index < step.getParameters().size(); index++) {
         ITextRegion region = locationInFileProvider.getSignificantTextRegion(step,
                  SystemDescriptorPackage.Literals.STEP__PARAMETERS, index);
         if (region.getOffset() < offset && offset <= region.getOffset() + region.getLength()) {
            break;
         } else if (offset <= region.getOffset()) {
            insertParameter = true;
            break;
         }
      }
      if (index >= step.getParameters().size()) {
         insertParameter = true;
      }
      Set<String> potentialParameters =
               scenarioStepCompletion.completeStepParameter(step, index, insertParameter);

      for (String potentialParameter : potentialParameters) {
         acceptor.accept(createCompletionProposal(potentialParameter, context));
      }
   }

   /**
    * Returns the Step associated with the given context or {@link Optional#empty()} if there is none.
    * 
    * @param context context
    * @return step
    */
   private Optional<Step> getStep(ContentAssistContext context) {
      /*
      * For parameters, the current model is always the step.
      * For step keywords, the current node's semantic element is sometimes a step 
      */
      if (SystemDescriptorPackage.Literals.STEP.isInstance(context.getCurrentModel())) {
         return Optional.of((Step) context.getCurrentModel());
      }
      INode currentNode = context.getCurrentNode();
      Step step = getStep(currentNode, false);
      if (step == null) {
         step = getStep(currentNode.getPreviousSibling(), false);
      }
      if (step == null) {
         step = getStep(currentNode, true);
      }
      if (step == null) {
         step = getStep(currentNode.getPreviousSibling(), true);
      }
      return Optional.ofNullable(step);
   }

   private Step getStep(INode node, boolean getLastStepFromDeclaration) {
      if (node != null) {
         EObject semanticElement = node.getSemanticElement();
         if (SystemDescriptorPackage.Literals.STEP.isInstance(semanticElement)) {
            return (Step) semanticElement;
         } else if (getLastStepFromDeclaration) {
            List<? extends Step> steps;
            if (SystemDescriptorPackage.Literals.GIVEN_DECLARATION.isInstance(semanticElement)) {
               steps = ((GivenDeclaration) semanticElement).getSteps();
            } else if (SystemDescriptorPackage.Literals.WHEN_DECLARATION.isInstance(semanticElement)) {
               steps = ((WhenDeclaration) semanticElement).getSteps();
            } else if (SystemDescriptorPackage.Literals.THEN_DECLARATION.isInstance(semanticElement)) {
               steps = ((ThenDeclaration) semanticElement).getSteps();
            } else {
               steps = Collections.emptyList();
            }
            if (!steps.isEmpty()) {
               return steps.get(steps.size() - 1);
            }
         }
      }
      return null;
   }
}
