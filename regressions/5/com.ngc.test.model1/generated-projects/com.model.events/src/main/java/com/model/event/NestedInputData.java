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
public class NestedInputData {

   public static final String TOPIC_NAME = "/data/com.model.event.NestedInputData";

   public static final com.ngc.blocs.service.event.api.IEventTopic<NestedInputData> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, NestedInputData.class);

   private java.lang.String field4;

   public java.lang.String getField4() {
      return field4;
   }

   public NestedInputData setField4(java.lang.String field4) {
      this.field4 = field4;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.field4      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof NestedInputData)) {
         return false;
      }

      NestedInputData that = (NestedInputData) obj;

      return          java.util.Objects.equals(this.field4, that.field4)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[field4=").append(field4).append("]");
      return builder.toString();
   }

}
