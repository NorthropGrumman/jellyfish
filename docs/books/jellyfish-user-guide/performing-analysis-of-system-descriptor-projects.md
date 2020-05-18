---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 11 Performing Analysis of System Descriptor Projects
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
prev-title: Ch. 10 Creating a Custom Command
prev-page: creating-a-custom-command
---
{% include base.html %}

Various types of analysis can be performed on System Descriptor projects.  These analysis are implemented as 
Jellyfish command.s  Analysis can be run in three ways:
1. Using the Jellyfish CLI.  This option allows the user to run an analysis directly with Jellyfish.
1. Using Gradle.  This option allows the user to run analysis as part of a Gradle build on an SD project.
1. Using Sonarqube.  This option allows the results of analysis to be posted to Sonarqube.  This option also requires
   Gradle.

Jellyfish includes various default report generators and analysis.  See 
https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-cli-analysis-commands for a complete list of
items built directly into Jellyfish.

# Running Analysis with Jellyfish
This option allows a user to run an analysis directly with Jellyfish.  Use the `analyze` command to do this.  This 
command allows a user to specifcy which types of reports should be generated and which analysis should be performed.
The example below performs an input/output analysis and outputs a plain text report to the console:

**Running an analysis with the Jellyfish CLI**
```
jellyfish analyze \
 analyses=analyze-inputs-outputs \
 reports=console-report \
 gav=my.group:my.sd.project:1.0.0-SNAPSHOT
```

The `gav` parameter identifies the rpoeprty to run the analysis on.  The `inputDirectory` parameter can be instead of 
the GAV.  The `analyses` parameter defines which analysis to run.  The `reports` parameter identifies which reports to
generate.  Mutliple analysis and reports can be performed with a single invocation of Jellyfish as shown below:

**Running multiple analysis**
```
jellyfish analyze \
 analyses=analyze-inputs-outputs,analyze-features \
 reports=html-report,console-report \
 reportName=analysis-results \
 outputDirectory=output/results
 gav=my.group:my.sd.project:1.0.0-SNAPSHOT
```

This example runs both the `analyze-inputs-outputs` and `analyze-features` analysis.  It also generates an HTML report.
The HTML report requries the additional arguments of `reportName` and `outputDirectory`.

# Running Analysis with Gradle
Analysis can also be performed when running a Gradle build for an SD project.  This can be done if the Gradle project
has the `com.ngc.seaside.jellyfish.system-descriptor` plugin applied.  Configure the reports to generate and the 
analysis to perform as shown below:

**build.gradle**
```groovy
sdAnalysis {
    command 'analyze-inputs-outputs'
    command 'analyze-budgets'
    
    report 'html-report'
    arg 'reportName', 'example-report'
    arg 'outputDirectory', 'build/reports/analysis'
}
```

Again, multiple commands and reports can be configured.  If no reporter is configured, the console reporter is used.

# Running Analysis with Sonarqube
The results of these analysis can also be posted to Sonarqube.  This also requries Gradle.  A SD project that has
the `com.ngc.seaside.jellyfish.system-descriptor` plugin can run `gradle sonarqube` to post the results of analysis to
a Sonarqube server.  As shown above, the analysis commands are configured with the `sdAnalysis` extension.

Note the Sonarqbue server must have the Jellyfish Sonarqube plugin installed for this to work correctly.  Download the
plugin JAR for the latest Jellyfish release at https://github.ms.northgrum.com/CEACIDE/jellyfish/releases.  Copy the
JAR to the directory `$SONARQUBE_HOME/extensitions/plugins`.  See
https://docs.sonarqube.org/display/SONAR/Installing+a+Plugin for more information.

Finally, a user must configure the URL of the Sonarqube server to post the results to.  This URL can be configured in 
the user's `gradle.properties` file like this:

**gradle.properties**
```
systemProp.sonar.host.url=<SERVER_URL>
```

See https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle for more information.

# Creating a New Analysis Command
Users can create their own analysis command like any other Jellyfish command.  See Ch. 10 for more information about 
creating a custom Jellyfish command.

Analysis commands typically extend the class `AbstractJellyfishAnalysisCommand`.  Users can then override
`analyzeModel`, `analyzeData`, `analyzeEnumeration`, or `analyzeEntireProject` to perform the actual work of analysis.

## Reporting Findings
Findings can be reported with the `reportFinding` method.  This requries developers to create "finding types" for the 
different categories of findings the analysis can report.  A common pattern is shown below:

**MyCustomFindingTypes.java**
```java
public enum MyCustomFindingTypes implements ISystemDescriptorFindingType {
   CONFUSING_NAME("confusingName", "docs/confusingName.md", Severity.WARNING),
   MISSING_DOCS("missingDocs", "docs/missingDocs.md", Severity.ERROR);

   private final String id;
   private final String description;
   private final Severity severity;

   MyCustomFindingTypes(String id,
                        String resource,
                        Severity severity) {
      this.id = id;
      this.description = AbstractJellyfishAnalysisCommand.getResource(resource, MyCustomFindingTypes.class);
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

Each finding has a unqiue ID, a description, and a severity.  Severity levels are info, warn, and error.  Info findings
are not reported to Sonarqube.  The description is included in the report with the a finding with the type is reported.
It is recommended to use Markdown to format the description.  In this example, we can place Markdown files in the 
locations `src/main/resources/docs/confusingName.md` and `src/main/resources/docs/missingDocs.md`.  These docs will be
loaded at runtime.  Below is an example description:

**src/main/resources/docs/missingDocs.md**
```
## Missing Documentation
Models should contain documentation in their **metadata**.  IE, ...
```

The finding types need to be registered with Guice.  This can be done in the module for the command.  The snippet below
shows an easy way to do this:

**Registering the finding types with Guice**
```java
// Register the finding types for this analysis.
Multibinder<ISystemDescriptorFindingType> typesBinder = Multibinder.newSetBinder(
            binder(),
            ISystemDescriptorFindingType.class);
for (ISystemDescriptorFindingType type : MyCustomFindingTypes.values()) {
  typesBinder.addBinding().toInstance(type);
}
```

Finally, findings can be created and reported like this:

**Reporting a finding**
```java
@Override
protected void analyzeModel(IModel model) {
    if (!model.getMetadata().getJson().containsKey("documentation")) {
      // Make this message an action message for fixing the issue. 
      String message = "Add 'documentation' metadata to this model.";
      ISourceLocation location = sourceLocatorService.getLocation(model, false);
      SystemDescriptorFinding<?> finding =
        MyCustomFindingTypes.MISSING_DOCS.createFinding(message, location, 1);
      reportFinding(finding);
    }
}
```

All findings must have an assoicated finding type.