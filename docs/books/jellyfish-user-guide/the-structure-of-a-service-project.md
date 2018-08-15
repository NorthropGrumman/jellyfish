---
restrictions: UNCLASSIFIED Copyright (C) 2018, Northrop Grumman Systems Corporation
title: Ch. 5 The Structure of a Service Project
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
next-title: Ch. 6 Testing Services and Systems
next-page: testing-services-and-systems
prev-title: Ch. 4 Running Jellyfish
prev-page: running-jellyfish
---
{% include base.html %}

In the previous chapter, we generated four service projects and one system project.  These two categories of projects
have slightly different project structures.  We'll explore each category below.

# Structure of Service Project
We'll start by exploring the project for the `SoftwareRequestService`.  This project can be found in the directory named
`com.helpco.helpdesk.softwarerequestservice`.  If we look inside this directory, we'll see the following structure:

**Structure of a service project**
```plaintext
- com.helpco.helpdesk.softwarerequestservice.distribution/
- com.helpco.helpdesk.softwarerequestservice.domain/
- com.helpco.helpdesk.softwarerequestservice.impl/
- com.helpco.helpdesk.softwarerequestservice.tests/
- generated-projects/
- gradle/
- build.gradle
- gradlew
- gradlew.bat
- README.md
- settings.gradle
```

We'll explore the contents of each of these files and directories below.

The `README.md` file is used to describe the contents of the project.  By default, the contents of this file is empty.
Users are encorguaged to update this file to contain a simple description of the project and instructions about how to
build it.

## Gradle Configuration
Generated services projects build with Gradle.  The `gradle` directory contains the neccessary files to run the Gradle
wrapper.  As discussions in [Ch. 1]({{ safebase }}/books/jellyfish-user-guide/introduction-to-jellyfish.html), the 
wrapper makes it possible to automatically download and install Gradle if a user has never ran Gradle before.

The entire contents of `com.helpco.helpdesk.softwarerequestservice` forms a single _root project_.  Each of the
directories that start with "com.helpco.helpdesk" as well as the directories within `generated-projects` contain 
_subprojects_.  The entire root project can be built by running the following command from the directory that contains
the root project:

**Building a service project**
```
$> ./gradlew clean build -x test
```
Note that a freshly generated service project has failing unit tests (since the service has not been implemented yet).
Therefore, we need to use `-x test` to tell Gradle to skip execution of the unit tests and complete the build.  When we
run the build for the root project, Gradle builds each sub-project.

We can also build sub-projects directly.  For example, we could build the sub-project
`com.helpco.helpdesk.softwarerequestservice.tests` by moving to that directory and running the command
`./gradlew clean build`.  This will run build only that particular sub-project.

