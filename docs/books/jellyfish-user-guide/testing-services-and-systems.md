---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 6 Testing Services and Systems
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
next-title: Ch. 7 Maintaining a Project
next-page: maintaining-a-project
prev-title: Ch. 5 The Structure of a Service Project
prev-page: the-structure-of-a-service-project
---
{% include base.html %}

As discussed in the previous chapter, Jellyfish will generate services and systems that contain sub-projects that can be
used to perform black box testing of services and systems.  This chapter will discuss how to run and implement these
tests.

# Testing Services
When we modeled the `SoftwareRequestService`, we created Cucumber feature files in the `src/test/gherkin/` directory of the
SD project.  The file `SoftwareRequestService.processRequest.feature` is the feature file that contains the Gherkin
scenarios for testing the `processRequest` scenario of the `SoftwareRequestService` service.

When the service project for the `SoftwareRequestService` is generated, the sub-project
`com.helpco.helpdesk.softwarerequestservice.tests` will automatically be configured to reference this feature file.  The
sub-project will reference any feature file whose name starts with `SoftwareRequestService`.  This
`com.helpco.helpdesk.softwarerequestservice.tests` sub-project will produce an executable application when it is built.
We can build the project with the command `./gradlew clean build`.

The `build` directory of the sub-project will be populated after the project is built.  This directory contains a
ZIP file that contains the testing application.  The ZIP file contains everything necessary to test the actual
`SoftwareRequestService`.  We can run the tests by running the start script inside the `bin/` directory.  Since the tests
are run as a separate process, we need to start the actual `SoftwareRequestService` service before starting the tests, or
they will fail.

The distribution ZIP also contains a `resources/` directory.  Within this directory is a directory structure that mirrors
the package of the `SoftwareRequestService`.  In this case, this structure is `com/helpco/helpdesk/`.  We can find the
feature files actually used by the service in this location.  Note that only the feature files for the service-under-test
are included.  Feature files for other services or systems are omitted.

## Implementing Tests
By default, all tests are not implemented.  Product teams must implement these tests by implementing
[Cucumber step definitions](https://docs.cucumber.io/guides/10-minute-tutorial/).  These definitions turn the phrases
in feature files into executable code.  Step definitions can be stored in the package
`com.helpco.helpdesk.srs.tests.steps` within the tests sub-project.

**Step definitions and multiple classes**
```note-info
Step definitions are really just methods with @Given, @When, @Then, etc annotations.  All step definitions do not have
to be implemented in the same class.  Steps can be broken into multiple classes for ease of development.
```

# Testing Systems
Systems are tested the same way.  Our `SoftwareStore` system also contains a feature file named
`SoftwareStore.handleInstallationRequest.feature`.  Unlike tests for individual services, tests for systems require all
services of a system to be running.

We can run the tests for our `SoftwareStore` system by running the start script generated in the
`com.helpco.helpdesk.softwarestore.tests` sub-project of the system project.  In many cases, we run the start script
for the entire system to easily start all the services required for the system level tests.

Step definitions for system tests are implemented in the exact same way as service tests.
