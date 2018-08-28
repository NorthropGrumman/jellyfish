---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 10 Creating a Custom Command
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
prev-title: Ch. 9 Creating a Scenario Verb
prev-page: creating-a-scenario-verb
---
{% include base.html %}

Jellyfish itself can be extended by adding new commands.  Commands are objects that perform some type of processing
on an SD project.  Examples of functionality that can be implemented in commands include:

* Generating documentation directly from a modeling project.
* Generating code from a model.
* Analyzing a model for anti-patterns.

# Creating a New Command
Commands are implemented like validators and scenario verbs.  A new command typically includes the following
dependencies in its `build.gradle` file:

**build.gradle**
```groovy
dependencies {
   implementation "com.ngc.seaside:jellyfish.api:$jellyfishVersion"
   implementation "com.ngc.seaside:jellyfish.service.api:$jellyfishVersion"
   implementation "com.ngc.seaside:jellyfish.utilities:$jellyfishVersion"
   implementation "com.google.inject:guice:$guiceVersion"
   implementation "com.google.inject.extensions:guice-multibindings:$jellyfishVersion"
}
```

Jellyfish commands implement either the `ICommand` or `IJellyfishCommand` interface.  Commands that implement the
`ICommand` interface are commands that _do not require a System Descriptor_ in order to be run; whereas, commands
that implement `IJellyfishCommand` do require a System Descriptor.  Most of the commands included with Jellyfish
implement the `IJellyfishCommand` interface.

## Types of Commands
Various abstract base classes are provided to make implementing commands easier.  Generic commands can extend the
class `AbstractJellyfishCommand`.  This includes commands that generate code, documentation, etc.

Commands may also perform various types of analyses and may report any findings.  These findings are consolidated into
a final report when `jellyfish analyze` is executed.  These types of commands typically extend
`AbstractJellyfishAnalysisCommand`.

## An Example Command
Below is an example of a simple command that is ready to be implemented:

**A skeleton of an AbstractJellyfishCommand**
```java
public class ExampleCommand extends AbstractJellyfishCommand {

   private static final String NAME = "example-command";

   public CreateJellyFishGradleProjectCommand() {
      super(NAME);
   }

   @Override
   protected IUsage createUsage() {
      // This is used when showing help to the user.
      return new DefaultUsage(
         "This is the description of what the command does and how to use it.",
         // These are required and optional parameters for the command.
         new DefaultParameter("foo").setDescription("the foo parameter is used to ...").setRequired(true),
         new DefaultParameter("bar").setDescription("the bar parameter is used to ...").setRequired(false)
      );
   }

   @Override
   protected void doRun() {
      // The command options provide the runtime arguments given to Jellyfish as well as the System Descriptor project
      // that Jellyfish is being run against.
      IJellyFishCommandOptions commandOptions = getOptions();
      // Do any logic here.
   }
}
```

The `NAME` of the command is what users will use to run the command.  For example, a user might run this command as follows:
`jellyfish example-command foo=...`.

`createUsage` is where the command describes itself.  The usage object includes a description of the command and the
parameters that the command supports.  Each parameter has a name, a description, and may be required or optional.  This
information is used when showing help to the user.

The `doRun` method is where the actual logic of the command resides.  The `getOptions` method
provided by the base class allows commands to obtain a reference to the System Descriptor project that Jellyfish
is being run against.  Users can explore the SD project like this:

**Exploring an SD project programmatically**
```java
IJellyFishCommandOptions commandOptions = getOptions();
ISystemDescriptor project = commandOptions.getSystemDescriptor();
// Get a particular model:
Optional<IModel> model = project.findModel("com.foo.MyModel");
// Explore all models in the project:
for (IPackage sdPackage : project.getPackages()) {
   for (IModel model : sdPackage.getModels()) {
      // Do something with the model.
      System.out.println("Found the model " + model.getFullyQualifiedName());
   }
}
```

Users can also use the `IJellyFishCommandOptions` to retrieve arguments provided by the user.  For example,

**Getting runtime arguments**
```java
IJellyFishCommandOptions commandOptions = getOptions();
String foo = commandOptions.getParameters().getParameter("foo").getStringValue();
```

Commonly used parameters can be found in the `CommonParameters` enumeration.  These can be referenced both when
creating usages and getting parameter values:

**Using CommonParameters**
```java
@Override
protected IUsage createUsage() {
   return new DefaultUsage(
      "This is the description of what the command does and how to use it.",
      // These are required parameters for the command.
      CommonParameters.MODEL.required(),
      new DefaultParameter("foo").setDescription("the foo parameter is used to ...").setRequired(true),
      new DefaultParameter("bar").setDescription("the bar parameter is used to ...").setRequired(false)
   );
}

@Override
protected void doRun() {
   IJellyFishCommandOptions commandOptions = getOptions();
   String modelName = commandOptions.getParameters().getParameter(CommonParameters.MODEL.getName()).getStringValue();
}
```

### Registering the Command
Like validators and scenario verbs, we need to configure a Guice module to register the command.  Below is an example
module:

