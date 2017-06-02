package com.ngc.seaside.systemdescriptor.service.impl.xtext;

import com.google.inject.AbstractModule;

public class TestableModule extends AbstractModule {

   @Override
   protected void configure() {
      bind(ITestableComponent.class).to(TestableComponent.class);
   }

   public interface ITestableComponent {

   }

   public static class TestableComponent implements ITestableComponent {

   }

}
