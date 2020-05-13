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
package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import java.util.ArrayList;
import java.util.List;

public class ReceiveDto {

   private String topic;
   private String name;
   private String eventType;
   private List<String> basicScenarios = new ArrayList<>();
   private boolean hasCorrelations;

   public String getTopic() {
      return topic;
   }

   public ReceiveDto setTopic(String topic) {
      this.topic = topic;
      return this;
   }

   public String getName() {
      return name;
   }

   public ReceiveDto setName(String name) {
      this.name = name;
      return this;
   }

   public String getEventType() {
      return eventType;
   }

   public ReceiveDto setEventType(String eventType) {
      this.eventType = eventType;
      return this;
   }

   public List<String> getBasicScenarios() {
      return basicScenarios;
   }

   public ReceiveDto setBasicScenarios(List<String> basicScenarios) {
      this.basicScenarios = basicScenarios;
      return this;
   }

   public boolean hasCorrelations() {
      return hasCorrelations;
   }

   public ReceiveDto setHasCorrelations(boolean hasCorrelations) {
      this.hasCorrelations = hasCorrelations;
      return this;
   }

}
