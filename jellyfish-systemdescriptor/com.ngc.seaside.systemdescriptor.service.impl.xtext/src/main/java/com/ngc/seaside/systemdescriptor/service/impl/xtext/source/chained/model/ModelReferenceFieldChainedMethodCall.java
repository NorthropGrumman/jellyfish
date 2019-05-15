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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.model;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;
import com.ngc.seaside.systemdescriptor.model.api.model.IModelReferenceField;
import com.ngc.seaside.systemdescriptor.model.api.model.IReferenceField;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.WrappedChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.common.StringChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.source.api.ISourceLocation;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BasePartDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.BaseRequireDeclaration;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorPackage;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.util.ITextRegion;

public class ModelReferenceFieldChainedMethodCall extends AbstractChainedMethodCall<IModelReferenceField> {

   private final IModelReferenceField field;
   private final EObject xtextField;

   /**
    * @param field field
    * @param xtextField xtext field
    * @param context context
    */
   public ModelReferenceFieldChainedMethodCall(IModelReferenceField field, EObject xtextField,
                                               ChainedMethodCallContext context) {
      super(field, context);

      this.field = field;
      this.xtextField = xtextField;
      try {
         register(IReferenceField.class.getMethod("getName"), this::thenGetName);
         register(IReferenceField.class.getMethod("getParent"), this::thenGetParent);
         register(IReferenceField.class.getMethod("getMetadata"), this::thenGetMetadata);
         register(IModelReferenceField.class.getMethod("getType"), this::thenGetType);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<String> thenGetName() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextField,
               SystemDescriptorPackage.Literals.DATA_FIELD_DECLARATION__NAME, 0);
      IDetailedSourceLocation location = context.getSourceLocation(xtextField, region);
      return new StringChainedMethodCall(location, context);
   }

   private IChainedMethodCall<IModel> thenGetType() {
      EReference reference;
      if (xtextField instanceof BaseRequireDeclaration) {
         reference = SystemDescriptorPackage.Literals.BASE_REQUIRE_DECLARATION__TYPE;
      } else if (xtextField instanceof BasePartDeclaration) {
         reference = SystemDescriptorPackage.Literals.BASE_PART_DECLARATION__TYPE;
      } else {
         throw new UnsupportedOperationException("Getting the type for " + xtextField + " has not been implemented");
      }
      IChainedMethodCall<IModel> call = context.getChainedMethodCallForElement(field.getType());
      return new WrappedChainedMethodCall<IModel>(call) {
         @Override
         public ISourceLocation getLocation() {
            ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextField, reference, 0);
            IDetailedSourceLocation location = context.getSourceLocation(xtextField, region);
            return location;
         }
      };
   }

   private IChainedMethodCall<IModel> thenGetParent() {
      return context.getChainedMethodCallForElement(field.getParent(), context);
   }

   private IChainedMethodCall<IMetadata> thenGetMetadata() {
      return context.getChainedMethodCallForElement(field.getMetadata(), field);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextField);
      return context.getSourceLocation(xtextField, region);
   }

}
