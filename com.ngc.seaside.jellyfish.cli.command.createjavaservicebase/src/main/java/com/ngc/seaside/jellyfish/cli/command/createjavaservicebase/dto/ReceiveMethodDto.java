package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import com.ngc.seaside.jellyfish.cli.command.createjavaservice.dto.MethodDto;

public class ReceiveMethodDto extends MethodDto {

   private MethodDto interfaceMethod;
   private MethodDto publishMethod;
   private String eventSourceClassName;

   public MethodDto getInterfaceMethod() {
      return interfaceMethod;
   }

   public ReceiveMethodDto setInterfaceMethod(MethodDto interfaceMethod) {
      this.interfaceMethod = interfaceMethod;
      return this;
   }

   public String getEventSourceClassName() {
      return eventSourceClassName;
   }

   public ReceiveMethodDto setEventSourceClassName(String eventSourceClassName) {
      this.eventSourceClassName = eventSourceClassName;
      return this;
   }

   public MethodDto getPublishMethod() {
      return publishMethod;
   }

   public ReceiveMethodDto setPublishMethod(MethodDto publishMethod) {
      this.publishMethod = publishMethod;
      return this;
   }
}
