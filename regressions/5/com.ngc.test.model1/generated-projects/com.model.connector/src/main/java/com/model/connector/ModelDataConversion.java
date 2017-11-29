package com.model.connector;

public class ModelDataConversion {

   public static com.model.event.Data1 convert(com.model.Data1 from) {
      com.model.event.Data1 to = new com.model.event.Data1();

      to.setField1(from.getField1());
      to.setField3(convert(from.getField3()));
      to.setField5(from.getField5());
      to.setField6(from.getField6());
      to.setField7(from.getField7());

      return to;
   }

   public static com.model.event.Data2 convert(com.model.Data2 from) {
      com.model.event.Data2 to = new com.model.event.Data2();

      to.setField1(from.getField1());
      to.setField3(convert(from.getField3()));
      to.setField5(from.getField5());
      to.setField6(from.getField6());
      to.setField7(from.getField7());

      return to;
   }

   public static com.model.event.Data3 convert(com.model.Data3 from) {
      com.model.event.Data3 to = new com.model.event.Data3();

      to.setField1(from.getField1());
      to.setField3(convert(from.getField3()));
      to.setField5(from.getField5());
      to.setField6(from.getField6());
      to.setField7(from.getField7());

      return to;
   }

   public static com.model.event.NestedInputData convert(com.model.NestedInputData from) {
      com.model.event.NestedInputData to = new com.model.event.NestedInputData();

      to.setField4(from.getField4());

      return to;
   }

   public static com.model.Data4 convert(com.model.event.Data4 from) {
      com.model.Data4.Builder to = com.model.Data4.newBuilder();

      to.setField2(from.getField2());
      to.setField5(convert(from.getField5()));

      return to.build();
   }

   public static com.model.Data5 convert(com.model.event.Data5 from) {
      com.model.Data5.Builder to = com.model.Data5.newBuilder();

      to.setField2(from.getField2());
      to.setField5(convert(from.getField5()));

      return to.build();
   }

   public static com.model.NestedOutputData convert(com.model.event.NestedOutputData from) {
      com.model.NestedOutputData.Builder to = com.model.NestedOutputData.newBuilder();

      to.setField6(from.getField6());

      return to.build();
   }

}
