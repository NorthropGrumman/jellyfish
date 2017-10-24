/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.threateval.datps.event.common.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class GPSTime {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.datps.event.common.datatype.GPSTime";

   public static final com.ngc.blocs.service.event.api.IEventTopic<GPSTime> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, GPSTime.class);

   private int secondsFromGPS;
   private int microseconds;

   public int getSecondsFromGPS() {
      return secondsFromGPS;
   }

   public GPSTime setSecondsFromGPS(int secondsFromGPS) {
      this.secondsFromGPS = secondsFromGPS;
      return this;
   }

   public int getMicroseconds() {
      return microseconds;
   }

   public GPSTime setMicroseconds(int microseconds) {
      this.microseconds = microseconds;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.secondsFromGPS,         this.microseconds      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof GPSTime)) {
         return false;
      }

      GPSTime that = (GPSTime) obj;

      return          java.util.Objects.equals(this.secondsFromGPS, that.secondsFromGPS) &&         java.util.Objects.equals(this.microseconds, that.microseconds)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[secondsFromGPS=").append(secondsFromGPS).append("]");
      builder.append("[microseconds=").append(microseconds).append("]");
      return builder.toString();
   }

}
