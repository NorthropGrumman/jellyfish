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
scenarios are made up of _steps_.  Each step uses a verb to convey specific meaning.  It's possible to create custom
verbs along the same lines as custom validators.  Custom verbs are useful when a team wants to convey specific meaning
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
Verbs are created in almost the same manner as when creating a custom validator.  Configure the dependencies for the
containing project the same way:

**build.gradle**
```groovy
group = 'com.mystuff'
version = '1.0.0-SNAPSHOT'

dependencies {
   api "com.ngc.seaside:systemdescriptor.model.api:$jellyfishVersion"
   api "com.ngc.seaside:systemdescriptor.service.api:$jellyfishVersion"
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
the register call. The `willEmail` keyword can be now be used in a Then step. Likewise the `emailing` and `emailed`
keywords can be used in When and Given steps.

Step handlers that extend `AbstractStepHandler` are also validators. Step handlers must validate that the verbs are used
correctly and the step is valid. Extending classes accomplish this validation by implementing the `doValidateStep` method.
This uses the same validation API as discussed in the previous chapter. Most of the time, verbs need parameters. Simply
call `requireStepParameters` in `doValidateStep` to check for this:

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
The verb is declared in a `Module` the same as validators. However, unlike validators, step handlers are usually
bound twice because they are both `IScenarioStepHandlers` and `ISystemDescriptorValidators`:

**EmailStepHandlerModule.java**
```java
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

As before, be sure to create a new file in `src/main/resources/META-INF/services/` named `com.google.inject.Module`.  Its
contents should contain the fully-qualified class name of the module:

**com.google.inject.Module**
```plaintext
com.mystuff.modeling.validation.module.EmailStepHandlerModule
```

**Creating both validators and verbs**
```note-info
It's possible to package both validators and verbs in a single JAR.  Simply register all verbs and scenarios with
the JAR's Guice module.  In this case, it will only be necessary to list a single JAR as a dependency when configuring
the build script of an SD project.
```

# Using the New Verb
Verbs are deployed the same way as validators.  Simply list the containing JAR as a Gradle `buildscript` dependency,
copy the JAR to the `JELLYFISH_USER_HOME` `plugins/` directory, or include the JAR in an Eclipse update site.

## Using the New Verb within Eclipse
The Eclipse tooling will automatically declare an error if a scenario verb is used that can't be found.  As a result,
custom verbs need to be installed into Eclipse.  This can be done in one of two ways:

1. Create an Eclipse update site that includes the JAR with the verbs.  Users can create a single update site that
   installs both Jellyfish and any custom verbs or validators.  See `build.gradle` in the
   [Jellyfish update site](https://google.com)
   for an example project that creates an update site with Gradle.
2. Using Jellyfish user home: simply copy the JAR to `~/.jellyfish/plugins`
   (or `$JELLYFISH_USER_HOME/plugins`). This option is simpler than the update site option and allows
   users to quickly install custom extensions.  Use this option with care. The update site requires more up front
   setup but is usually a better option in the long term.
