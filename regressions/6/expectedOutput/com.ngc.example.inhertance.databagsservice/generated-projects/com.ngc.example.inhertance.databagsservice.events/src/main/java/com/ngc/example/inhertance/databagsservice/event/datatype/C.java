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
public class C extends com.ngc.example.inhertance.databagsservice.event.datatype.D {

   public static final String TOPIC_NAME = "/data/com.ngc.example.inhertance.databagsservice.event.datatype.C";

   public static final com.ngc.blocs.service.event.api.IEventTopic<C> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, C.class);

   private int fieldC;

   public int getFieldC() {
      return fieldC;
   }

   public C setFieldC(int fieldC) {
      this.fieldC = fieldC;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
      super.hashCode(),         this.fieldC      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof C)) {
         return false;
      }

      C that = (C) obj;

      return super.equals(obj) &&         java.util.Objects.equals(this.fieldC, that.fieldC)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(super.toString());
      builder.append("[fieldC=").append(fieldC).append("]");
      return builder.toString();
   }

}
