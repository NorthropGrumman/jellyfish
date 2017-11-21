package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto2;

public class PublishDto {
   private String type;
   private String name;
   private String topic;
   
	public String getType() {
		return type;
	}
	
	public PublishDto setType(String type) {
		this.type = type;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public PublishDto setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public PublishDto setTopic(String topic) {
		this.topic = topic;
		return this;
	}
   
   
}
