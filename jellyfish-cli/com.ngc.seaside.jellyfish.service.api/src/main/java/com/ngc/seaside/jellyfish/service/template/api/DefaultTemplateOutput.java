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
