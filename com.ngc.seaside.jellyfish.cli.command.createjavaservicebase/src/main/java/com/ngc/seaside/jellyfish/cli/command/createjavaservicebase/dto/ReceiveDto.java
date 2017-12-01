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
