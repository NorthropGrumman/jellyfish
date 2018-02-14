package com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.systemDescriptor.DeclarationDefinition;
import com.ngc.seaside.systemdescriptor.systemDescriptor.SystemDescriptorFactory;

/**
 * Handles wrapping and unwrapping {@link DeclarationDefinition} instances.  {@code DeclarationDefinition}s are just
 * convenience containers to make the DSL easier to maintain.  Therefore, we just provide static operation to convert
 * from and to objects that are located inside the container.
 */
public class WrappedDeclarationDefinition {

   private WrappedDeclarationDefinition() {
   }

   /**
    * Creates a new {@code IMetadata} instance using the given {@code DeclarationDefinition}.  If the definition is
    * {@code null}, {@link IMetadata#EMPTY_METADATA} is returned.
    */
   public static IMetadata metadataFromXtext(DeclarationDefinition definition) {
      IMetadata metadata = IMetadata.EMPTY_METADATA;
      if (definition != null) {
         metadata = WrappedMetadata.fromXtext(definition.getMetadata());
      }
      return metadata;
   }

   /**
    * Converts the given metadata returning a {@code DeclarationDefinition} which contains the converted metadata.  If
    * {@code metadata} is {@code null} or is {@code empty} the returned definition will be {@code null}.
    */
   public static DeclarationDefinition toXtext(IMetadata metadata) {
      DeclarationDefinition xtext = null;
      if (metadata != null && metadata.getJson() != null && !metadata.getJson().isEmpty()) {
         xtext = SystemDescriptorFactory.eINSTANCE.createDeclarationDefinition();
         xtext.setMetadata(WrappedMetadata.toXtext(metadata));
      }
      return xtext;
   }

   // TODO TH: add support for properties here when the DSL is updated
}