The top level `build.gradle` and `settings.gradle` files contains the build configuration for the entire project.  The
`settings.gradle` file declares which sub-projects make up the root project.  Only a single `settings.gradle` file is 
declared for an entire project.  The top level `build.gradle` file contains build logic used by the entire system.  It
also contains build logic applied to each sub-project and defines the version number for commonly used dependencies.
Note that each sub-project also contains its own `build.gradle` file.  These files contain build logic that is specific
to each sub-project.  See the
[Gradle documentation](https://docs.gradle.org/current/userguide/multi_project_builds.html) for more information about
Gradle and multi-project builds.

## Stubbed Sub-projects
The following sub-project where _stubbed_ by Jellyfish:

* `com.helpco.helpdesk.softwarerequestservice.distribution`
* `com.helpco.helpdesk.softwarerequestservice.domain`
* `com.helpco.helpdesk.softwarerequestservice.impl`
* `com.helpco.helpdesk.softwarerequestservice.tests`

These projects are considered stubs because Jellyfish initally generates the structure of these projects as part of the 
`create-java-service-project` command.  However, these projects are edited and maintained directly by the product team.
Jellyfish will never again attempt to generate or modify any of these sub-projects.

Each of the sub-projects serves as specific purpose:
* `com.helpco.helpdesk.softwarerequestservice.impl`: This sub-project contains the actual implementation of the of the
  `SoftwareRequestService`.  This is usually where a product team spends their bulk of their time.  This sub-project
  contains a class that is ready to be implemented by the team.  Teams are free to implement the service using any
  approach the want.
* `com.helpco.helpdesk.softwarerequestservice.distribution`: This sub-project does not contain executable code.
  Instead, it contains the runtime resources needed to run the actual service.  This might include runtime configuration
  files, etc. This project also contains scripts that can be used to start the service.  When `./gradlew build` is ran
  for this sub-project, a ZIP file will be created that is called a _distribution_ of the project.  This ZIP file
  contains everything neccessary to run the service.  This includes JAR files, start scripts, runtime configuration,
  etc.
* `com.helpco.helpdesk.softwarerequestservice.tests`: This sub-project is used to perform black blox testing of the
  service with Cucumber.  It references the feature files included with the model.  This project builds a seperate,
  standalone application that runs a seperate process from the actual service.  More information about the testing is
  covered in the next chapter.
* `com.helpco.helpdesk.softwarerequestservice.domain`: This sub-project contains _domain models_.  Domain models are
  POJOs (plain old Java objects) that are used to model data that flows _within_ the service.  This sub-project is used
  for complex services that may want to have layout information using different data structures.  Teams are free to
  change these data structure to layout the data in a way that is convenient for them.  Domain objects are private to a 
  service and are not explosed to other services.  **Not all services will use this sub-project**.  Simple services, 
  such as our `SoftwareRequestService`, can completely ignore this project.

## Generated Sub-projects
More sub-projects can be found under the `generated-projects` directory.  These sub-projects are fully generated by
Jellyfish and **should not be modified**.  

* `com.helpco.helpdesk.softwarerequestservice.base`: Contains the interface, API, and base classes for the service 
  itself.  Teams may extend the classes in this sub-project but that is not required.
* `com.helpco.helpdesk.softwarerequestservice.config`: Contains transport configuration and other configuration used
  to configure the service.
* `com.helpco.helpdesk.softwarerequestservice.connector`: The component that performs serialization and interacts with
  the transport layer.
* `com.helpco.helpdesk.softwarerequestservice.events`: Contains the inputs and the service.  These types are identical
  to the contents of the messages project but are serialization agnostic.  These types are used throughout the service.
* `com.helpco.helpdesk.softwarerequestservice.messages`: Contains the messages that the services receives or publishes.
  These messages are specific to serialization techniques and expose details related to that serialization.
* `com.helpco.helpdesk.softwarerequestservice.pubsubbridge`: Contains logic neccessary to allow the service to swap
  pub/sub for request/response or vice versa.
* `com.helpco.helpdesk.softwarerequestservice.testsconfig`: Contains the transport configuration neccessary to run the
  tests for the service.  This configuration is only used by the tests and not by the rest of the service.

## Running the Service
We can run the service by first performing a build of the root project.  Run `./gradlew clean build` from the root 
project directory to build the entire project.  Use `-x test` to skip unit tests if neccessary.  Once the build
finishes, the `com.helpco.helpdesk.softwarerequestservice.distribution` sub-project will contain a `build/distribution`
directory.  This directory contains a ZIP and another directory that contains the contents of the ZIP.  This ZIP 
contains everything needed to run the application.  Run the start script inside the `bin` directory to start the 
service.

## The Other Services
The remaining services have a very similar project structure.  Each project will vary slightly based on each model but
they are built and executed the same way.

# Structure of a System Project
The final project is the `com.helpco.helpdesk.softwarestore` project.  This project represents the entire 
`SoftwareStore` system.  Note this project contains no code that is executed as part of the system.  The system is 
fully realized by running the individual services.  The directory structure of a system looks like this:

**Structure of a system project**
```plaintext
- com.helpco.helpdesk.softwarestore.distribution/
- com.helpco.helpdesk.softwarestore.tests/
- generated-projects/
- gradle/
- build.gradle
- gradlew
- gradlew.bat
- README.md
- settings.gradle
```

Like services, this project has a README and various Gradle files.

## Gradle Configuration
The Gradle configuration for the system mirrors the configuration for services.  It too consists of a root project 
which contains sub-projects.

The system project can be built by running the command `./gradle clean build` from the directory of the root project.
Note that system projects require some extra configuration to get them up and running out of the both.  The
`build.gradle` of the root project must be updated before it is possible to build a distribution of the entire system.
The generated file contains two sections that require updates.  The first is the section that declares the versions
of the service projects to use.  This can be found in the `ext` section of the script.

**Configuring the versions of the services in the system's build.gradle**
```groovy
ext {
   logger.error "Not implemented: you need specify the versions for the distributions of this system's parts"
   // TODO: Add versions for these distributions
   // softwareRequestServiceDistributionVersion = '1.2.3'
   // licenseServiceDistributionVersion = '1.2.3'
   // pcManagementServiceDistributionVersion = '1.2.3'
   // emailServiceDistributionVersion = '1.2.3'
}
```

In our example, all of the services start at version 1.0.0-SNAPSHOT, so we can update the configuration as follows:

**Configuring the versions of the services in the system's build.gradle**
```groovy
ext {
   softwareRequestServiceDistributionVersion = '1.0.0-SNAPSHOT'
   licenseServiceDistributionVersion = '1.0.0-SNAPSHOT'
   pcManagementServiceDistributionVersion = '1.0.0-SNAPSHOT'
   emailServiceDistributionVersion = '1.0.0-SNAPSHOT'
}
```

Now that we have declared versions, we can update the `systemDescriptor` section of the `build.gradle`:

**Configure the services in the system's build.gradle**
```groovy
systemDescriptor {
   project = 'com.ngc.seaside:helpdesk.descriptor:1.0.0-SNAPSHOT'
   model = 'com.helpco.helpdesk.SoftwareStore'
   deploymentModel = 'com.helpco.helpdesk.LocalSoftwareStore'

   part {
      model = 'com.helpco.helpdesk.SoftwareRequestService'
      distribution = "com.helpco.helpdesk:srs.distribution:$softwareRequestServiceDistributionVersion@zip"
   }
   part {
      model = 'com.helpco.helpdesk.LicenseService'
      distribution = "com.helpco.helpdesk:ls.distribution:$licenseServiceDistributionVersion@zip"
   }
   part {
      model = 'com.helpco.helpdesk.PcManagementService'
      distribution = "com.helpco.helpdesk:pcms.distribution:$pcManagementServiceDistributionVersion@zip"
   }
   part {
      model = 'com.helpco.helpdesk.EmailService'
      distribution = "com.helpco.helpdesk:es.distribution:$emailServiceDistributionVersion@zip"
   }
}
```

## Stubbed Sub-projects
A system only contains two sub-projects that are stubs:

* `com.helpco.helpdesk.softwarestore.distribution`: This sub-project contains a single distribution that will contain
  all of the services that make up the system.
* `com.helpco.helpdesk.softwarestore.tests`: This sub-project contains a black box test for testing the entire system.
  This test will require all of the services in the system to be running.  This tests is implemented in a similar was as
  the tests for services (IE, they both use Cucumber).  The test runs as a seperate application and this sub-project
  will produce a runnable application for the tests.

## Generated Sub-projects
Like services, the `generated-projects` directory contains sub-projects that are entirely generated by Jellyfish and
should not be edited.

* `com.helpco.helpdesk.softwarestore.base`: This contains transport topics used by the tests.
* `com.helpco.helpdesk.softwarestore.messages`: Like services, this sub-project contains the messages for the entire
  system.  These are used by the tests.
* `com.helpco.helpdesk.softwarestore.testsconfig`: Contains the transport configuration for the tests.

## Running the System
The `com.helpco.helpdesk.softwarestore.distribution` sub-project is provided for convenience to make it easy to run the
entire system.  Before building the system's distribution, **it is neccessary to build and "install" the service 
projects** that make up the system.  This means running the command `./gradlew build install` for each of the services
described above.  Once complete, the command `./gradle clean build` can be run for the root project of the system.

The build will produce a ZIP that can be found in the `build` directory of the 
`com.helpco.helpdesk.softwarestore.distribution` sub-project.  This ZIP contains all of the services and a single 
start script that will start all four services as different processes.  This is primarly used for testing on a single 
host and such a script might not be useful for services that deploy on seperate hosts.

# Conclusion
This chapter provided a detailed break-down of the services that Jellyfish generates.  The next chapter will cover
testing both services and systems.