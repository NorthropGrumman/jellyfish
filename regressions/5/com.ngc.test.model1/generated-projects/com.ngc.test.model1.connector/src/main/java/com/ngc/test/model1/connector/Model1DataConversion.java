package com.ngc.test.model1.connector;

public class Model1DataConversion {

   public static com.ngc.test.model1.event.Data1 convert(com.ngc.test.model1.Data1 from) {
      com.ngc.test.model1.event.Data1 to = new com.ngc.test.model1.event.Data1();

      to.setBooleanField(from.getBooleanField());
      to.setManyBooleanField(new java.util.ArrayList<>(from.getManyBooleanFieldList()));
      to.setIntField(from.getIntField());
      to.setManyIntField(new java.util.ArrayList<>(from.getManyIntFieldList()));
      to.setFloatField(from.getFloatField());
      to.setManyFloatField(new java.util.ArrayList<>(from.getManyFloatFieldList()));
      to.setStringField(from.getStringField());
      to.setManyStringField(new java.util.ArrayList<>(from.getManyStringFieldList()));
      to.setEnumField(convert(from.getEnumField()));
      to.setManyEnumField(new java.util.ArrayList<>(from.getManyEnumFieldCount()));
      for (com.ngc.test.model1.Enum1 value : from.getManyEnumFieldList()) {
         to.getManyEnumField().add(convert(value));
      }
      to.setDataField(convert(from.getDataField()));
      to.setManyDataField(new java.util.ArrayList<>(from.getManyDataFieldCount()));
      for (com.ngc.test.model1.Data2 value : from.getManyDataFieldList()) {
         to.getManyDataField().add(convert(value));
      }

      return to;
   }

   public static com.ngc.test.model1.event.Data2 convert(com.ngc.test.model1.Data2 from) {
      com.ngc.test.model1.event.Data2 to = new com.ngc.test.model1.event.Data2();


      return to;
   }

   public static com.ngc.test.model1.event.test2.Data3 convert(com.ngc.test.model1.test2.Data3 from) {
      com.ngc.test.model1.event.test2.Data3 to = new com.ngc.test.model1.event.test2.Data3();


      return to;
   }

   public static com.ngc.test.model1.event.Enum1 convert(com.ngc.test.model1.Enum1 from) {
      com.ngc.test.model1.event.Enum1 to = new com.ngc.test.model1.event.Enum1();


      return to;
   }

   public static com.ngc.test.model1.Data1 convert(com.ngc.test.model1.event.Data1 from) {
      com.ngc.test.model1.Data1.Builder to = com.ngc.test.model1.Data1.newBuilder();

      to.setBooleanField(from.getBooleanField());
      to.addAllManyBooleanField(from.getManyBooleanField());
      to.setIntField(from.getIntField());
      to.addAllManyIntField(from.getManyIntField());
      to.setFloatField(from.getFloatField());
      to.addAllManyFloatField(from.getManyFloatField());
      to.setStringField(from.getStringField());
      to.addAllManyStringField(from.getManyStringField());
      to.setEnumField(convert(from.getEnumField()));
      for (com.ngc.test.model1.event.Enum1 value : from.getManyEnumField()) {
         to.addManyEnumField(convert(value));
      }
      to.setDataField(convert(from.getDataField()));
      for (com.ngc.test.model1.event.Data2 value : from.getManyDataField()) {
         to.addManyDataField(convert(value));
      }

      return to.build();
   }

   public static com.ngc.test.model1.test2.Data4 convert(com.ngc.test.model1.event.test2.Data4 from) {
      com.ngc.test.model1.test2.Data4.Builder to = com.ngc.test.model1.test2.Data4.newBuilder();


      return to.build();
   }

   public static com.ngc.test.model1.Data5 convert(com.ngc.test.model1.event.Data5 from) {
      com.ngc.test.model1.Data5.Builder to = com.ngc.test.model1.Data5.newBuilder();


      return to.build();
   }

   public static com.ngc.test.model1.Enum1 convert(com.ngc.test.model1.event.Enum1 from) {
      com.ngc.test.model1.Enum1.Builder to = com.ngc.test.model1.Enum1.newBuilder();


      return to.build();
   }

   public static com.ngc.test.model1.Data2 convert(com.ngc.test.model1.event.Data2 from) {
      com.ngc.test.model1.Data2.Builder to = com.ngc.test.model1.Data2.newBuilder();


      return to.build();
   }

}
