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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.model;

import com.ngc.seaside.systemdescriptor.model.api.INamedChildCollection;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.link.IModelLink;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.NamedChildCollectionChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.sd.ElementChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Model;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.util.ITextRegion;

import java.util.Collection;
import java.util.Optional;

public class ModelChainedMethodCall extends ElementChainedMethodCall<IModel, Model> {

   private final IModel model;

   /**
    * @param model model
    * @param xtextModel xtext context
    * @param context context
    */
   public ModelChainedMethodCall(IModel model, Model xtextModel, ChainedMethodCallContext context) {
      super(model, xtextModel, context);

      this.model = model;
      try {
         register(IModel.class.getMethod("getName"), this::thenGetName);
         register(IModel.class.getMethod("getParent"), this::thenGetParent);
         register(IModel.class.getMethod("getMetadata"), this::thenGetMetadata);
         register(IModel.class.getMethod("getProperties"), this::thenGetProperties);
         register(IModel.class.getMethod("getInputs"), this::thenGetInputs);
         register(IModel.class.getMethod("getOutputs"), this::thenGetOutputs);
         register(IModel.class.getMethod("getScenarios"), this::thenGetScenarios);
         register(IModel.class.getMethod("getParts"), this::thenGetParts);
         register(IModel.class.getMethod("getRequiredModels"), this::thenGetRequiredModels);
         register(IModel.class.getMethod("getRefinedModel"), this::thenGetRefinedModel);
         register(IModel.class.getMethod("getLinkByName", String.class), this::thenGetLinkByName);
         register(IModel.class.getMethod("getLinks"), this::thenGetLinks);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<IProperties> thenGetProperties() {
      throw new UnsupportedOperationException("Not implemented");
   }

   private IChainedMethodCall<INamedChildCollection<IModel, IDataReferenceField>> thenGetInputs() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement.getInput());
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new NamedChildCollectionChainedMethodCall<>(location, model.getInputs(),
               context::getChainedMethodCallForElement, context);
   }

   private IChainedMethodCall<INamedChildCollection<IModel, IDataReferenceField>> thenGetOutputs() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement.getOutput());
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new NamedChildCollectionChainedMethodCall<>(location, model.getOutputs(),
               context::getChainedMethodCallForElement, context);
   }

   private IChainedMethodCall<INamedChildCollection<IModel, IScenario>> thenGetScenarios() {
      EList<Scenario> xtextScenarios = xtextElement.getScenarios();
      IDetailedSourceLocation location;
      if (xtextScenarios.isEmpty()) {
         location = null;
      } else {
         ITextRegion firstRegion = context.getLocationInFileProvider().getFullTextRegion(xtextScenarios.get(0));
         ITextRegion lastRegion =
                  context.getLocationInFileProvider().getFullTextRegion(xtextScenarios.get(xtextScenarios.size() - 1));
         ITextRegion region = firstRegion.merge(lastRegion);
         location = context.getSourceLocation(xtextElement, region);
      }
      return new NamedChildCollectionChainedMethodCall<>(location, model.getScenarios(),
               context::getChainedMethodCallForElement, context);
   }

   private IChainedMethodCall<INamedChildCollection<IModel, IModelReferenceField>> thenGetParts() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement.getParts());
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new NamedChildCollectionChainedMethodCall<>(location, model.getParts(),
               context::getChainedMethodCallForElement, context);
   }

   private IChainedMethodCall<INamedChildCollection<IModel, IModelReferenceField>> thenGetRequiredModels() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextElement.getRequires());
      IDetailedSourceLocation location = context.getSourceLocation(xtextElement, region);
      return new NamedChildCollectionChainedMethodCall<>(location, model.getRequiredModels(),
               context::getChainedMethodCallForElement, context);
   }

   private IChainedMethodCall<Optional<IModel>> thenGetRefinedModel() {
      return null;
   }

   private IChainedMethodCall<IModelLink<?>> thenGetLinkByName(String name) {
      return null;
   }

   private IChainedMethodCall<Collection<IModelLink<?>>> thenGetLinks() {
      return null;
   }

   @Override
   protected IMetadata getMetadata() {
      return element.getMetadata();
   }
}
