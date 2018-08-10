---
title: Ch. 1 Introduction to Jellyfish
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
next-title: Ch. 2 A Simple Software Store System
next-page: a-simple-software-store-system
---
{% include base.html %}

Jellyfish is a set of tools that can be used with System Descriptor projects.  Jellyfish provides software developers
with a way to extend the System Descriptor language, programmatically inspect System Descriptor (SD) projects, and to 
generate various custom artifacts from a project.  The most notable feature that Jellyfish provides is automatic code
generation.

It should be noted that Jellyfish does not aim to completely generate a software application from a System Descriptor
model.  Instead, it is used to generate infrastructure, boilerplate, or other "unintresting" aspects of an application.
Application developers are still expected to implement the core business functionality of the app themselves.  Jellyfish
aims to generate unobtrusive code, so developers are free to implement their own business logic however they wish.

# The Command Line Interface
Most users will directly interact with Jellyfish using its command line interface (CLI).  Users run the command
`jellyfish` from a prompt and supply the Jellyfish action or command to execute along with any arguments needed to
perform that action.

The simplest command is the `validate` command.  This command can be used to check the syntax of an SD project.  It
can be run like this:

**jellyfish CLI example**
```
$> jellyfish validate inputDir=/home/me/my-sd-project
System Descriptor project is valid.
-- SUCCESS (00:00:03.173) --
```

The `inputDir` parameter is used to specific the location of the project on disk.

## Help
Users can always run `jellyfish help` to learn about the avaiable commands and parameters needed to run a particualr 
Jellyfish command.

**Viewing help**
```
$> jellyfish help
Usage: jellyfish command [option1=value1 ...]

Commands:


   analyze                                Run various types of analysis and
                                          generates reports.
   analyze-budget                         Analyzes the budgets of a model and
                                          its parts.
   analyze-inputs-outputs                 Checks that models which have
                                          inputs also have outputs.
   console-report                         Outputs the results of analysis to
                                          the console.  This command is
                                          rarely ran directly; instead it is
                                          run with the 'analyze' command.
...
```

Users can also see help for a specific command.  For example, this shows help for the `analyze` command:

**Viewing help for a particular command**
```
$> jellyfish help command=analyze
Run various types of analysis and generates reports.




required parameters:


   analyses                        Configures the analysis to execute.  The
                                   values are comma (,) separated.




optional parameters:


   reports                         Configures the reports to generated after
                                   performing analysis.  The values are comma
                                   (,) separated.

```

# Jellyfish Setup
Users can download the latest version of Jellyfish at
[https://github.ms.northgrum.com/CEACIDE/jellyfish/releases](https://github.ms.northgrum.com/CEACIDE/jellyfish/releases).

1.  Download the file named jellyfish-vX.X.X.zip listed under assets.  Replace _X.X.X_ with the latest version of 
Jellyfish.  
1. Extract the ZIP file to any location.
1. In order to be able run Jellyfish from any command prompt, it is neccessary to add the location where Jellyfish
was unzipped to the system's _PATH_.  This process is different for Windows and Linux users.
    1. **Windows**: Search for `env` and select **Edit Environment Variables for your account**.
        1. Select the `PATH` variable and select **Edit**.
        1. Add the following to the field: `;<JELLYFISH_DIRECTORY>\bin` where `<JELLYFISH_DIRECTORY>` is the directory
        where the Jellyfish ZIP was extracted.
        1. Click OK to save the changes.
    1. **Linux**: Edit the RC file of the user's perferred shell, for example `~/.bashrc`.  
        1. Add the line `export PATH=<JELLYFISH_DIRECTORY>/bin:$PATH` where `<JELLYFISH_DIRECTORY>` is the directory
        where the Jellyfish ZIP was extracted.
    1.  In either case, open a new command prompt and run `jellyfish help` to verify everything is working correctly.

# Additional Features
Jellyfish also provides additional features including
* The ability to create and run custom analysis of SD projects.  Teams can use this functionalty to generate custom
aritfacts directly from the model.  
* A Sonarqube plugin which integrates Jellyfish with Sonarqube.  This makes it possible to employ automated quality
accessments of SD projects in the same way that quality accessments of software/code projects are performed.  
In particular, teams can build custom analyses that detect undesiredable ariteactureal patterns in SD projects.  Teams 
can then configure the SD projects so that the results of these analysis are posted to Sonarqube automatically and 
teams can review ariteactureal trends of projects.
* Various Gradle plugins to make it possible to perform Jellyfish actions during a Gradle build.  This is useful for
both SD projects and software code projects that are generated from an SD project.

All of these features are convered throughout this book.