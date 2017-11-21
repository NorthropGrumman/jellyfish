package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import java.util.List;

public class TriggerDto {
   private String triggerType;
   private List<?> eventProducers;
   
   public static class EventDto {
      private String type;
      private String getterSnippet;
   }
}
