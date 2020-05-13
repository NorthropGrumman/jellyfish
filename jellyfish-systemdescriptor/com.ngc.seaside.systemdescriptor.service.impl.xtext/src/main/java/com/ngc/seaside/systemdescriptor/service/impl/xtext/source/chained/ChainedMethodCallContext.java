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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained;

import com.google.common.base.Preconditions;
import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.data.IDataField;
import com.ngc.seaside.systemdescriptor.model.api.data.IEnumeration;
import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IDataReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenarioStep;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.IUnwrappable;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinCell;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinFeature;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinRow;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinScenario;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinStep;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinTable;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.GherkinTag;
import com.ngc.seaside.systemdescriptor.service.impl.gherkin.model.IGherkinUnwrappable;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.DetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.XtextSourceLocation;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.data.DataChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.data.DataFieldChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.data.EnumerationChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin.FeatureChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin.GherkinCellChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin.GherkinRowChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin.GherkinScenarioChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin.GherkinStepChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin.GherkinTableChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.gherkin.TagChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.metadata.MetadataChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.model.DataReferenceFieldChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.model.ModelChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.model.ModelReferenceFieldChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.scenario.ScenarioChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.scenario.ScenarioStepChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DataFieldDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Element;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Scenario;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.util.ITextRegion;

import java.util.Arrays;

public class ChainedMethodCallContext {

   private ILocationInFileProvider locationInFileProvider;

   public ChainedMethodCallContext(ILocationInFileProvider locationInFileProvider) {
      this.locationInFileProvider = locationInFileProvider;
   }

   public ILocationInFileProvider getLocationInFileProvider() {
      return locationInFileProvider;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   public <T> IChainedMethodCall<T> getChainedMethodCallForElement(T element, Object... context) {
      Preconditions.checkNotNull(element, "element may not be null");
      IChainedMethodCall methodCall = null;
      if (element instanceof IModel) {
         methodCall = new ModelChainedMethodCall((IModel) element,
                  unwrap(element, SystemDescriptorPackage.Literals.MODEL), this);
      } else if (element instanceof IData) {
         methodCall = new DataChainedMethodCall((IData) element, unwrap(element, SystemDescriptorPackage.Literals.DATA),
                  this);
      } else if (element instanceof IEnumeration) {
         methodCall = new EnumerationChainedMethodCall((IEnumeration) element,
                  unwrap(element, SystemDescriptorPackage.Literals.ENUMERATION),
                  this);
      } else if (element instanceof IScenario) {
         methodCall = new ScenarioChainedMethodCall((IScenario) element,
                  unwrap(element, SystemDescriptorPackage.Literals.SCENARIO), this);
      } else if (element instanceof IScenarioStep) {
         methodCall = new ScenarioStepChainedMethodCall((IScenarioStep) element,
                  unwrap(element, SystemDescriptorPackage.Literals.STEP), this);
      } else if (element instanceof IDataReferenceField) {
         methodCall = new DataReferenceFieldChainedMethodCall((IDataReferenceField) element,
                  unwrap(element, SystemDescriptorPackage.Literals.FIELD_DECLARATION), this);
      } else if (element instanceof IModelReferenceField) {
         methodCall = new ModelReferenceFieldChainedMethodCall((IModelReferenceField) element,
                  unwrap(element, SystemDescriptorPackage.Literals.BASE_PART_DECLARATION,
                           SystemDescriptorPackage.Literals.BASE_REQUIRE_DECLARATION),
                  this);
      } else if (element instanceof IDataField) {
         methodCall = new DataFieldChainedMethodCall((IDataField) element,
                  unwrap(element, SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION), this);
      } else if (element instanceof IMetadata) {
         if (context == null || context.length != 1) {
            throw new IllegalArgumentException("parent of metadata is required for context");
         }
         EObject parent = unwrap(context[0], EcorePackage.Literals.EOBJECT);
         Metadata metadata = getMetadata(parent);
         methodCall = new MetadataChainedMethodCall((IMetadata) element, metadata, this);
      } else if (element instanceof GherkinFeature) {
         methodCall = new FeatureChainedMethodCall((GherkinFeature) element, this);
      } else if (element instanceof GherkinTag) {
         methodCall = new TagChainedMethodCall((GherkinTag) element, this);
      } else if (element instanceof GherkinScenario) {
         methodCall = new GherkinScenarioChainedMethodCall((GherkinScenario) element, this);
      } else if (element instanceof GherkinStep) {
         methodCall = new GherkinStepChainedMethodCall((GherkinStep) element, this);
      } else if (element instanceof GherkinTable) {
         methodCall = new GherkinTableChainedMethodCall((GherkinTable) element, this);
      } else if (element instanceof GherkinRow) {
         methodCall = new GherkinRowChainedMethodCall((GherkinRow) element, this);
      } else if (element instanceof GherkinCell) {
         methodCall = new GherkinCellChainedMethodCall((GherkinCell) element, this);
      }
      if (methodCall == null) {
         throw new IllegalStateException("Cannot get chained method call instance for element " + element);
      }
      return methodCall;
   }

   public <E extends EObject> IDetailedSourceLocation getSourceLocation(EObject element, ITextRegion region) {
      return new XtextSourceLocation(element, region);
   }

   public <T> IDetailedSourceLocation getSourceLocation(IGherkinUnwrappable<T> element) {
      // Length information is not available from Gherkin.
      return DetailedSourceLocation.of(element.getPath(), element.getLineNumber(), element.getColumn(), -1);
   }

   @SuppressWarnings("unchecked")
   private static <E extends EObject> E unwrap(Object o, EClass... classes) {
      if (o instanceof IUnwrappable) {
         EObject unwrapped = ((IUnwrappable<? extends EObject>) o).unwrap();
         for (EClass cls : classes) {
            if (cls.isInstance(unwrapped)) {
               return (E) unwrapped;
            }
         }
         throw new IllegalArgumentException(o + " does not unwrap to " + Arrays.toString(classes));
      }
      throw new IllegalArgumentException(o + " is not of type " + IUnwrappable.class);
   }

   private static Metadata getMetadata(EObject parent) {
      if (SystemDescriptorPackage.Literals.ELEMENT.isInstance(parent)) {
         return ((Element) parent).getMetadata();
      } else if (SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION.isInstance(parent)) {
         return ((DataFieldDeclaration) parent).getDefinition().getMetadata();
      } else if (SystemDescriptorPackage.Literals.SCENARIO.isInstance(parent)) {
         return ((Scenario) parent).getMetadata();
      }
      throw new IllegalStateException("Cannot find metadata for " + parent);
   }
}
