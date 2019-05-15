/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
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
