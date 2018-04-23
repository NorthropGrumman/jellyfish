package com.ngc.seaside.jellyfish.service.template.api;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;


/**
 *
 */
public class DefaultTemplateOutput implements ITemplateOutput {

   private Map<String, ?> properties;
   private Path outputPath;

   @Override
   public Map<String, ?> getProperties() {
      return properties;
   }

   public DefaultTemplateOutput setProperties(Map<String, ?> properties) {
      this.properties = properties;
      return this;
   }

   @Override
   public Path getOutputPath() {
      return outputPath;
   }

   public DefaultTemplateOutput setOutputPath(Path outputPath) {
      this.outputPath = outputPath;
      return this;
   }

   @Override
   public String toString() {
      return String.format("properties: %s, outputPath: %s", properties, outputPath);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }
      if (!(obj instanceof ITemplateOutput)) {
         return false;
      }
      ITemplateOutput that = (ITemplateOutput) obj;
      return Objects.equals(properties, that.getProperties())
            && Objects.equals(outputPath, that.getOutputPath());
   }

   @Override
   public int hashCode() {
      return Objects.hash(properties, outputPath);
   }
}
