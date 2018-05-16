package com.ngc.seaside.jellyfish.cli.gradle;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;

import java.io.File;

public class DerivedRootProjectExtension {

   public static final String DEFAULT_SYSTEM_DESCRIPTOR_DIRECTORY_NAME = "models";
   public static final String DEFAULT_SYSTEM_DESCRIPTOR_TEST_DIRECTORY_NAME = "features";

   private String model;
   private String deploymentModel;
   private Dependency project;
   private File directory;
   private File testDirectory;

   private final Project gradleProject;

   /**
    * Constructor
    * @param project used to construct this object
    */
   public DerivedRootProjectExtension(Project project) {
      this.gradleProject = project;
      this.directory = new File(project.getBuildDir(), DEFAULT_SYSTEM_DESCRIPTOR_DIRECTORY_NAME);
      this.testDirectory = new File(project.getBuildDir(), DEFAULT_SYSTEM_DESCRIPTOR_TEST_DIRECTORY_NAME);
   }

   /**
    *
    * @return Dependency for this project
    */
   public Dependency getProject() {
      return project;
   }

   /**
    *
    * @param dependencyNotation used to set the dependency for the project
    * @return DerivedRootProjectExtension for this project
    */
   public DerivedRootProjectExtension setProject(Object dependencyNotation) {
      if (dependencyNotation == null) {
         project = null;
      } else {
         project = gradleProject.getDependencies().create(dependencyNotation);
      }
      return this;
   }

   public String getModel() {
      return model;
   }

   public DerivedRootProjectExtension setModel(String model) {
      this.model = model;
      return this;
   }

   public String getDeploymentModel() {
      return deploymentModel;
   }

   public DerivedRootProjectExtension setDeploymentModel(String deploymentModel) {
      this.deploymentModel = deploymentModel;
      return this;
   }

   public File getDirectory() {
      return directory;
   }

   public DerivedRootProjectExtension setDirectory(File file) {
      this.directory = file;
      return this;
   }

   public File getTestDirectory() {
      return testDirectory;
   }

   public DerivedRootProjectExtension setTestDirectory(File file) {
      this.testDirectory = file;
      return this;
   }

}
