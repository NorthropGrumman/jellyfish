package com.ngc.seaside.jellyfish.service.config.api.dto;

import java.util.Objects;

/**
 * Defines the details for a {@link com.ngc.seaside.jellyfish.service.config.api.TransportConfigurationType#REST}
 * transport configuration.
 */
public class RestConfiguration {

   private NetworkAddress networkAddress;
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
      return Objects.equals(this.networkAddress, that.networkAddress) &&
             this.port == that.port &&
             Objects.equals(this.path, that.path) &&
             Objects.equals(this.contentType, that.contentType) &&
             this.httpMethod == that.httpMethod;
   }

   @Override
   public int hashCode() {
      return Objects.hash(networkAddress, port, path, contentType, httpMethod);
   }

   @Override
   public String toString() {
      return "RestConfiguration[networkAddress=" + networkAddress + ", port=" + port + ", path=" + path + ", contentType="
             + contentType + ", httpMethod=" + httpMethod + "]";
   }

}
