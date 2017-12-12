/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.jellyfish.examples.project1.model.event;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class Data1 {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.jellyfish.examples.project1.model.event.Data1";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Data1> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Data1.class);

   private int field1;

   public int getField1() {
      return field1;
   }

   public Data1 setField1(int field1) {
      this.field1 = field1;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.field1      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Data1)) {
         return false;
      }

      Data1 that = (Data1) obj;

      return          java.util.Objects.equals(this.field1, that.field1)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[field1=").append(field1).append("]");
      return builder.toString();
   }

}
