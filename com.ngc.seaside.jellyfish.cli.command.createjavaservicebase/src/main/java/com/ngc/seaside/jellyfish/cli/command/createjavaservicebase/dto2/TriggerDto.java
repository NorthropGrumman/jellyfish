package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import java.util.List;

public class TriggerDto {
   public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public String getCorrelationMethod() {
		return correlationMethod;
	}

	public void setCorrelationMethod(String correlationMethod) {
		this.correlationMethod = correlationMethod;
	}

	public List<EventDto> getEventProducers() {
		return eventProducers;
	}

	public void setEventProducers(List<EventDto> eventProducers) {
		this.eventProducers = eventProducers;
	}

	public List<CompletenessDto> getCompletionStatements() {
		return completionStatements;
	}

	public void setCompletionStatements(List<CompletenessDto> completionStatements) {
		this.completionStatements = completionStatements;
	}

	public List<InputDto> getInputs() {
		return inputs;
	}

	public void setInputs(List<InputDto> inputs) {
		this.inputs = inputs;
	}

   private String triggerType;
   private String correlationMethod;
   private String name;
   
   public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

private List<EventDto> eventProducers;
   private List<CompletenessDto> completionStatements;
   private List<InputDto> inputs;
   
   public static class CompletenessDto {
      private String input1Type;
      private String input2Type;
      public String getInput1Type() {
		return input1Type;
	}
	public void setInput1Type(String input1Type) {
		this.input1Type = input1Type;
	}
	public String getInput2Type() {
		return input2Type;
	}
	public void setInput2Type(String input2Type) {
		this.input2Type = input2Type;
	}
	public String getInput1GetterSnippet() {
		return input1GetterSnippet;
	}
	public void setInput1GetterSnippet(String input1GetterSnippet) {
		this.input1GetterSnippet = input1GetterSnippet;
	}
	public String getInput2GetterSnippet() {
		return input2GetterSnippet;
	}
	public void setInput2GetterSnippet(String input2GetterSnippet) {
		this.input2GetterSnippet = input2GetterSnippet;
	}
	private String input1GetterSnippet;
      private String input2GetterSnippet;
   }
   
   public static class EventDto {
      private String type;
      public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGetterSnippet() {
		return getterSnippet;
	}
	public void setGetterSnippet(String getterSnippet) {
		this.getterSnippet = getterSnippet;
	}
	private String getterSnippet;
   }
}
