/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.example.inhertance.databagsservice.event.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public abstract class D {

   public static final String TOPIC_NAME = "/data/com.ngc.example.inhertance.databagsservice.event.datatype.D";

   public static final com.ngc.blocs.service.event.api.IEventTopic<D> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, D.class);

   private int fieldD;

   public int getFieldD() {
      return fieldD;
   }

   public D setFieldD(int fieldD) {
      this.fieldD = fieldD;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.fieldD      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof D)) {
         return false;
      }

      D that = (D) obj;

      return          java.util.Objects.equals(this.fieldD, that.fieldD)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[fieldD=").append(fieldD).append("]");
      return builder.toString();
   }

}
