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
public class Data3 {

   public static final String TOPIC_NAME = "/data/com.model.event.Data3";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Data3> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Data3.class);

   private int field1;
   private com.model.event.NestedInputData field3;
   private int field5;
   private int field6;
   private int field7;

   public int getField1() {
      return field1;
   }

   public Data3 setField1(int field1) {
      this.field1 = field1;
      return this;
   }

   public com.model.event.NestedInputData getField3() {
      return field3;
   }

   public Data3 setField3(com.model.event.NestedInputData field3) {
      this.field3 = field3;
      return this;
   }

   public int getField5() {
      return field5;
   }

   public Data3 setField5(int field5) {
      this.field5 = field5;
      return this;
   }

   public int getField6() {
      return field6;
   }

   public Data3 setField6(int field6) {
      this.field6 = field6;
      return this;
   }

   public int getField7() {
      return field7;
   }

   public Data3 setField7(int field7) {
      this.field7 = field7;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.field1,         this.field3,         this.field5,         this.field6,         this.field7      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Data3)) {
         return false;
      }

      Data3 that = (Data3) obj;

      return          java.util.Objects.equals(this.field1, that.field1) &&         java.util.Objects.equals(this.field3, that.field3) &&         java.util.Objects.equals(this.field5, that.field5) &&         java.util.Objects.equals(this.field6, that.field6) &&         java.util.Objects.equals(this.field7, that.field7)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[field1=").append(field1).append("]");
      builder.append("[field3=").append(field3).append("]");
      builder.append("[field5=").append(field5).append("]");
      builder.append("[field6=").append(field6).append("]");
      builder.append("[field7=").append(field7).append("]");
      return builder.toString();
   }

}
