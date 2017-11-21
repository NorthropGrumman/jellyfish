package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import java.util.List;

public class BasicPubSubDto {

   private String name;
   private String inputType;
   private String outputType;
   private String serviceMethod;
   private String publishMethod;
   private List<IOCorrelationDto> inputOutputCorrelations;
   
   public static class IOCorrelationDto {
      private String getterSnippet;
      private String setterSnippet;
   }
}
