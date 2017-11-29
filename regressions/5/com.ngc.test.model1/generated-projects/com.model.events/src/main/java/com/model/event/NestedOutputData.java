/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.model.event;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class NestedOutputData {

   public static final String TOPIC_NAME = "/data/com.model.event.NestedOutputData";

   public static final com.ngc.blocs.service.event.api.IEventTopic<NestedOutputData> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, NestedOutputData.class);

   private java.lang.String field6;

   public java.lang.String getField6() {
      return field6;
   }

   public NestedOutputData setField6(java.lang.String field6) {
      this.field6 = field6;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.field6      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof NestedOutputData)) {
         return false;
      }

      NestedOutputData that = (NestedOutputData) obj;

      return          java.util.Objects.equals(this.field6, that.field6)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[field6=").append(field6).append("]");
      return builder.toString();
   }

}
