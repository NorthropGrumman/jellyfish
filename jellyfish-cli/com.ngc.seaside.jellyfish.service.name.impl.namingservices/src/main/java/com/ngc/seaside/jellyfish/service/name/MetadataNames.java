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
