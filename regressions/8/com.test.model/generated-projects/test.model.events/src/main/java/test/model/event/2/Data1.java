/**
***************************************** UNCLASSIFIED ******************************************
*************************************************************************************************
************************* Copyright 2017 Northrop Grumman Corporation ***************************
* Author: Generated
*/
package test.model.event.2;

/**
 * This file has been generated and should not be edited directly.
 * @author Generated
 */
public class Data1 {

   public static final String TOPIC_NAME = "/data/test.model.event.2.Data1";

   public static final com.ngc.blocs.service.event.api.IEventTopic<Data1> TOPIC = 
      com.ngc.blocs.service.event.api.EventTopic.of(TOPIC_NAME, Data1.class);

   private float field2;

   public float getField2() {
      return field2;
   }

   public Data1 setField2(float field2) {
      this.field2 = field2;
      return this;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
         this.field2      );
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      }

      if(!(obj instanceof Data1)) {
         return false;
      }

      Data1 that = (Data1) obj;

      return          java.util.Objects.equals(this.field2, that.field2)      ;
   }

   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
	  builder.append(getClass().getSimpleName()).append(" - ");
      builder.append("[field2=").append(field2).append("]");
      return builder.toString();
   }

}
