package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2.BasicPubSubDto.IOCorrelationDto;

import java.util.List;

public class CorrelationDto {
   private String name;
   private String outputType;
   private String serviceName;
   private String correlationType;
   private String inputLogFormat;
   private String publishMethod;
   private List<InputDto> inputs;
   private List<IOCorrelationDto> inputOutputCorrelations;
   
   public static class IOCorrelationDto {
      private String getterSnippet;
      private String setterSnippet;
      private String inputType;
   }
}
