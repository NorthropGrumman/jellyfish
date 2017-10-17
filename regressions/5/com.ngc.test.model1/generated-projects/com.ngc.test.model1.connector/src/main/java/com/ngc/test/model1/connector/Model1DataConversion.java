package com.ngc.test.model1.connector;

public class Model1DataConversion {

   public static com.ngc.test.model1.event.Data1 convert(com.ngc.test.model1.Data1 from) {
      com.ngc.test.model1.event.Data1 to = new com.ngc.test.model1.event.Data1();

      to.setManyFloatField(new java.util.ArrayList<>(from.getManyFloatFieldList()));
      to.setManyIntField(new java.util.ArrayList<>(from.getManyIntFieldList()));
      to.setStringField(from.getStringField());
      to.setBooleanField(from.getBooleanField());
      to.setManyStringField(new java.util.ArrayList<>(from.getManyStringFieldList()));
      to.setManyDataField(new java.util.ArrayList<>(from.getManyDataFieldCount()));
      for (com.ngc.test.model1.Data2 value : from.getManyDataFieldList()) {
         to.getManyDataField().add(convert(value));
      }
      to.setManyBooleanField(new java.util.ArrayList<>(from.getManyBooleanFieldList()));
      to.setEnumField(convert(from.getEnumField()));
      to.setDataField(convert(from.getDataField()));
      to.setManyEnumField(new java.util.ArrayList<>(from.getManyEnumFieldCount()));
      for (com.ngc.test.model1.Enum1 value : from.getManyEnumFieldList()) {
         to.getManyEnumField().add(convert(value));
      }
      to.setFloatField(from.getFloatField());
      to.setIntField(from.getIntField());

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

      to.addAllManyFloatField(from.getManyFloatField());
      to.addAllManyIntField(from.getManyIntField());
      to.setStringField(from.getStringField());
      to.setBooleanField(from.getBooleanField());
      to.addAllManyStringField(from.getManyStringField());
      for (com.ngc.test.model1.event.Data2 value : from.getManyDataField()) {
         to.addManyDataField(convert(value));
      }
      to.addAllManyBooleanField(from.getManyBooleanField());
      to.setEnumField(convert(from.getEnumField()));
      to.setDataField(convert(from.getDataField()));
      for (com.ngc.test.model1.event.Enum1 value : from.getManyEnumField()) {
         to.addManyEnumField(convert(value));
      }
      to.setFloatField(from.getFloatField());
      to.setIntField(from.getIntField());

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
