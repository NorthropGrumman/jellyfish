package com.ngc.seaside.jellyfish.cli.gradle;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;

import java.io.File;

public class DerivedRootProjectPluginConvention {

   public static final String DEFAULT_SYSTEM_DESCRIPTOR_DIRECTORY_NAME = "models";
   public static final String DEFAULT_SYSTEM_DESCRIPTOR_TEST_DIRECTORY_NAME = "features";
   
   private String model;
   private Dependency systemDescriptor;
   private File systemDescriptorDirectory;
   private File systemDescriptorTestDirectory;
   
   private final Project project;
   
   public DerivedRootProjectPluginConvention(Project project) {
      this.project = project;
      this.systemDescriptorDirectory = new File(project.getBuildDir(), DEFAULT_SYSTEM_DESCRIPTOR_DIRECTORY_NAME);
      this.systemDescriptorTestDirectory = new File(project.getBuildDir(), DEFAULT_SYSTEM_DESCRIPTOR_TEST_DIRECTORY_NAME);
   }

   public final Project getProject() {
      return project;
   }
   
   public Dependency getSystemDescriptor() {
      return systemDescriptor;
   }

   public DerivedRootProjectPluginConvention setSystemDescriptor(Object dependencyNotation) {
      return this;
   }
   
   public String getModel() {
      return model;
   }
   
   public DerivedRootProjectPluginConvention setModel(String model) {
      this.model = model;
      return this;
   }
   
   public File getSystemDescriptorDirectory() {
      return systemDescriptorDirectory;
   }
   
   public DerivedRootProjectPluginConvention setSystemDescriptorDirectory(File file) {
      this.systemDescriptorDirectory = file;
      return this;
   }
   
   public File getSystemDescriptorTestDirectory() {
      return systemDescriptorTestDirectory;
   }
   
   public DerivedRootProjectPluginConvention setSystemDescriptorTestDirectory(File file) {
      this.systemDescriptorTestDirectory = file;
      return this;
   }
   
}
