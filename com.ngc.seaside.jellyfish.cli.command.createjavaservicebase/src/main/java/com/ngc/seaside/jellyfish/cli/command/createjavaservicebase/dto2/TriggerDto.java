package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

import java.util.List;

public class TriggerDto {
	
	private String triggerType;
	private String correlationMethod;
	private String name;
	private List<EventDto> eventProducers;
	private List<CompletenessDto> completionStatements;
	private List<InputDto> inputs;
	
	public String getTriggerType() {
		return triggerType;
	}

	public TriggerDto setTriggerType(String triggerType) {
		this.triggerType = triggerType;
		return this;
	}

	public String getCorrelationMethod() {
		return correlationMethod;
	}

	public TriggerDto setCorrelationMethod(String correlationMethod) {
		this.correlationMethod = correlationMethod;
		return this;
	}

	public List<EventDto> getEventProducers() {
		return eventProducers;
	}

	public TriggerDto setEventProducers(List<EventDto> eventProducers) {
		this.eventProducers = eventProducers;
		return this;
	}

	public List<CompletenessDto> getCompletionStatements() {
		return completionStatements;
	}

	public TriggerDto setCompletionStatements(List<CompletenessDto> completionStatements) {
		this.completionStatements = completionStatements;
		return this;
	}

	public List<InputDto> getInputs() {
		return inputs;
	}

	public TriggerDto setInputs(List<InputDto> inputs) {
		this.inputs = inputs;
		return this;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public static class CompletenessDto {
		private String input1Type;
		private String input2Type;
		private String input1GetterSnippet;
		private String input2GetterSnippet;
		
		public String getInput1Type() {
			return input1Type;
		}

		public CompletenessDto setInput1Type(String input1Type) {
			this.input1Type = input1Type;
			return this;
		}

		public String getInput2Type() {
			return input2Type;
		}

		public CompletenessDto setInput2Type(String input2Type) {
			this.input2Type = input2Type;
			return this;
		}

		public String getInput1GetterSnippet() {
			return input1GetterSnippet;
		}

		public CompletenessDto setInput1GetterSnippet(String input1GetterSnippet) {
			this.input1GetterSnippet = input1GetterSnippet;
			return this;
		}

		public String getInput2GetterSnippet() {
			return input2GetterSnippet;
		}

		public CompletenessDto setInput2GetterSnippet(String input2GetterSnippet) {
			this.input2GetterSnippet = input2GetterSnippet;
			return this;
		}
	}

	public static class EventDto {
		private String type;
		private String getterSnippet;

		public String getType() {
			return type;
		}

		public EventDto setType(String type) {
			this.type = type;
			return this;
		}

		public String getGetterSnippet() {
			return getterSnippet;
		}

		public EventDto setGetterSnippet(String getterSnippet) {
			this.getterSnippet = getterSnippet;
			return this;
		}

	}
}
