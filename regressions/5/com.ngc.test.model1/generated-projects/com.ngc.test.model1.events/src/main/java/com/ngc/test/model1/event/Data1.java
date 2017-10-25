/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.test.model1.event;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class Data1 {

   public static final String TOPIC_NAME = "/data/com.ngc.test.model1.event.Data1";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Data1> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Data1.class);

   private boolean booleanField;
   private java.util.List<java.lang.Boolean> manyBooleanField;
   private int intField;
   private java.util.List<java.lang.Integer> manyIntField;
   private float floatField;
   private java.util.List<java.lang.Float> manyFloatField;
   private java.lang.String stringField;
   private java.util.List<java.lang.String> manyStringField;
   private com.ngc.test.model1.event.Enum1 enumField;
   private java.util.List<com.ngc.test.model1.event.Enum1> manyEnumField;
   private com.ngc.test.model1.event.Data2 dataField;
   private java.util.List<com.ngc.test.model1.event.Data2> manyDataField;

   public boolean getBooleanField() {
      return booleanField;
   }

   public Data1 setBooleanField(boolean booleanField) {
      this.booleanField = booleanField;
      return this;
   }

   public java.util.List<java.lang.Boolean> getManyBooleanField() {
      return manyBooleanField;
   }

   public Data1 setManyBooleanField(java.util.List<java.lang.Boolean> manyBooleanField) {
      this.manyBooleanField = manyBooleanField;
      return this;
   }

   public int getIntField() {
      return intField;
   }

   public Data1 setIntField(int intField) {
      this.intField = intField;
      return this;
   }

   public java.util.List<java.lang.Integer> getManyIntField() {
      return manyIntField;
   }

   public Data1 setManyIntField(java.util.List<java.lang.Integer> manyIntField) {
      this.manyIntField = manyIntField;
      return this;
   }

   public float getFloatField() {
      return floatField;
   }

   public Data1 setFloatField(float floatField) {
      this.floatField = floatField;
      return this;
   }

   public java.util.List<java.lang.Float> getManyFloatField() {
      return manyFloatField;
   }

   public Data1 setManyFloatField(java.util.List<java.lang.Float> manyFloatField) {
      this.manyFloatField = manyFloatField;
      return this;
   }

   public java.lang.String getStringField() {
      return stringField;
   }

   public Data1 setStringField(java.lang.String stringField) {
      this.stringField = stringField;
      return this;
   }

   public java.util.List<java.lang.String> getManyStringField() {
      return manyStringField;
   }

   public Data1 setManyStringField(java.util.List<java.lang.String> manyStringField) {
      this.manyStringField = manyStringField;
      return this;
   }

   public com.ngc.test.model1.event.Enum1 getEnumField() {
      return enumField;
   }

   public Data1 setEnumField(com.ngc.test.model1.event.Enum1 enumField) {
      this.enumField = enumField;
      return this;
   }

   public java.util.List<com.ngc.test.model1.event.Enum1> getManyEnumField() {
      return manyEnumField;
   }

   public Data1 setManyEnumField(java.util.List<com.ngc.test.model1.event.Enum1> manyEnumField) {
      this.manyEnumField = manyEnumField;
      return this;
   }

   public com.ngc.test.model1.event.Data2 getDataField() {
      return dataField;
   }

   public Data1 setDataField(com.ngc.test.model1.event.Data2 dataField) {
      this.dataField = dataField;
      return this;
   }

   public java.util.List<com.ngc.test.model1.event.Data2> getManyDataField() {
      return manyDataField;
   }

   public Data1 setManyDataField(java.util.List<com.ngc.test.model1.event.Data2> manyDataField) {
      this.manyDataField = manyDataField;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.booleanField,         this.manyBooleanField,         this.intField,         this.manyIntField,         this.floatField,         this.manyFloatField,         this.stringField,         this.manyStringField,         this.enumField,         this.manyEnumField,         this.dataField,         this.manyDataField      );
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

      return          java.util.Objects.equals(this.booleanField, that.booleanField) &&         java.util.Objects.equals(this.manyBooleanField, that.manyBooleanField) &&         java.util.Objects.equals(this.intField, that.intField) &&         java.util.Objects.equals(this.manyIntField, that.manyIntField) &&         java.util.Objects.equals(this.floatField, that.floatField) &&         java.util.Objects.equals(this.manyFloatField, that.manyFloatField) &&         java.util.Objects.equals(this.stringField, that.stringField) &&         java.util.Objects.equals(this.manyStringField, that.manyStringField) &&         java.util.Objects.equals(this.enumField, that.enumField) &&         java.util.Objects.equals(this.manyEnumField, that.manyEnumField) &&         java.util.Objects.equals(this.dataField, that.dataField) &&         java.util.Objects.equals(this.manyDataField, that.manyDataField)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[booleanField=").append(booleanField).append("]");
      builder.append("[manyBooleanField=").append(manyBooleanField).append("]");
      builder.append("[intField=").append(intField).append("]");
      builder.append("[manyIntField=").append(manyIntField).append("]");
      builder.append("[floatField=").append(floatField).append("]");
      builder.append("[manyFloatField=").append(manyFloatField).append("]");
      builder.append("[stringField=").append(stringField).append("]");
      builder.append("[manyStringField=").append(manyStringField).append("]");
      builder.append("[enumField=").append(enumField).append("]");
      builder.append("[manyEnumField=").append(manyEnumField).append("]");
      builder.append("[dataField=").append(dataField).append("]");
      builder.append("[manyDataField=").append(manyDataField).append("]");
      return builder.toString();
   }

}
