/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package com.ngc.seaside.threateval.ctps.event.common.datatype;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public abstract class BaseData {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.ctps.event.common.datatype.BaseData";

   public static final com.ngc.blocs.service.event.api.IEventTopic<BaseData> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, BaseData.class);

   private com.ngc.seaside.threateval.ctps.event.common.datatype.Metadata header;

   public com.ngc.seaside.threateval.ctps.event.common.datatype.Metadata getHeader() {
      return header;
   }

   public BaseData setHeader(com.ngc.seaside.threateval.ctps.event.common.datatype.Metadata header) {
      this.header = header;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.header      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof BaseData)) {
         return false;
      }

      BaseData that = (BaseData) obj;

      return          java.util.Objects.equals(this.header, that.header)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[header=").append(header).append("]");
      return builder.toString();
   }

}
