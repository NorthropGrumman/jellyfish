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
public class StateVector {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.datps.event.common.datatype.StateVector";

   public static final com.ngc.blocs.service.event.api.IEventTopic<StateVector> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, StateVector.class);

   private com.ngc.seaside.threateval.datps.event.common.datatype.GPSTime validityTime;
   private com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 ecefPosition;
   private com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 ecefVelocity;
   private com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 ecefAcc;

   public com.ngc.seaside.threateval.datps.event.common.datatype.GPSTime getValidityTime() {
      return validityTime;
   }

   public StateVector setValidityTime(com.ngc.seaside.threateval.datps.event.common.datatype.GPSTime validityTime) {
      this.validityTime = validityTime;
      return this;
   }

   public com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 getEcefPosition() {
      return ecefPosition;
   }

   public StateVector setEcefPosition(com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 ecefPosition) {
      this.ecefPosition = ecefPosition;
      return this;
   }

   public com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 getEcefVelocity() {
      return ecefVelocity;
   }

   public StateVector setEcefVelocity(com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 ecefVelocity) {
      this.ecefVelocity = ecefVelocity;
      return this;
   }

   public com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 getEcefAcc() {
      return ecefAcc;
   }

   public StateVector setEcefAcc(com.ngc.seaside.threateval.datps.event.common.datatype.Vector3 ecefAcc) {
      this.ecefAcc = ecefAcc;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.validityTime,         this.ecefPosition,         this.ecefVelocity,         this.ecefAcc      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof StateVector)) {
         return false;
      }

      StateVector that = (StateVector) obj;

      return          java.util.Objects.equals(this.validityTime, that.validityTime) &&         java.util.Objects.equals(this.ecefPosition, that.ecefPosition) &&         java.util.Objects.equals(this.ecefVelocity, that.ecefVelocity) &&         java.util.Objects.equals(this.ecefAcc, that.ecefAcc)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[validityTime=").append(validityTime).append("]");
      builder.append("[ecefPosition=").append(ecefPosition).append("]");
      builder.append("[ecefVelocity=").append(ecefVelocity).append("]");
      builder.append("[ecefAcc=").append(ecefAcc).append("]");
      return builder.toString();
   }

}
