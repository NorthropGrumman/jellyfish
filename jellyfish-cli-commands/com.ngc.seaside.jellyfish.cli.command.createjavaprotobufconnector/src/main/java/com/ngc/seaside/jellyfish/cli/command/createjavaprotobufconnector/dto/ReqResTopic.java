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
