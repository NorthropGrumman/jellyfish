---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Appendix C - The Jellyfish Command Line Interface
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
prev-title: Appendix B - Standard Scenario Verbs
prev-page: appendix-b
---
{% include base.html %}
Jellyfish is a product that can be used to perform various operations on SD models.  The input
to Jellyfish is the group ID, artifact ID and version of an SD project whose artifacts have been released to a remote
repository as discussed in
[Ch. 11 Configuration Management]({{ safebase }}/books/modeling-with-the-system-descriptor/configuration-management.html).
The latest version of Jellyfish can be found at https://github.ms.northgrum.com/CEACIDE/jellyfish/releases.  Jellyfish
provides a command line interface (CLI).  Jellyfish can be executed by unzipping the file and running the script
`bin/jellyfish` on Linux or `bin\jellyfish.bat` on Windows.

# Generating a Java Micro-service
Jellyfish can be used to generate a Java micro-service project from a model. Below is an example of running Jellyfish to
generate a micro service project for the modeling component
`com.ngc.seaside.archipelago.threat.EngagementTrackPriorityService`.

**Running Jellyfish**
```plaintext
./jellyfish create-java-service-project \
 gav="com.ngc.seaside.archipelago:archipelago:1.0.0" \
 model="com.ngc.seaside.threateval.EngagementTrackPriorityService" \
 outputDirectory="/example/service" \
 version=1.0.0-SNAPSHOT
```

Note the use of the `model` parameter that is used to identify the model/service to generate.  `gav` identifies the
particular artifact to retrieve from the remote repository.  `outputDirectory` identifies where to store the generated
project.

# Extending Jellyfish and the System Descriptor Language
Jellyfish is made up of a series of "commands".  Jellyfish and the System Descriptor Language itself can be extend in
the following ways.  Each of these techniques uses Jellyfish to provide the extension points.
* By creating commands for the Command Line Interface (CLI)
* By creating new validation rules for the System Descriptor DSL
* By creating new scenario step keywords or verbs for use in the DSL

## Creating new Jellyfish Commands
TODO

## Creating new validation rules for the System Descriptor Language
This section describes how to create your own validation rules that might be specific to your project or domain.These
validation rules will augment the Eclipse editor to allow for faster verification of model validity by the modeller.
Validation rules are typically implemented by a software developer, not the actual model.  Validation rules are packaged
in a validation plugins which are just JARs or OSGi bundles.

### Writing the Validation Plugin
In order to write a validation plugin, the following dependencies need to be added to the project.  The gradle syntax is
given below.  Use the version of the APIs appropriate to your project.

**build.gradle**
```groovy
dependencies {
  compile "com.ngc.seaside:systemdescriptor.model.api:1.1-SNAPSHOT"
  compile "com.ngc.seaside:systemdescriptor.service.api:1.1-SNAPSHOT"
  compile "com.google.inject:guice:3.0"
  compile "com.google.inject.extensions:guice-multibindings:3.0"
}
```

Validation plugins need to implement the `ISystemDescriptorValidator` interface.  Most of the time, the
`AbstractSystemDescriptorValidator` can be extended instead of having to implement the interface directly.  When
extending this class, simply override the appropriate method.  The various methods are invoked to validate different
parts of the model.  For example,  override `validateModel` to validate a model object or override `validateScenario` to
validate a scenario.  Actual validation is accomplished using the `IValidationContext`.  The declare method can be used
to declare errors, warnings, or suggestions.  For example, the implementation below requires a model not be named "Foo".

**ExampleModelValidator**
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

Note that the 3rd argument passed to `declare` must be the original object obtained via `context.getObject()`.  declare
returns an instance of this object, so you invoke a getter on the result that contains the invalid property or value.
In the example above, we invoked `getName` to indicate the name field of the model was invalid.  You can invoke most any
getter method on the object that is being validated after calling declare to declare an error or issue on that field.
However, you cannot declare an error on the following:
* `getFullyQualifiedName` of either the `IModel` or `IData` interface.  This is a derived property, declare an error on
  the name field of the model instead.
