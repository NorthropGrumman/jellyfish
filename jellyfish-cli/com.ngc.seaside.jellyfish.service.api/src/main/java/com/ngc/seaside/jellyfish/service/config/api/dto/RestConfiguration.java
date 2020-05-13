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
package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

/**
 * Defines the details for a {@link com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType#REST}
 * transport configuration.
 */
public class RestConfiguration {

   private NetworkAddress networkAddress;
   private NetworkInterface networkInterface;
   private int port;
   private String path;
   private String contentType;
   private HttpMethod httpMethod;

   public NetworkAddress getNetworkAddress() {
      return networkAddress;
   }

   public RestConfiguration setNetworkAddress(NetworkAddress networkAddress) {
      this.networkAddress = networkAddress;
      return this;
   }

   public NetworkInterface getNetworkInterface() {
      return networkInterface;
   }

   public RestConfiguration setNetworkInterface(NetworkInterface networkInterface) {
      this.networkInterface = networkInterface;
      return this;
   }

   public int getPort() {
      return port;
   }

   public RestConfiguration setPort(int port) {
      this.port = port;
      return this;
   }

   public String getPath() {
      return path;
   }

   public RestConfiguration setPath(String path) {
      this.path = path;
      return this;
   }

   public String getContentType() {
      return contentType;
   }

   public RestConfiguration setContentType(String contentType) {
      this.contentType = contentType;
      return this;
   }

   public HttpMethod getHttpMethod() {
      return httpMethod;
   }

   public RestConfiguration setHttpMethod(HttpMethod httpMethod) {
      this.httpMethod = httpMethod;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof RestConfiguration)) {
         return false;
      }
      RestConfiguration that = (RestConfiguration) o;
      return Objects.equals(this.networkAddress, that.networkAddress)
            && this.port == that.port
            && Objects.equals(this.path, that.path)
            && Objects.equals(this.contentType, that.contentType)
            && this.httpMethod == that.httpMethod;
   }

   @Override
   public int hashCode() {
      return Objects.hash(networkAddress, port, path, contentType, httpMethod);
   }

   @Override
   public String toString() {
      return "RestConfiguration[networkAddress=" + networkAddress
            + ", networkInterface=" + networkInterface
            + ", port=" + port
            + ", path=" + path
            + ", contentType=" + contentType
            + ", httpMethod=" + httpMethod + "]";
   }

}
