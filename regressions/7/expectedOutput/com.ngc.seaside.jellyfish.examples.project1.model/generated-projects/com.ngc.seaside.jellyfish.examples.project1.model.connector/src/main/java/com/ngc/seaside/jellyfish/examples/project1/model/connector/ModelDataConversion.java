package com.ngc.seaside.jellyfish.examples.project1.model.connector;

public class ModelDataConversion {

   public static com.ngc.seaside.jellyfish.examples.project1.model.event.project2.Data2 convert(com.ngc.seaside.jellyfish.examples.project1.model.project2.Data2 from) {
      com.ngc.seaside.jellyfish.examples.project1.model.event.project2.Data2 to = new com.ngc.seaside.jellyfish.examples.project1.model.event.project2.Data2();

      to.setField2(from.getField2());

      return to;
   }

   public static com.ngc.seaside.jellyfish.examples.project1.model.event.common.datatype.GPSTime convert(com.ngc.seaside.jellyfish.examples.project1.model.common.datatype.GPSTime from) {
      com.ngc.seaside.jellyfish.examples.project1.model.event.common.datatype.GPSTime to = new com.ngc.seaside.jellyfish.examples.project1.model.event.common.datatype.GPSTime();

      to.setSecondsFromGPS(from.getSecondsFromGPS());
      to.setMicroseconds(from.getMicroseconds());

      return to;
   }

   public static com.ngc.seaside.jellyfish.examples.project1.model.Data1 convert(com.ngc.seaside.jellyfish.examples.project1.model.event.Data1 from) {
      com.ngc.seaside.jellyfish.examples.project1.model.Data1.Builder to = com.ngc.seaside.jellyfish.examples.project1.model.Data1.newBuilder();

      to.setField1(from.getField1());

      return to.build();
   }

}
