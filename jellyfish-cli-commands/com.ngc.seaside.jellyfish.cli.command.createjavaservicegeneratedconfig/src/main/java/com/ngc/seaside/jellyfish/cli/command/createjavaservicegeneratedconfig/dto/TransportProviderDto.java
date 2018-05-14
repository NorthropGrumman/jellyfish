package com.ngc.seaside.jellyfish.cli.command.createjavaservicegeneratedconfig.dto;

public class TransportProviderDto {

   private String topic;
   private String providerName;
   private String configurationType;
   private String componentName;
   private String module;

   /**
    * Returns the fully qualified name of the topic.
    * 
    * @return the fully qualified name of the topic
    */
   public String getTopic() {
      return topic;
   }

   public TransportProviderDto setTopic(String topic) {
      this.topic = topic;
      return this;
   }

   /**
    * Returns the package name of the topic.
    * 
    * @return the package name of the topic
    */
   public String getTopicPackage() {
      return topic.substring(0, topic.lastIndexOf('.'));
   }

   /**
    * Returns the simple class name of the topic.
    * 
    * @return the simple class name of the topic
    */
   public String getTopicType() {
      return topic.substring(topic.lastIndexOf('.') + 1);
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

   /**
    * Returns the fully qualified name of the Guice module for the transport provider.
    * 
    * @return the fully qualified name of the Guice module for the transport provider
    */
   public String getModule() {
      return module;
   }

   public TransportProviderDto setModule(String module) {
      this.module = module;
      return this;
   }

   /**
    * Returns the class name of the Guice module for the transport provider.
    * 
    * @return the class name of the Guice module for the transport provider
    */
   public String getModuleType() {
      return module.substring(module.lastIndexOf('.') + 1);
   }

   /**
    * Returns the package of the Guice module for the transport provider.
    * 
    * @return the package of the Guice module for the transport provider
    */
   public String getModulePackage() {
      return module.substring(0, module.lastIndexOf('.'));
   }
}
