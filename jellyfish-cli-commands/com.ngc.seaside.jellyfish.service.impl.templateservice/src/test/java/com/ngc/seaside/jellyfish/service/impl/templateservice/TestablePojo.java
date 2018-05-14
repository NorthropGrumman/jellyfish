package com.ngc.seaside.jellyfish.service.impl.templateservice;

public class TestablePojo {

   private final String firstName;
   private final String lastName;

   public TestablePojo(String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getLastName() {
      return lastName;
   }
}
