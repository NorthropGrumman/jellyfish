# Jellyfish
Jellyfish is a product suite for the System Descriptor (SD) which is an Architecture-as-Code construct modeled after the
DevOps Infrastructure-as-Code paradigm.  Jellyfish is made up of several components:

## jellyfish-systemdescriptor-dsl
The System Descriptor language is a simple text based language that describes the architecture of a system or
product.  The [jellyfish-systemdescriptor-dsl](https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-systemdescriptor-dsl)
project contains the language's grammar, parser, editor, and other components that deal with the DSL directly

## jellyfish-systemdescriptor
The [jellyfish-systemdescriptor](https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-systemdescriptor)
project contains the API and default implementations that interact with the 
System Descriptor language.  Developers use this project to build custom tooling that interacts with System Descriptor
projects.  Developers rarely use the DSL directly; they should use the APIs provided by this project.

## jellyfish-cli
The [jellyfish-cli](https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-cli) project contains the
API for creating various tooling built on top of the System Descriptor language. The API may be extended by developers
to add custom features without having to build tooling from scratch.

## jellyfish-cli-commands
The [jellyfish-cli-commands](https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-cli-commands)
project contains commands for generating software projects directly from System Descriptor models.  

## jellyfish-cli-analysis-commands
The [jellyfish-cli-analysis-commands](https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-cli-analysis-commands)
project contains commands for generating various reports derived from System Descriptor models.  These commands provide
a static analysis of architectural quality.

## jellyfish-packaging
The [jellyfish-packaging](https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-packaging) project is
used to package Jellyfish for different deployments.  This includes
* a command line interface
* a set of Gradle plugins
* an Eclipse update site for installing the language support into Eclipse
* a Sonarqube plugin for running Jellyfish analysis with Sonarqube

## jellyfish-systemdescriptor-lang
The [jellyfish-systemdescriptor-lang](https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-systemdescriptor-lang)
project contains the default models and data types that are built into the language.  By default, all System Descriptor
projects can reference elements in these projects. 

# Examples
The [jellyfish-examples](https://github.com/NorthropGrumman/jellyfish/tree/master/jellyfish-examples) project
contains various examples of both System Descriptor models and running the Jellyfish CLI.

# Downloads
You can download the latest releases at https://github.com/NorthropGrumman/jellyfish/releases.

# More Info
Learn more about the System Descriptor language and Jellyfish at https://northropgrumman.github.io/jellyfish/.
