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
package com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.metadata;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.AbstractChainedMethodCall;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.chained.ChainedMethodCallContext;
import com.ngc.seaside.systemdescriptor.service.impl.xtext.source.location.IDetailedSourceLocation;
import com.ngc.seaside.systemdescriptor.service.source.api.IChainedMethodCall;
import com.ngc.seaside.systemdescriptor.systemDescriptor.Metadata;

import org.eclipse.xtext.util.ITextRegion;

import javax.json.JsonObject;

public class MetadataChainedMethodCall extends AbstractChainedMethodCall<IMetadata> {

   private final IMetadata metadata;
   private final Metadata xtextMetadata;

   /**
    * @param metadata metadata
    * @param xtextMetadata xtext metadata
    * @param context context
    */
   public MetadataChainedMethodCall(IMetadata metadata, Metadata xtextMetadata,
                                    ChainedMethodCallContext context) {
      super(metadata, context);
      this.metadata = metadata;
      this.xtextMetadata = xtextMetadata;
      try {
         register(IMetadata.class.getMethod("getJson"), this::thenGetJson);
      } catch (NoSuchMethodException e) {
         throw new AssertionError(e);
      }
   }

   private IChainedMethodCall<JsonObject> thenGetJson() {
      return new JsonObjectChainedMethodCall(metadata.getJson(), xtextMetadata.getJson(), context);
   }

   @Override
   public IDetailedSourceLocation getLocation() {
      ITextRegion region = context.getLocationInFileProvider().getFullTextRegion(xtextMetadata);
      return context.getSourceLocation(xtextMetadata, region);
   }

}
