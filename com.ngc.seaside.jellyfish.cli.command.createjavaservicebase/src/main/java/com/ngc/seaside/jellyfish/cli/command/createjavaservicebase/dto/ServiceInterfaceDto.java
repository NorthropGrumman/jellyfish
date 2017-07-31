package com.ngc.seaside.jellyfish.cli.command.createjavaservicebase.dto;

import java.util.Set;

public class ServiceInterfaceDto {

   private Set<String> imports;
   private String interfaceName;
   private String packageName;

   public Set<String> getImports() {
      return imports;
   }

   public ServiceInterfaceDto setImports(Set<String> imports) {
      this.imports = imports;
      return this;
   }

   public String getInterfaceName() {
      return interfaceName;
   }

   public ServiceInterfaceDto setInterfaceName(String interfaceName) {
      this.interfaceName = interfaceName;
      return this;
   }

   public String getPackageName() {
      return packageName;
   }

   public ServiceInterfaceDto setPackageName(String packageName) {
      this.packageName = packageName;
      return this;
   }
}
