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
public class Covariance {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.datps.event.common.datatype.Covariance";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Covariance> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Covariance.class);


   @Override
   public int hashCode() {
      return java.util.Objects.hash(
      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Covariance)) {
         return false;
      }

      return true;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName());
      return builder.toString();
   }

}
