package com.ngc.test.model1.connector;

public class Model1DataConversion {

   public static com.ngc.test.model1.event.Data1 convert(com.ngc.test.model1.Data1 from) {
      com.ngc.test.model1.event.Data1 to = new com.ngc.test.model1.event.Data1();

      to.setBooleanField(from.getBooleanField());
      to.setManyBooleanField(new java.util.ArrayList<>(from.getManyBooleanFieldCount()));
      for (java.lang.Boolean value : from.getManyBooleanFieldList()) {
         to.getManyBooleanField().add(value);
      }
      to.setIntField(from.getIntField());
      to.setManyIntField(new java.util.ArrayList<>(from.getManyIntFieldCount()));
      for (java.lang.Integer value : from.getManyIntFieldList()) {
         to.getManyIntField().add(value);
      }
      to.setFloatField(from.getFloatField());
      to.setManyFloatField(new java.util.ArrayList<>(from.getManyFloatFieldCount()));
      for (java.lang.Float value : from.getManyFloatFieldList()) {
         to.getManyFloatField().add(value);
      }
      to.setStringField(from.getStringField());
      to.setManyStringField(new java.util.ArrayList<>(from.getManyStringFieldCount()));
      for (java.lang.String value : from.getManyStringFieldList()) {
         to.getManyStringField().add(value);
      }
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
      for (java.lang.Boolean value : from.getManyBooleanField()) {
         to.addManyBooleanField(value);
      }
      to.setIntField(from.getIntField());
      for (java.lang.Integer value : from.getManyIntField()) {
         to.addManyIntField(value);
      }
      to.setFloatField(from.getFloatField());
      for (java.lang.Float value : from.getManyFloatField()) {
         to.addManyFloatField(value);
      }
      to.setStringField(from.getStringField());
      for (java.lang.String value : from.getManyStringField()) {
         to.addManyStringField(value);
      }
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
