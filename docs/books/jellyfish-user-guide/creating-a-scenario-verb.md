---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 9 Creating a Scenario Verb
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
next-title: Ch. 10 Creating a Custom Command
next-page: creating-a-custom-command
prev-title: Ch. 8 Creating a Custom Model Validator 
prev-page: creating-a-custom-model-validator
---
{% include base.html %}

The SD language uses scenarios to declare behavior.  As discussed in
[Modeling with the System Descriptor]({{ safebase }}/books/modeling-with-the-system-descriptor/modeling-a-simple-component.html),
scenarios are made up of _steps_.  Each steps uses a verb to convey specific meaning.  It's possible to create custm 
verbs along the same lies as custom validators.  Custom verbs are useful when a team wants to convey specific meaning
with a scenario or step.

For this example, we'll create a new verb called `willEmail` which can be used in a `when` step in a scenario like this:

**Example willEmail scenario**
```
scenario notifyRequestorOfStatus {
  when receiving installationStatus
  then willEmail emailMessage
}
```

# Creating the Verb
Verbs are created in almost the same as creating a validator.  Configure the dependencies for the containing project
the same way:

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

New scenario verbs implement the `IScenarioStepHandler` interface.   Most of the time, the abstract class
`AbstractStepHandler` can be extended.

**EmailStepHandler.java**
```java
public class EmailStepHandler extends AbstractStepHandler {
  private final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("emailed");
  private final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("emailing");
  private final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willEmail");
 
  public EmailStepHandler() {
    register(PAST, PRESENT, FUTURE);
  }
 
  @Override
  protected void doValidateStep(IValidationContext<IScenarioStep> context) {
    // No extra validation needed right now.
  }
}
```

First, the 3 tenses of the verb are declared as `ScenarioStepVerb` objects. They are registered in the constructor with
the register call. The "willEmail" keyword can be now be used in a then step. Likewise, emailing, and emailed can be
used in when and given steps.

Step handlers that extend `AbstractStepHandler` are also validators. Step handlers must validate that the verbs are used
correctly and the step is valid. Extending classes do this validation by implementing `doValidateStep`. This uses the
same validation API as discussed in the previous chapter. Most of the time, verbs need parameters. Simply call
`requireStepParameters` in `doValidateStep` to check for this:

**EmailStepHandler.java**
```java
@Override
protected void doValidateStep(IValidationContext<IScenarioStep> context) {
  requireStepParameters(context, "The 'email' verb requires parameters!");
}
```

You can also do additional validation. For example, maybe you don’t want to email any data types whose name ends in 
"Spam":

**EmailStepHandler.java**
```java
@Override
protected void doValidateStep(IValidationContext<IScenarioStep> context) {
 requireStepParameters(context, "The 'email' verb requires parameters!");
 
 IScenarioStep step = context.getObject();
 String dataTypeName = step.getParameters().get(0);
 if (dataTypeName.endsWith("Spam")) {
  context.declare(Severity.ERROR, "Spam can't be emailed!", step).getParameters();
 }
}
```

Like validators, step handlers are managed by Guice and can get dependencies injected via `@Inject`.

## Packaging the Verb
The verb is declared in a `Module` the same was as a validator. However, unlike validators, step handlers are usually
bound twice because they are both `IScenarioStepHandlers` and `ISystemDescriptorValidators`:

**EmailStepHandlerModule.java**
```
public class EmailStepHandlerModule extends AbstractModule {
  @Override
  protected void configure() {
    // Most of the time, verbs and validators are singletons.
    bind(EmailStepHandler.class).in(Singleton.class);
 
    // Bind EmailStepHandler as a handler.
    Multibinder<IScenarioStepHandler> handlers = Multibinder.newSetBinder(
      binder(),
      IScenarioStepHandler.class);
    handlers.addBinding().to(EmailStepHandler.class);
 
    // Bind EmailStepHandler as a validator.
    Multibinder<ISystemDescriptorValidator> validators = Multibinder.newSetBinder(
      binder(),
      ISystemDescriptorValidator.class);
    validators.addBinding().to(EmailStepHandler.class);
  }
}
```

As before, be sure to create a new file in `src/main/resources/META-INF/services` named `com.google.inject.Module`.  Its
contents should contain the fully qualified class name of the module:

**com.google.inject.IModule**
```plaintext
com.mystuff.modeling.validation.module.EmailStepHandlerModule
```

**Creating both validators and verbs**
```note-info
It's possible to package both validators and verbs in a single JAR.  Simply register all verbs and scenarios with
the JAR's Guice module.  Now it is only neccessary to list a single JAR as a dependency when configuring the build
script of an SD project.
```

# Using the New Verb
Verbs are deployed the same way as validators.  Simply list the containing JAR as a Gradle buildscript dependency,
copy the JAR to the Jellyfish `lib` directory, or include the JAR in an Eclipse update site.

## Using the New Verb with Eclipse
The Eclipse tooling will automatically declare an error if a scenario verb is used that can't be found.  As a result,
custom verbs needs to be installed into Eclipse.  This can be done in one of two ways:

1. Create an Eclipse update site that includes the JAR with the verbs.  Users can create a single update site that
   installs both Jellyfish and any custom verbs or validators.  See the `build.gradle` the
   [Jellyfish update site](https://github.ms.northgrum.com/CEACIDE/jellyfish/tree/master/jellyfish-packaging/com.ngc.seaside.systemdescriptor.updatesite)
   for an example project that creates an update site with Gradle.
2. Using the `com.ngc.seaside.jellyfish.extraBundles` system property with Eclipse.  This option is simpler than the
   update site option and allows users to quickly install custom extensions.  Use this option with care.  The update 
   site requires more up front setup but is usually a better option in the long term.

### Using the com.ngc.seaside.jellyfish.extraBundles Property
This option requires the JAR containing the scenario verbs to be built as described above.  Copy the JAR to some 
location on the local filesystem such as `/opt/jellyfish/extensions`.  Next, edit the `eclipse.ini` file of the Eclipse
instance that has Jellyfish installed.  Add the property below under the first occurance of the
`--add-modules=ALL-SYSTEM` line:

**Deploying extra plugins by updating eclipse.ini**
```plaintext
-Dcom.ngc.seaside.jellyfish.extraBundles=<filesystem_location>
```

Replace `<filesystem_location>` with the path to the directory that contains the JAR with the custom verbs.  A complete
example is given below.  This example assumes the JAR is at `/opt/jellyfish/extensions`.

**eclipse.ini**
```plaintext
-startup
plugins/org.eclipse.equinox.launcher_1.5.0.v20180512-1130.jar
--launcher.library
plugins/org.eclipse.equinox.launcher.gtk.linux.x86_64_1.1.700.v20180518-1200
-product
org.eclipse.epp.package.dsl.product
-showsplash
org.eclipse.epp.package.common
--launcher.defaultAction
openFile
--launcher.defaultAction
openFile
--launcher.appendVmargs
-vmargs
-Dosgi.requiredJavaVersion=1.8
-Dosgi.instance.area.default=@user.home/eclipse-workspace
-XX:+UseG1GC
-XX:+UseStringDeduplication
--add-modules=ALL-SYSTEM
-Dcom.ngc.seaside.jellyfish.extraBundles=/opt/jellyfish/extensions
-Dosgi.requiredJavaVersion=1.8
-Dosgi.dataAreaRequiresExplicitInit=true
-Xms256m
-Xmx2024m
--add-modules=ALL-SYSTEM
```
