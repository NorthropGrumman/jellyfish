package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import java.util.List;

public class TriggerDto {
   private String triggerType;
   private String correlationMethod;
   private List<EventDto> eventProducers;
   private List<CompletenessDto> completionStatements;
   private List<InputDto> inputs;
   
   public static class CompletenessDto {
      private String input1Type;
      private String input2Type;
      private String input1GetterSnippet;
      private String input2GetterSnippet;
   }
   
   public static class EventDto {
      private String type;
      private String getterSnippet;
   }
}
