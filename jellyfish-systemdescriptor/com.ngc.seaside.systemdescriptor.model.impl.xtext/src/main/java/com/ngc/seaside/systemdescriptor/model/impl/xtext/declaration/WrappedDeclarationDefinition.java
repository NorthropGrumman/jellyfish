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
