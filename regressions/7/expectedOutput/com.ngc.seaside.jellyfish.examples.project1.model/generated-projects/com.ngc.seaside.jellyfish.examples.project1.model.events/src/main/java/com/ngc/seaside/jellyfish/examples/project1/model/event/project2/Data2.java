/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.jellyfish.examples.project1.model.event.project2;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class Data2 {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.jellyfish.examples.project1.model.event.project2.Data2";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Data2> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Data2.class);

   private int field2;

   public int getField2() {
      return field2;
   }

   public Data2 setField2(int field2) {
      this.field2 = field2;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.field2      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Data2)) {
         return false;
      }

      Data2 that = (Data2) obj;

      return          java.util.Objects.equals(this.field2, that.field2)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[field2=").append(field2).append("]");
      return builder.toString();
   }

}
