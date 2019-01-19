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
package com.ngc.seaside.jellyfish.service.name;

import com.ngc.seaside.jellyfish.api.IJellyFishCommandOptions;
import com.ngc.seaside.systemdescriptor.model.api.ISystemDescriptor;
import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Optional;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 * Contains constants and util methods dealing with metadata.
 */
public class MetadataNames {

   public static final String CODEGEN = "codegen";
   public static final String CODEGEN_ALIAS = "alias";

   private MetadataNames() {
   }

   /**
    * Gets the alternate name or alias of the model that should be used when performing code generation if one has been
    * provided.
    */
   public static Optional<String> getAlias(IModel model) {
      // Try to find the model.
      String alias = null;
      if (model.getMetadata() != null) {
         // Use the codegen.alias metadata property to name the model.
         JsonValue codegen = model.getMetadata().getJson().get(MetadataNames.CODEGEN);
         if (codegen != null && codegen.getValueType() == JsonValue.ValueType.OBJECT) {
            JsonString aliasJson = ((JsonObject) codegen).getJsonString(MetadataNames.CODEGEN_ALIAS);
            alias = aliasJson == null ? null : aliasJson.getString();
         }
      }
      return Optional.ofNullable(alias);
   }

   /**
    * Gets the alternate name or alias of the model that should be used when performing code generation if one has been
    * provided.
    */
   public static Optional<String> getModelAlias(IJellyFishCommandOptions options, String fullyQualifiedName) {
      // Try to find the model.
      Optional<String> alias = Optional.empty();
      ISystemDescriptor systemDescriptor = options.getSystemDescriptor();
      if (systemDescriptor != null) {
         IModel model = options.getSystemDescriptor().findModel(fullyQualifiedName).orElse(null);
         if (model != null) {
            alias = getAlias(model);
         }
      }
      return alias;
   }
}
