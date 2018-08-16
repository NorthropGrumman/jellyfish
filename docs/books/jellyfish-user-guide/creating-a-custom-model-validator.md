---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 8 Creating a Custom Model Validator
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
next-title: Ch. 9 Creating a Scenario Verb
next-page: creating-a-scenario-verb
prev-title: Ch. 7 Maintaining a Project
prev-page: maintaining-a-project
---
{% include base.html %}

Jellyfish provides an API that can be used to extend the System Descriptor language.  One of these extension points is
the ability to create custom validators that can validate models, data, or other modeling elements.  Product teams can
create validation rules they want to enforce in their models.  These validation rules are applied

* when an SD project is edited with Eclipse
* when an SD project is built using Gradle with the command `gradle build`  
* when an SD project is validated with Jellyfish with the command `jellyfish validate`

Validation rules are packaged in a validation plugins which are just JARs or OSGi bundles.

# Creating a New Validator
Validators are created in Java by implemening the `ISystemDescriptorValidator` interface.  A new project needs to be
created that contains the classes for the validator.  Users can use which build tool they prefer; this example will
use Gradle.

First, we need to ensure our project contains a `settings.gradle` file to name the project:

**settings.gradle**
```
rootProject.name = 'modeling.validation'
```

Next, we need to configure the project to include the relevant dependencies:

**build.gradle**
```groovy
group = 'com.mystuff'
version = '1.0.0-SNAPSHOT'

dependencies {
  api "com.ngc.seaside:systemdescriptor.model.api:$jellyfishVersion"
  api "com.ngc.seaside:systemdescriptor.service.api:$jellyfishVersion""
  implementation "com.google.inject:guice:4.1.0"
  implementation "com.google.inject.extensions:guice-multibindings:4.1.0"
}
```