* You cannot declare a error on a named child of any object (ie, something that implements `INamedChild`.  Instead, you
  need to validate the child during the appropriate callback.  For example, if you wanted to validate scenario of a
  model, you would override `validateScenario`  and perform that check in that method, not in `validateModel`.
* Note that all the children of a model may not be available during validation since validation is performed live as
  parsing progresses.  Always be sure to validate an object in the correct callback.

You can use the utilities in the `SystemDescriptors` to determine if an `IScenarioStep`, if a `IDataReferenceField` is
an input or output field, or if a `IModelReferenceField` is a part or required model.  For example:

**ExampleModelValidator**
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
annotation.

### Packaging the Plugin
Once the plugin is complete, you need to write a Guice Module that will include the plugin.  Continuing the example
above, we'll create a new module called `ExampleModelValidatorModule`:

**ExampleModelValidatorModule**
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

If your plugin JAR contains multiple validators, bind them all here as shown above.

We need to tell JellyFish how to find module implementation.  Create a new file in
`src/main/resources/META-INF/services`. Name the file `com.google.inject.Module`.  Inside the file, list the fully
qualified class name of the module. Assume the module is contained in the package
`com.ngc.seaside.systemdescriptor.validation.impl.mymodel.module`.  The file would look like this:

**com.google.inject.IModule**
```plaintext
com.ngc.seaside.systemdescriptor.validation.impl.mymodel.module.ExampleModelValidatorModule
```

If you have multiple modules, list them line by line.

Finally, we need to configure the package that contains the module to be exported.  This is needed when deploying the
plugin inside Eclipse.  By always doing this, we can use the same plugin both in Eclipse and with the JellyFish CLI that
runs outside of Eclipse.  Assume the module is contained in the package
`com.ngc.seaside.systemdescriptor.validation.impl.mymodel.module`.  If you project extends seaside-gradle-parent,
include the following in your `build.gradle`:

**build.gradle**
```groovy
jar {
   manifest {
      attributes('Export-Package': 'com.ngc.seaside.systemdescriptor.validation.impl.mymodel.module')
   }
}
```

### Testing and Deploying the Plugin
To deploy the plugin with the JellyFish CLI, simply place the JAR on the classpath of the CLI application.  The plugin
should be picked up automatically.

It's possible to quickly test the plugin with Eclipse by following the directions at **Testing the Plugin with
Eclipse**.  To deploy the plugin in Eclipse, the plugin must be included in a p2 Update Site.  Follow the instructions
at **Building an Update Site for Eclipse** to build the update site.

## Creating new Scenario Step Keywords or Verbs
New scenario step keywords or verbs can also be implemented as plugins similar to how validators are implemented.  In
the DSL, scenarios are made up of steps: given, when, and then steps.  Each step starts with a keyword or verb and is
followed by optional parameters.  Consider the example below:

**Example Scenario Steps**
```
scenario triggerAlerts {
    given haveReceived alarmTimes
    when receiving currentTime
    then willAsk speaker ^to buzz
}
```

In this example, there are 3 steps: 1 given step, 1 when step, and 1 then step.  The verb for the given step step is
"haveReceived".  Likewise, the verbs for the when and then steps are "receiving" and "willAsk", respectively.  The
parameters of the given step is "alarmTimes".  The parameters for the when step is "currentTime" and the parameters for
the then step are "speaker", "to", and "buzz".

By convention, verbs used in given steps are in **past** tense.  Verbs in when steps are in **present** tense and verbs
in then steps are in **future** tense.

### Writing the Scenario Step Plugin
Plugins can be added to JellyFish that declare new verbs.  To do this, the `IScenarioStepHandler` interface must be
implemented.  Most of the time, the abstract class `AbstractStepHandler` can be extended.  Add the following
dependencies to your project:

**build.gradle**
```groovy
dependencies {
  compile "com.ngc.seaside:systemdescriptor.model.api:1.1-SNAPSHOT"
  compile "com.ngc.seaside:systemdescriptor.service.api:1.1-SNAPSHOT"
  compile "com.google.inject:guice:3.0"
  compile "com.google.inject.extensions:guice-multibindings:3.0"
}
```

Below is a step handler that provides the "cook" verb:

**CookStepHandler**
```java
public class CookStepHandler extends AbstractStepHandler {
  private final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("cooked");
  private final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("cooking");
  private final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willCook");
 
  public CookStepHandler () {
   register(PAST, PRESENT, FUTURE);
  }
 
  @Override
  protected void doValidateStep(IValidationContext<IScenarioStep> context) {
   // No extra validation needed right now.
  }
}
```

First, the 3 tenses of the verb are declared as `ScenarioStepVerb` objects.  They are registered in the constructor with
the `register` call.  The "cooked" keyword can be now be used in a given step.  Likewise, "cooking", and "willCook" can be
used in `when` and `then` steps.

Step handlers that extend `AbstractStepHandler` are also validators.  Step handlers must validate that the verbs are
used correctly and the step is valid.  Extending classes do this validation by implementing `doValidateStep`.  This uses
the same validation API as discussed above.  Most of the time, verbs need parameters.  Simply call
`requireStepParameters` in `doValidateStep` to check for this:

**CookStepHandler**
```java
@Override
protected void doValidateStep(IValidationContext<IScenarioStep> context) {
 requireStepParameters(context, "The 'cook' verb requires parameters!");
}
```

You can also do additional validation.  For example, maybe you don't want cooked sushi:

**CookStepHandler**
```java
@Override
protected void doValidateStep(IValidationContext<IScenarioStep> context) {
 requireStepParameters(context, "The 'cook' verb requires parameters!");
 
 IScenarioStep step = context.getObject();
 if(step.getParameters().contains("sushi")) {
  context.declare(Severity.WARNING, "Are you sure you want to cook your sushi?", step).getParameters();
 }
}
```

Like validators, step handlers are managed by [Guice](https://github.com/google/guice/wiki/GettingStarted) and can get
dependencies injected via `@Inject`.

### Packaging the Plugin
The plugin is declared in a `Module` the same was as a validator.  However, unlike validators, step handlers are usually
bound twice because they are both `IScenarioStepHandlers` and `ISystemDescriptorValidators:`

**CookStepHandlerModule**
```java
public class CookStepHandlerModule extends AbstractModule {
  @Override
  protected void configure() {
    // Most of the time, plugins are singletons.
    bind(CookStepHandler.class).in(Singleton.class);
 
    // Bind CookStepHandler as a handler.
    Multibinder<IScenarioStepHandler> handlers = Multibinder.newSetBinder(
      binder(),
      IScenarioStepHandler.class);
    handlers.addBinding().to(CookStepHandler.class);
 
    // Bind CookStepHandler as a validator.
    Multibinder<ISystemDescriptorValidator> validators = Multibinder.newSetBinder(
      binder(),
      ISystemDescriptorValidator.class);
    validators.addBinding().to(CookStepHandler.class);
  }
}
```

Make sure an entry in `src/main/resources/META-INF/services/com.google.inject.Module` for the module class is added and
the package that contains the module is exported.  See the instructors for validators for more information.

Note you can create a plugin that contains both validators and step handlers.  Simply declare them in the module.

### Deploying the Plugin
The plugins for step handlers and deployed the same way as validation plugins.  To deploy the plugin with the JellyFish
CLI, simply place the JAR on the classpath of the CLI application.  The plugin should be picked up automatically.

It's possible to quickly test the plugin with Eclipse by following the directions at **Testing the Plugin with Eclipse**  To
deploy the plugin in Eclipse, the plugin must be included in a p2 Update Site.  Follow the instructions at **Building an
Update Site for Eclipse** to build the update site.

# Testing the Plugin with Eclipse
A plugin under development can be tested within Eclipse without having to run the installer from the p2 update site.
Note the plugin(s) that should be tested should already be complied and built.  They should be located inside some
directory.  Be sure that the plugins to test and their dependencies are located in this directory.  In this example,
we'll assume the directory is `C:\projects\jellyfish\my-example-plugin\build\distribution`.  Follow these directions:
1. Clone the `jellyfish` Git repository at [https://github.ms.northgrum.com/CEACIDE/jellyfish](https://github.ms.northgrum.com/CEACIDE/jellyfish).
1. Run the Gradle build with `../gradlew build` from the `jellyfish-systemdescriptor-dsl` directory.
1. Start a development version of Eclipse and import the projects from `jellyfish-systemdescriptor-dsl` into the
   workspace.  The projects should be valid.  Note Eclipse should have the XText DSL support plugins.  For convenience,
   versions that includes support is located at [https://nexusrepomgr.ms.northgrum.com/service/rest/repository/browse/raw-ng-repo/ceacide/](https://nexusrepomgr.ms.northgrum.com/service/rest/repository/browse/raw-ng-repo/ceacide/).
1. Inside Eclipse, click the upside down arrow next to the green "run" icon and click Run Configurations.
    1. Create a new Eclipse Application configuration (name it anything you want).
    1. Set the workspace location to some directory that you can use to test with.  This workspace will contain a JellyFish project with SystemDescriptor (.sd) files.  You can create a new project in this workspace after starting Eclipse or use an existing workspace.
    1. Make sure Run a product (org.eclipse.platform.ide) is selected
    1. Click the arguments tab and add the following to the VM arguments section: `-Dcom.ngc.seaside.jellyfish.extraBundles=C:\projects\jellyfish\my-example-plugin\build\distribution`
    1. Click the plugins tab and make sure the plugins in the current work space are included.
    1. Click the Configuration tab and make sure Clear the configuration area before launching is checked.
1. Click Run.

Eclipse should start and it should have both the DSL and any extra plugins installed.  Create a new JellyFish project in
the workspace (or use an existing project) to test the plugins.

# Building an Update Site for Eclipse
In order to install the plugins inside Eclipse, it is necessary to build a p2 update site.  We'll use gradle to build
the update site.  This avoids having to manually configure and build the update site in Eclipse every time.

## The Feature File
A set of custom JellyFish plugins requires a feature file to install in Eclipse.  The file lists all the plugins/bundles
needed to install the feature.  You can find an example feature file and gradle build that will generate a feature.jar
at
[https://github.ms.northgrum.com/CEACIDE/jellyfish/tree/master/jellyfish-packaging/com.ngc.seaside.systemdescriptor.feature](https://github.ms.northgrum.com/CEACIDE/jellyfish/tree/master/jellyfish-packaging/com.ngc.seaside.systemdescriptor.feature)
Some notes are below:
* OSGi (and Eclipse) require all versions be of the format `major.minor.patch.qualifier` where major, minor, and patch
  are not optional.  Gradle and Maven (and the rest of the world) use the format `major.minor.patch-qualifier` where
  patch and qualifier are optional.  This means the Gradle build has to do some translation to get the versions correct.
  You can use the `com.ngc.seaside.gradle.plugins.util.Versions` class in the seaside-gradle-plugins JAR to help with
  this. See the example gradle file.
* The plugin `ID` attribute of feature.xml file must mach the `Bundle-Name` header in the manifest of the bundle.

The result of the feature file project should be a JAR file that only includes the feature.xml file.

## The Update Site
The update site contains the feature files and all the plugins that need to be installed.  It must have a layout like
this:
* features - contains the feature JAR files
* plugins - contains all the plugins
* artifacts.jar - contains metadata about the plugins
* content.jar - contains metadata about the plugins.

Gradle can be configured to build the update site as part of the build.  See the example at
[https://github.ms.northgrum.com/CEACIDE/jellyfish/tree/master/jellyfish-packaging/com.ngc.seaside.systemdescriptor.updatesite](https://github.ms.northgrum.com/CEACIDE/jellyfish/tree/master/jellyfish-packaging/com.ngc.seaside.systemdescriptor.updatesite)

**Filesnames Manner**
```note-warning
When building an update site with Gradle, the filenames of the plugins must match what Eclipse is
expected.  In particular, the format of the filename of a JAR in the plugins directory MUST be of the format
<bundleName>_<bundleVersion>.jar.  Where bundleName matches the Bundle-Name header in the manifest and the plugin ID
attribute in the feature.xml file and bundleVersion is the OSGi formatted version that matches the Bundle-Version header
in the manifest and the plugin version attribute in the feature.xml file.
```