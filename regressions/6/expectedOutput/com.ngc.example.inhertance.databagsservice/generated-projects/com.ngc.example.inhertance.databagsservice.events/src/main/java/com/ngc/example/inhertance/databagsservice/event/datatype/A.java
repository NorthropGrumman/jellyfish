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
public abstract class A {

   public static final String TOPIC_NAME = "/data/com.ngc.example.inhertance.databagsservice.event.datatype.A";

   public static final com.ngc.blocs.service.event.api.IEventTopic<A> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, A.class);

   private int fieldA;

   public int getFieldA() {
      return fieldA;
   }

   public A setFieldA(int fieldA) {
      this.fieldA = fieldA;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.fieldA      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof A)) {
         return false;
      }

      A that = (A) obj;

      return          java.util.Objects.equals(this.fieldA, that.fieldA)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[fieldA=").append(fieldA).append("]");
      return builder.toString();
   }

}
