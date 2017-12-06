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
public class Data4 {

   public static final String TOPIC_NAME = "/data/com.model.event.Data4";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Data4> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Data4.class);

   private int field2;
   private com.model.event.NestedOutputData field5;

   public int getField2() {
      return field2;
   }

   public Data4 setField2(int field2) {
      this.field2 = field2;
      return this;
   }

   public com.model.event.NestedOutputData getField5() {
      return field5;
   }

   public Data4 setField5(com.model.event.NestedOutputData field5) {
      this.field5 = field5;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.field2,         this.field5      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Data4)) {
         return false;
      }

      Data4 that = (Data4) obj;

      return          java.util.Objects.equals(this.field2, that.field2) &&         java.util.Objects.equals(this.field5, that.field5)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[field2=").append(field2).append("]");
      builder.append("[field5=").append(field5).append("]");
      return builder.toString();
   }

}
