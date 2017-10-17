package com.ngc.example.inhertance.databagsservice.connector;

public class DataBagsServiceDataConversion {

   public static com.ngc.example.inhertance.databagsservice.event.datatype.B convert(com.ngc.example.inhertance.databagsservice.datatype.B from) {
      com.ngc.example.inhertance.databagsservice.event.datatype.B to = new com.ngc.example.inhertance.databagsservice.event.datatype.B();

      to.setFieldB1(from.getFieldB1());
      to.setFieldB2(convert(from.getFieldB2()));
      to.setFieldA(from.getFieldA());

      return to;
   }

   public static com.ngc.example.inhertance.databagsservice.event.datatype.C convert(com.ngc.example.inhertance.databagsservice.datatype.C from) {
      com.ngc.example.inhertance.databagsservice.event.datatype.C to = new com.ngc.example.inhertance.databagsservice.event.datatype.C();

      to.setFieldC(from.getFieldC());
      to.setFieldD(from.getFieldD());

      return to;
   }

   public static com.ngc.example.inhertance.databagsservice.datatype.C convert(com.ngc.example.inhertance.databagsservice.event.datatype.C from) {
      com.ngc.example.inhertance.databagsservice.datatype.C.Builder to = com.ngc.example.inhertance.databagsservice.datatype.C.newBuilder();

      to.setFieldC(from.getFieldC());
      to.setFieldD(from.getFieldD());

      return to.build();
   }

}
