#jellyfish-systemdescriptor
This project contains API and implementation bundles related to working with System Descriptor projects.  See the
quick start guide below for information about how to read System Descriptor projects.

# API and Implementations
API and basic implementation bundles does not have dependencies on XText or the DSL. 

## com.ngc.seaside.systemdescriptor.model.api
Contains the model interfaces used by the System Descriptor language.

## com.ngc.seaside.systemdescriptor.service.api
Contains the API for the service based interfaces for interacting with the System Descriptor language.

## com.ngc.seaside.systemdescriptor.model.impl.basic
Contains a basic POJO based implementation of the model API.  This implementation is useful in tests and other areas
where you don't want to create a dependency on the XTest based DSL.

## com.ngc.seaside.systemdescriptor.model.impl.xtext
Contains the XText implementation of the model API.  This implementation uses the XText DSL.  In most cases, users
interact with the `ISystemDescriptorService` to obtain instances of model interfaces.

## com.ngc.seaside.systemdescriptor.service.impl.xtext
Contains an implementation of the `ISystemDescriptorService` that uses the XText implementation.  Users generally use
this service to interact with System Descriptor projects.

# Quick Start
The `ISystemDescriptorService` is the entry point for Java code that wants to interact with a system descriptor.  To use
it in a project add the following dependencies to your gradle build:
```
compile "com.ngc.seaside:systemdescriptor.model.api:1.0"
compile "com.ngc.seaside:systemdescriptor.service.api:1.0"
compile "com.ngc.seaside:systemdescriptor.service.impl.xtext:1.0"
compile "com.google.inject:guice:4.1.0"
```

## Getting an instance of the service
First, you need to configure you application to use the service.  The service uses Guice for dependency injection, so 
you will need to include the service's module when creating the injector.  Note that some implementation of BLoCS'
`com.ngc.blocs.service.log.api.ILogService` must be bound in order to start the service.

If you are using the service **outside** of Eclipse, you include the service's module as follows:
```
Collection<Module> appModules = new ArrayList<>();
// Add any application modules here, ie
appModules.add(new MyAppModule());
// Include the module for the service.
appModules.add(XTextSystemDescriptorServiceModule.forStandaloneUsage());
// Create the injector.
Injector injector = Guice.createInjector(appModules);
// Get the service.
ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
```

If you are using the service **within** Eclipse, you use the constructor of `XTextSystemDescriptorServiceModule`, ie:
```
appModules.add(new XTextSystemDescriptorServiceModule());
```

Uou can inject the service directly into your own component instead of resolving it directly from
the `Injector`, which is the preferred method of obtaining the service:
```
public class MyApplication {
  private final ISystemDescriptorService service;
  
  @Inject
  public MyApplication(ISystemDescriptorService service) {
    this.service = service;
  }
}
```

Note that this bundle also includes a `META-INF/services/com.google.inject.Module` service loader file, so the module
for this service can be created via the service loader mechanism.

## Using the service
You can parse a set of SD files with the following code:
```
Collection<Path> mySdFiles = Arrays.asList(
  Paths.get("my", "package", "HelloWorld.sd"),
  Paths.get("my", "package", "FooBar.sd")
);

IParsingResult result = service.parseFiles(mySdFiles);
// Check the result of the parsing for errors
if(!result.isSuccessful()) {
  System.err.println("Parsing failed: " + result.getIssues());
} else {
  // Interact with the result.  This will print the structure of the model to stdout.
  result.getSystemDescriptor().traverse(Traversals.SYSTEM_OUT_PRINTING_VISITOR);
}
```

You can also parse an SD project with:
```
IParsingResult result = service.parseProject(Paths.get("my", "project"));
// Do something with the result.
```

Note the project must use the `src/main/sd` project structure layout if using the second option.

See the test `XTextSystemDescriptorServiceIT.java` in the service implementation or the Javadoc of
`XTextSystemDescriptorServiceBuilder` project for more information.

# Creating Custom Validators
Custom validators implement the `ISystemDescriptorValidator` interface and usually extend
`AbstractSystemDescriptorValidator`:
```
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

Validators are declared in modules:
```
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

Modules have service loader files at `src/main/resources/META-INF/services/com.google.inject.Module`:
```
com.ngc.seaside.systemdescriptor.validation.impl.mymodel.module.ExampleModelValidatorModule
```

Always export the package that contains the module.

See http://10.207.42.42:8080/display/SEAS/JellyFish+-+How+to+extend+JellyFish#JellyFish-HowtoextendJellyFish-CreatingnewvalidationrulesfortheSystemDescriptor
for a complete tutorial.

# Creating Custom Scenario Step Handlers
Scenario step handlers declare keywords or verbs that can be used in scenario steps.  The `IScenarioStepHandler`
interface must be implemented and usually extend `AbstractStepHandler`.  Handlers that extend `AbstractStepHandler` are
also validators:
```
public class CookStepHandler extends AbstractStepHandler {
  private final static ScenarioStepVerb PAST = ScenarioStepVerb.pastTense("cooked");
  private final static ScenarioStepVerb PRESENT = ScenarioStepVerb.presentTense("cooking");
  private final static ScenarioStepVerb FUTURE = ScenarioStepVerb.futureTense("willCook");
 
  public CookStepHandler () {
   register(PAST, PRESENT, FUTURE);
  }
 
  @Override
  protected void doValidateStep(IValidationContext<IScenarioStep> context) {
   requireStepParameters(context, "The 'cook' verb requires parameters!");
   
   IScenarioStep step = context.getObject();
    if(step.getParameters().contains("sushi")) {
     context.declare(Severity.WARNING, "Are you sure you want to cook this?", step).getParameters();
    }
  }
}
```

Handlers are declared in `Module`s as both handlers and validators:
```
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

Modules have service loader files at `src/main/resources/META-INF/services/com.google.inject.Module`:
```
com.ngc.seaside.systemdescriptor.scenario.impl.cook.module.CookStepHandlerModule 
```

Always export the package that contains the module.

See
http://10.207.42.42:8080/display/SEAS/JellyFish+-+How+to+extend+JellyFish#JellyFish-HowtoextendJellyFish-CreatingnewScenarioStepKeywordsorVerbs
for a complete tutorial.
