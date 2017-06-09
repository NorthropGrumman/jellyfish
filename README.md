# jellyfish-systemdescriptor-ext
This repository contains various extensions for JellyFish.

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
TODO

# Creating Custom Scenario Step Handlers
TODO

# com.ngc.seaside.systemdescriptor.model.impl.xtext
This project contains an implementation of the
[model API](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-api) that uses the
[XText DSL](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-dsl).  

# com.ngc.seaside.systemdescriptor.service.impl.xtext
This project contains an implementation of the [service API](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-api).
