# jellyfish-systemdescriptor-ext
This repository contains various extensions for JellyFish.

# Quick Start
The `ISystemDescriptorService` is the entry point for Java code that wants to interact with a system descriptor.  To use
it in a project add the following dependencies to your gradle build:
```
compile "com.ngc.seaside:systemdescriptor.model.api:1.0"
compile "com.ngc.seaside:systemdescriptor.service.api:1.0"
compile "com.ngc.seaside:systemdescriptor.service.impl.xtext:1.0"
```

## Getting an instance of the service
First, you need to configure you application to use the service.  If you app is simple and has no dependency injection
(or you are in a test), you can do something like this:
```
ISystemDescriptorService service = XTextSystemDescriptorServiceBuilder.forApplication().build();
```

However, if you app is using Guice for dependency injection, you can better integrate the service with you app by
following the example below.  To do this, make sure you have a dependency on Guice in your build:
```
compile "com.google.inject:guice:4.1.0"
```

In your code, do:
```
Collection<Module> appModules = new ArrayList<>();
// Add any application modules here, ie
appModules.add(new MyAppModule());
// Configure Guice and create the Injector.
Injector injector = XTextSystemDescriptorServiceBuilder.forIntegration(appModules::add)
  .build(() -> Guice.createInjector(appModules));
ISystemDescriptorService service = injector.getInstance(ISystemDescriptorService.class);
```

With this method, you can inject the service directly into your own component instead of resolving it directly from
the `Injector`:
```
public class MyApplication {
  private final ISystemDescriptorService service;
  
  @Inject
  public MyApplication(ISystemDescriptorService service) {
    this.service = service;
  }
}
```
This is the preferred way to manage the service.

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

# com.ngc.seaside.systemdescriptor.model.impl.xtext
This project contains an implementation of the
[model API](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-api) that uses the
[XText DSL](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-dsl).  

# com.ngc.seaside.systemdescriptor.service.impl.xtext
This project contains an implementation of the [service API](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-api).
