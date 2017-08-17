package com.ngc.seaside.jellyfish.cli.command.createjavacucumbertests.dto;

public class CucumberDto {

   private String className;
   private String packageName;


   public String getClassName() {
      return className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public String getPackageName() {
      return packageName;
   }

   public void setPackageName(String packageName) {
      this.packageName = packageName;
   }
}
