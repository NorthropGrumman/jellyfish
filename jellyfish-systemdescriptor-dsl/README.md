# jellyfish-systemdescriptor-dsl
This is an Eclipse XText project which defines the System Descriptor language and generates the parser and Eclipse based
 tooling.  This project generates:
* the parser and code for creating an AST (abstract syntax tree) of System Descriptor files
* the validation logic and rules of the DSL
* content assists and Eclipse plugins for using Eclipse as a System Descriptor editor

# Installation and Setup with Eclipse
Installation requires Eclipse and the update site.  Download Eclipse from the links below.  You can download the update 
site from [Releases](https://github.ms.northgrum.com/CEACIDE/jellyfish/releases).  Unzip the update site to any location
you want.

See [Ch. 2 Installation and Setup](https://pages.github.ms.northgrum.com/CEACIDE/jellyfish/books/modeling-with-the-system-descriptor/installation-and-setup.html)
for help with installation and setup.

# Creating a New System Descriptor Project
[Ch. 2 Installation and Setup](https://pages.github.ms.northgrum.com/CEACIDE/jellyfish/books/modeling-with-the-system-descriptor/installation-and-setup.html)
also contains information about creating a new project.

# Building the Project
The project builds with Gradle.  Simply run the command below from the directory `jellyfish-systemdescriptor-dsl` within
the repository.  Since the Gradle wrapper is used, Gradle will download itself.
```
../gradlew clean build
```
This will build project and run the tests.  The update site can be creating by building the subproject
`com.ngc.seaside.systemdescriptor.updatesite` inside the
[jellyfish-packaging](https://github.ms.northgrum.com/CEACIDE/jellyfish/tree/master/jellyfish-packaging)
project.

# Importing the Project with Eclipse
The project can be imported into Eclipse as a Gradle project.
1. Start Eclipse.
1. Select File -> Import.
1. Select Gradle -> Existing Gradle Project and click Next.
1. Click Next again and make sure the root directory is set to the `jellyfish-systemdescriptor-dsl` directory of the repository.
1. Click Finish to complete the import.  
Sometimes, Eclipse doesn't play well with the Gradle wrapper.  If an error happends during the import, download Gradle 
yourself, and set the **Local installation directory** field in the last screen to the location where you have already
installed Gradle.  Click finish to try the import again.