The first two dependencies contain the API for extending Jellyfish.  The Guice dependencies are required because
Jellyfish uses Guice for dependency injection.  Note the property `$jellyfishVersion` should reference the version
of Jellyfish the product team is using.  The version of Guice should match the version that Jellyfish uses.  You can
find this version in the
[versions.gradle](https://github.ms.northgrum.com/CEACIDE/jellyfish/blob/master/versions.gradle) file.

**Additional Gradle setup**
```note-info
A Gradle project actually requires additional setup.  IE, it is usually necessary to apply the Java plugin to the a
Java project.  The complete configuration of a Java project with Gradle is outside the scope of this chapter, so we 
omit the details of the full project.  
```

## Implementing the Validator
Validation plugins need to implement the `ISystemDescriptorValidator` interface.  Most of the time, the
`AbstractSystemDescriptorValidator` can be extended instead of having to implement the interface directly.  When
extending this class, simply override the appropriate method.  The various methods are invoked to validate different
parts of the model.  For example,  override `validateModel` to validate a model object or override `validateScenario` to
validate a scenario.  Actual validation is accomplished using the `IValidationContext`.  The declare method can be used
to declare errors, warnings, or suggestions.  For example, the implementation below requires a model not be named "Foo".

**ExampleModelValidator.java**
```java
public class ExampleModelValidator extends AbstractSystemDescriptorValidator {
  @Override
  protected void validateModel(IValidationContext<IModel> context) {
    IModel model = context.getObject();
    if("Foo".equals(model.getName())) {
      context.declare(Severity.ERROR, "Foo is not a valid model name!", model).getName();
    }
  }
}
```

Note that the 3rd argument passed to `declare` must be the original object obtained via `context.getObject()`.
`declare` returns an instance of this object, so you invoke a getter on the result that contains the invalid property or
value. In the example above, we invoked `getName` to indicate the name field of the model was invalid.  You can invoke
most any getter method on the object that is being validated after calling `declare` to declare an error or issue on
that field. However, you cannot declare an error on the following:
* `getFullyQualifiedName` of either the `IModel` or `IData` interface.  This is a derived property, declare an error on
  the name field of the model instead.
* You cannot declare a error on a named child of any object (ie, something that implements `INamedChild`.  Instead, you
  need to validate the child during the appropriate callback.  For example, if you wanted to validate scenario of a
  model, you would override `validateScenario`  and perform that check in that method, not in `validateModel`.
* Note that all the children of a model may not be available during validation since validation is performed live as
  parsing progresses.  Always be sure to validate an object in the correct callback.

You can use the utilities in the `SystemDescriptors` to determine if an `IScenarioStep` is a given, when, or then step,
if a `IDataReferenceField` is an input or output field, or if a `IModelReferenceField` is a part or required model.  For
example:

**ExampleModelValidator.java**
```java
@Override
protected void validateDataReferenceField(IValidationContext<IDataReferenceField> context) {
  IDataReferenceField field = context.getObject();
  if(SystemDescriptors.isInput(field)) {
    // Validate input here.
  } else if(SystemDescriptors.isOutput(field)) {
    // Validate output here.
  }
}
```

A validator is a Guice managed component.  This means you can get dependencies injected via Guice with the `@Inject`
annotation.  For example, you could log something in your validator like this:

**ExampleModelValidator.java**
```java
public class ExampleModelValidator extends AbstractSystemDescriptorValidator {
  
  private final ILogService logService;

  @Inject
  public ExampleModelValidator(ILogSerivce logSerivce) {
  	this.logService = logService;
  }

  @Override
  protected void validateModel(IValidationContext<IModel> context) {
    IModel model = context.getObject();
    if("Foo".equals(model.getName())) {
      logService.error(ExampleModelValidator.class, "Found an invalid model name!");
      context.declare(Severity.ERROR, "Foo is not a valid model name!", model).getName();
    }
  }
}
```

## Packaging the Validator
Once the validator is complete, you need to write a Guice Module that will register the validator.  Continuing the
example above, we'll create a new module called `ExampleModelValidatorModule`:

**ExampleModelValidatorModule.java**
```java
public class ExampleModelValidatorModule extends AbstractModule {
  @Override
  protected void configure() {
    // Always use a multibinder when binding validators since there is more than one implementation.
    Multibinder<ISystemDescriptorValidator> multibinder = Multibinder.newSetBinder(
      binder(),
      ISystemDescriptorValidator.class);
    multibinder.addBinding().to(ExampleModelValidator.class);
  }
}
```

If your validator JAR contains multiple validators, bind them all here as shown above.

We need to tell Jellyfish how to find module implementation.  Create a new file in
`src/main/resources/META-INF/services`. Name the file `com.google.inject.Module`.  Inside the file, list the fully
qualified class name of the module. Assume the module is contained in the package
`com.mystuff.modeling.validation.module`.  The file would look like this:

**com.google.inject.IModule**
```plaintext
com.mystuff.modeling.validation.module.ExampleModelValidatorModule
```

If you have multiple modules, list them line by line.

Finally, we need to configure the package that contains the module to be exported.  This is needed when deploying the
plugin inside Eclipse.  By always doing this, we can use the same plugin both in Eclipse and with the JellyFish CLI that
runs outside of Eclipse.  Assume the module is contained in the package
`com.mystuff.modeling.validation.module`.  Include the following in your `build.gradle`:

**build.gradle**
```groovy
jar {
   manifest {
      attributes('Export-Package': 'com.mystuff.modeling.validation.module')
   }
}
```

# Using the New Validator
When we build the validator project with the command `gradle clean build`, a JAR file is produced in the directory
`build/libs`.  We need to deploy this JAR file to able to actually use the validator.  We can deploy the JAR a number of
ways depending on the use case.

## Using the Plugin with Gradle
It easiest way to use the use the validator is to configure it with Gradle.  We can do this by publishing the JAR that
contains the validator and by editing the `build.gradle` of a System Descriptor project we want the validator to be
applied to.  

First, we need to upload our JAR to a remote repository so it can be used when building System Descriptor projects.  Run
the command `gradle clean build upload` from the directory that contains the validation project to upload the JAR.

Recall our `SoftwareStore` example contains a `build.gradle` file that looks like this:

**build.gradle**
```groovy
buildscript {
    repositories {
        mavenLocal()

        maven {
            url nexusConsolidated
        }
    }
    
    dependencies {
        classpath 'com.ngc.seaside:jellyfish.cli.gradle.plugins:2.13.0'
    }
}

apply plugin: 'com.ngc.seaside.jellyfish.system-descriptor'

group = 'com.ngc.seaside'
version = '1.0.0-SNAPSHOT'
```

We need to update this file to list our new validator JAR as a _buildscript_ dependency.  We do this by editing the
`dependencies` section of the `buildscript` section:

**build.gradle**
```groovy
buildscript {
    repositories {
        mavenLocal()

        maven {
            url nexusConsolidated
        }
    }
    
    dependencies {
        classpath 'com.ngc.seaside:jellyfish.cli.gradle.plugins:2.13.0'
        // Declare the group, artifact, and version of the JAR that contains the validator here.
        classpath 'com.mystuff:modeling.validation:1.0.0-SNAPSHOT'
    }
}

apply plugin: 'com.ngc.seaside.jellyfish.system-descriptor'

group = 'com.ngc.seaside'
version = '1.0.0-SNAPSHOT'
```

We can now run `./gradlew clean build` on the `SoftwareStore` project to build the project.  The custom validator will
be used to ensure that no model declared in the `SoftwareStore` project has a name called `Foo`.

## Using the Plugin with the Jellyfish Command Line Interface
When running a Jellyfish command directly (such as `validate` or `create-java-service-project`) we can package the 
validator directly with Jellyfish.  To do this, simply copy the JAR file to the `lib` directory of the Jellyfish
distribution directory.  The validator will be automatically used when running Jellyfish.

## Deploying the Plugin to Eclipse
This deployment option is used to show validation errors and warnings directly inside Eclipse as a modeler is editing
files.  It is necessary to package the JAR into an Eclipse an update site to be able to use the JAR inside Eclipse.
TODO