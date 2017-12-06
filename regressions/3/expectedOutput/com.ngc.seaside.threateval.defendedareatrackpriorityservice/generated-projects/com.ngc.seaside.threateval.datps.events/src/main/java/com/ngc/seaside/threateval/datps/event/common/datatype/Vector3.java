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
public class Vector3 {

   public static final String TOPIC_NAME = "/data/com.ngc.seaside.threateval.datps.event.common.datatype.Vector3";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Vector3> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Vector3.class);

   private float x;
   private float y;
   private float z;

   public float getX() {
      return x;
   }

   public Vector3 setX(float x) {
      this.x = x;
      return this;
   }

   public float getY() {
      return y;
   }

   public Vector3 setY(float y) {
      this.y = y;
      return this;
   }

   public float getZ() {
      return z;
   }

   public Vector3 setZ(float z) {
      this.z = z;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.x,         this.y,         this.z      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Vector3)) {
         return false;
      }

      Vector3 that = (Vector3) obj;

      return          java.util.Objects.equals(this.x, that.x) &&         java.util.Objects.equals(this.y, that.y) &&         java.util.Objects.equals(this.z, that.z)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[x=").append(x).append("]");
      builder.append("[y=").append(y).append("]");
      builder.append("[z=").append(z).append("]");
      return builder.toString();
   }

}