**Creating a Guice module for the command**
```java
public class ExampleCommandModule extends AbstractModule {
   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(ExampleCommand.class);
   }
}
```

As before, be sure to create a new file in `src/main/resources/META-INF/services/` named `com.google.inject.Module`.  Its
contents should contain the fully qualified class name of the module:

**com.google.inject.IModule**
```plaintext
com.mystuff.modeling.validation.module.ExampleCommandModule
```

## An Example Analysis Command
An analysis command is implemented by extending `AbstractJellyfishAnalysisCommand`:

**Implementing an analysis command**
```java
public class ExampleAnalysisCommand extends AbstractJellyfishAnalysisCommand {

   public static final String NAME = "exmaple-analysis";

   public AnalyzeInputsOutputsCommand() {
      super(NAME);
   }

   @Override
   protected IUsage createUsage() {
      return new DefaultUsage("Checks the model has at least one output.", CommonParameters.MODEL);
   }

   @Override
   protected void analyzeModel(IModel model) {
      if (model.getOutputs().isEmpty()) {
         String message = "The model has no outputs.  Consider adding some outputs.";
         ISourceLocation location = sourceLocatorService.getLocation(model, false);
         SystemDescriptorFinding<?> finding = ExampleFindingTypes.NO_OUTPUTS.createFinding(message, location, 1);
         reportFinding(finding);
      }
   }
}
```

This analysis checks models to ensure they have at least one output field declared.  It does this by overriding the
method `analyzeModel`.  Analysis commands can also override `analyzeData` and `analyzeEnumeration` to review data types
and enumerations.

If an analysis finds a problem, it reports the finding using the method `reportFinding`.  This requires an instance of
`SystemDescriptorFinding`.  Command writers create findings and finding types like this:

**Declaring a new type of finding**
```java
public enum ExampleFindingTypes implements ISystemDescriptorFindingType {

   NO_OUTPUTS("no-outputs", "docs/no-inputs.md", Severity.WARNING);

   private final String id;
   private final String description;
   private final Severity severity;

   ExampleFindingTypes(String id,
                       String resource,
                       Severity severity) {
      this.id = id;
      this.description = AbstractJellyfishAnalysisCommand.getResource(resource, InputsOutputsFindingTypes.class);
      this.severity = severity;
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getDescription() {
      return description;
   }

   @Override
   public Severity getSeverity() {
      return severity;
   }
}
```

This declares the different _types_ of findings.  Findings have a unqiue ID, a description, and a severity level.  Note
that is example actually loads the description from a Markdown file.  Markdown will be converted to HTML when the
HTML report is generated.  In order for this to work, we'll need a file named `no-inputs.md` in the directory
`src/main/resources/docs/`.  This file might look like this:

**no-inputs.md**
```markdown
## Avoid components that have inputs but no outputs
Models of components that receive inputs but do not produce outputs can result in components that cannot be tested or
verified.  Minimally, a component should produce some type of output that indicates it has received some input.  This
allows the component to be tested as well as inspected.  This applies to both components that use pub/sub messaging an
well as components that use request/response to exchange messages.

See [Avoid components that have inputs but no outputs](http://www.some.more.info/stuff) for more information.
```

Developers create _specific instances_ of a type of finding like this:

**Creating an instance of a finding**
```java
ISourceLocation location = sourceLocatorService.getLocation(model, false);
SystemDescriptorFinding<?> finding = ExampleFindingTypes.NO_OUTPUTS.createFinding(message, location, 1);
```

The finding can then be reported.

## Registering the Analysis Command
Like any other type of command, analysis commands must be registered in a Guice module:

**ExampleAnalysisCommandModule.java**
```java
public class ExampleAnalysisCommandModule extends AbstractModule {
   @Override
   protected void configure() {
      Multibinder.newSetBinder(binder(), IJellyFishCommand.class)
            .addBinding()
            .to(ExampleAnalysisCommand.class);
   }
}
```

# Deploying and Using the New Command
Jellyfish commands are deployed like scenario verbs and validators.  Copy the JAR that contains the command
to the `lib/` directory of Jellyfish.  The example command can now be run like this:

**Running the example command**
```plaintext
$> jellyfish example-command gav=my.group:my.sd.project:1.0.0 model=my.Model foo=whatever
```

The `example-command` will be invoked on the SD project with the group of `my.group`, the artifact ID of
`my.sd.project`, and a version of 1.0.0.  The command will be invoked with the arguments `my.Model` and `whatever`.

Likewise, the analysis command can be invoked like this:

**Running the analysis command**
```plaintext
jellyfish analyze \
  analyses=examle-analysis \
  reports=html-report  \
  reportName=analysis-results \
  outputDirectory=output/results/ \
  gav=my.group:my.sd.project:1.0.0
```

Any findings reported by the command we be in the HTML report located at `output/results/analysis-results.html`.

# Conclusion
Teams can build a single JAR that contains all their custom validators, scenario verbs, and commands.  This JAR can
contain a single module that registers all the components.  This is useful because it allows teams to distribute a
single JAR that contains all of their customizations.
