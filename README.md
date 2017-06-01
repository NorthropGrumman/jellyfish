This repository contains various extensions for JellyFish.

# Quick Start
The `ISystemDescriptorService` is the entry point for Java code that wants to interact with a system descriptor.  To use it in a project add the following dependencies to your gradle build:

```
compile "com.ngc.seaside:systemdescriptor.model.api:1.0"
compile "com.ngc.seaside:systemdescriptor.service.api:1.0"
compile "com.ngc.seaside:systemdescriptor.service.impl.xtext:1.0"
```

You can parse a set of SD files with the following code:

```java
Collection<Path> mySdFiles = Arrays.asList(
  Paths.get("my", "package", "HelloWorld.sd"),
  Paths.get("my", "package", "FooBar.sd")
);

ISystemDescriptorService service = new XTextSystemDescriptorService();
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

```java

ISystemDescriptorService service = new XTextSystemDescriptorService();
IParsingResult result = service.parseProject(Paths.get("my", "project"));
// Do something with the result.
```

Note the porject must use the `src/main/sd` project structure layout if using the second option.

# com.ngc.seaside.systemdescriptor.model.impl.xtext
This project contains an implementation of the
[model API](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-api) that uses the
[XText DSL](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-dsl).  

# com.ngc.seaside.systemdescriptor.service.impl.xtext
This project contains an implementation of the [service API](https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-api).
