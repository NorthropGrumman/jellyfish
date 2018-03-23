package com.ngc.seaside.jellyfish.cli.command.createjavaprotobufconnector.dto;

import com.ngc.seaside.systemdescriptor.model.api.data.IData;
import com.ngc.seaside.systemdescriptor.model.api.model.scenario.IScenario;

import java.util.Objects;

public class ReqResTopic {

   private IData request;
   private IData response;
   private IScenario scenario;

   public IData getRequest() {
      return request;
   }

   public ReqResTopic setRequest(IData request) {
      this.request = request;
      return this;
   }

   public IData getResponse() {
      return response;
   }

   public ReqResTopic setResponse(IData response) {
      this.response = response;
      return this;
   }

   public IScenario getScenario() {
      return scenario;
   }

   public ReqResTopic setScenario(IScenario scenario) {
      this.scenario = scenario;
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof ReqResTopic)) {
         return false;
      }
      ReqResTopic that = (ReqResTopic) o;
      return Objects.equals(request, that.request)
             && Objects.equals(response, that.response)
             && Objects.equals(scenario, that.scenario);
   }

   @Override
   public int hashCode() {
      return Objects.hash(request, response, scenario);
   }
}
