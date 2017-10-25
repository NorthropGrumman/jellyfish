/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.test.model1.event.test2;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class Data4 {

   public static final String TOPIC_NAME = "/data/com.ngc.test.model1.event.test2.Data4";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Data4> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Data4.class);


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

      if(!(obj instanceof Data4)) {
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
