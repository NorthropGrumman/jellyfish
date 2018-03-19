package com.ngc.seaside.systemdescriptor.model.impl.xtext.declaration;

import com.ngc.seaside.systemdescriptor.model.api.metadata.IMetadata;
import com.ngc.seaside.systemdescriptor.model.api.model.properties.IProperties;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.metadata.WrappedMetadata;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.model.properties.WrappedProperties;
import com.ngc.seaside.systemdescriptor.model.impl.xtext.store.IWrapperResolver;
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
      if (definition != null && definition.getMetadata() != null) {
         metadata = WrappedMetadata.fromXtext(definition.getMetadata());
      }
      return metadata;
   }

   /**
    * Creates a new {@code IProperties} instance using the given {@code DeclarationDefinition}.  If the definition is
    * {@code null}, an empty IProperties is returned.
    */
   public static IProperties propertiesFromXtext(IWrapperResolver resolver, DeclarationDefinition definition) {
      IProperties properties = IProperties.EMPTY_PROPERTIES;
      if (definition != null && definition.getProperties() != null) {
         properties = new WrappedProperties(resolver, definition.getProperties());
      }
      return properties;
   }

   /**
    * Converts the given metadata and properties and returns a {@code DeclarationDefinition} which contains the
    * converted metadata and properties. Returns {@code null} if both {@code metadata} and {@code properties} are null
    * or empty.
    */
   public static DeclarationDefinition toXtext(IWrapperResolver resolver, IMetadata metadata, IProperties properties) {
      if ((metadata == null || metadata.getJson() == null || metadata.getJson().isEmpty())
         && (properties == null || properties.isEmpty())) {
         return null;
      }
      DeclarationDefinition xtext = SystemDescriptorFactory.eINSTANCE.createDeclarationDefinition();
      if (metadata != null && metadata.getJson() != null && !metadata.getJson().isEmpty()) {
         xtext.setMetadata(WrappedMetadata.toXtext(metadata));
      }
      if (properties != null && !properties.isEmpty()) {
         xtext.setProperties(WrappedProperties.toXtext(resolver, properties));
      }
      return xtext;
   }
}
