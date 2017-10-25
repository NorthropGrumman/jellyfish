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
public class SystemTrack {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrack";

   public static final com.ngc.blocs.service.event.api.IEventTopic<SystemTrack> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, SystemTrack.class);

   private com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrackIdentifier identifier;
   private com.ngc.seaside.threateval.datps.event.common.datatype.StateVector state;
   private com.ngc.seaside.threateval.datps.event.common.datatype.Covariance cov;

   public com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrackIdentifier getIdentifier() {
      return identifier;
   }

   public SystemTrack setIdentifier(com.ngc.seaside.threateval.datps.event.common.datatype.SystemTrackIdentifier identifier) {
      this.identifier = identifier;
      return this;
   }

   public com.ngc.seaside.threateval.datps.event.common.datatype.StateVector getState() {
      return state;
   }

   public SystemTrack setState(com.ngc.seaside.threateval.datps.event.common.datatype.StateVector state) {
      this.state = state;
      return this;
   }

   public com.ngc.seaside.threateval.datps.event.common.datatype.Covariance getCov() {
      return cov;
   }

   public SystemTrack setCov(com.ngc.seaside.threateval.datps.event.common.datatype.Covariance cov) {
      this.cov = cov;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.identifier,         this.state,         this.cov      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof SystemTrack)) {
         return false;
      }

      SystemTrack that = (SystemTrack) obj;

      return          java.util.Objects.equals(this.identifier, that.identifier) &&         java.util.Objects.equals(this.state, that.state) &&         java.util.Objects.equals(this.cov, that.cov)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[identifier=").append(identifier).append("]");
      builder.append("[state=").append(state).append("]");
      builder.append("[cov=").append(cov).append("]");
      return builder.toString();
   }

}
