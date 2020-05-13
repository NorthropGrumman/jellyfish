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
package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

import com.google.common.base.Objects;

/**
 * Defines the configuration for a link that should use ZeroMQ's inter-process transport mechanism.
 */
public class ZeroMqIpcTransportConfiguration extends ZeroMqConfiguration {

   private String path;

   /**
    * The file path to use for the buffer. This value should be an absolute file path and it should not begin with
    * 'ipc://'.
    */
   public String getPath() {
      return path;
   }

   public ZeroMqIpcTransportConfiguration setPath(String path) {
      this.path = path;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof ZeroMqIpcTransportConfiguration)) {
         return false;
      }
      ZeroMqIpcTransportConfiguration that = (ZeroMqIpcTransportConfiguration) o;
      return Objects.equal(this.getConnectionType(), that.getConnectionType())
            && Objects.equal(this.getPath(), that.getPath());
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.getConnectionType(), this.getPath());
   }

   @Override
   public String toString() {
      return "ZeroMqIpcTransportConfiguration[connectionType=" + this.getConnectionType()
            + ",groupAddress=" + this.getPath()
            + "]";
   }
}
