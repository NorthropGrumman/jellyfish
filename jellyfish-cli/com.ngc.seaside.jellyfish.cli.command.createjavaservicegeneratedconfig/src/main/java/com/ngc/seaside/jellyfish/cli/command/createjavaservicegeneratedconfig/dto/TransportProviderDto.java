package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto;

public class TransportProviderDto {

   private String topicPackage;
   private String topicType;
   private String providerName;
   private String configurationType;
   private String componentName;

   /**
    * Returns the package name of the topic.
    * 
    * @return the package name of the topic
    */
   public String getTopicPackage() {
      return topicPackage;
   }

   public TransportProviderDto setTopicPackage(String topicPackage) {
      this.topicPackage = topicPackage;
      return this;
   }

   /**
    * Returns the simple class name of the topic.
    * 
    * @return the simple class name of the topic
    */
   public String getTopicType() {
      return topicType;
   }

   public TransportProviderDto setTopicType(String topicType) {
      this.topicType = topicType;
      return this;
   }

   /**
    * Returns the variable name used to reference the transport provider.
    * 
    * @return the variable name used to reference the transport provider
    */
   public String getProviderName() {
      return providerName;
   }

   public TransportProviderDto setProviderName(String providerName) {
      this.providerName = providerName;
      return this;
   }

   /**
    * Returns the class name of the generated configuration class.
    * 
    * @return the class name of the generated configuration class
    */
   public String getConfigurationType() {
      return configurationType;
   }

   public TransportProviderDto setConfigurationType(String configurationType) {
      this.configurationType = configurationType;
      return this;
   }

   /**
    * Returns the OSGI component name for the transport provider.
    * 
    * @return the OSGI component name for the transport provider
    */
   public String getComponentName() {
      return componentName;
   }

   public TransportProviderDto setComponentName(String componentName) {
      this.componentName = componentName;
      return this;
   }
   
}
