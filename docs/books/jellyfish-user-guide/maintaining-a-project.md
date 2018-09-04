---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 7 Maintaining a Project
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
next-title: Ch. 8 Creating a Custom Model Validator
next-page: creating-a-custom-model-validator
prev-title: Ch. 6 Testing Services and Systems
prev-page: testing-services-and-systems
---
{% include base.html %}

At this point, we have used Jellyfish to generate four services and one system.  In this chapter, we'll learn how to
make model changes and incorporate them into services and systems that have already been generated.

Once we have initially generated projects with Jellyfish, we typically no longer need to run Jellyfish directly.
Instead, we use Gradle to build and maintain our generated projects.

**Gradle and Jellyfish**
```note-info
Gradle actually uses Jellyfish internally when a service project is being built.  So even though Jellyfish isn't being
used directly, it's still being used indirectly by Gradle during a build.
```

# Updating the Model
Assume that it is necessary to make a change to the `SoftwareRequestService`.  The service is being updated so that
requests can be emailed as attachments directly to the system.  We'll update model to include an additional input
and a new scenario:

**SoftwareRequestService.sd**
```
model SoftwareRequestService {
   metadata {
      "stereotypes" : ["service"],
      "codegen": {
         "alias": "srs"
      }
   }

   input {
      InstallationRequest installationRequest
      EmailMessage emailedInstallationRequest
   }

   output {
      UnapprovedRequest unapprovedRequest
   }

   scenario processRequest {
      when receiving installationRequest
      then willPublish unapprovedRequest
   }

   scenario processEmailedRequest {
      when receiving emailedInstallationRequest
      then willPublish unapprovedRequest
   }
}
```

We now need to update the inputs and links in the top level `SoftwareStore` system:

**SoftwareStore.sd**
```
model SoftwareStore {
   input {
      InstallationRequest installationRequest
      EmailMessage emailedInstallationRequest // This input is new
   }

   // ... (omitted)

   links {
      link emailedInstallationRequest -> softwareRequestService.emailedInstallationRequest // this link is new
      link installationRequest -> softwareRequestService.installationRequest
      // ... (omitted)
   }
}
```

Finally, we can also include a new top-level scenario:

**SoftwareStore.sd**
```
scenario handleEmailedInstallationRequest {
   when receiving emailedInstallationRequest
   then willPublish emailMessage
}
```

Our modeling changes are complete.  We now need to publish this new version of the model.  Before doing this, we need to
update the version of the model project itself.  We can do this by editing the value of the `version` property in the SD
project's `build.gradle` file:

**Setting the version in build.gradle**
```groovy
version = '1.1.0-SNAPSHOT'
```

Note the previous version was `1.0.0-SNAPSHOT`.  In keeping with the principles of
[Semantic Versioning](https://semver.org/), we've updated the version to `1.1.0-SNAPSHOT`.  We can now build and upload
the project to a remote repository with `./gradlew clean build publish`.

# Update the Service and System
Now that a new version of the model has been published, we can update our existing services and system.  We do not need
to re-run Jellyfish.  Instead, we can just edit the `build.gradle` file of the service project
`com.helpco.helpdesk.softwarerequestservice`.  This is the build file for the root project.  The file contains the
following section:

**build.gradle**
```groovy
systemDescriptor {
   project = 'com.ngc.seaside:helpdesk.descriptor:1.0.0-SNAPSHOT'
   model = 'com.helpco.helpdesk.SoftwareRequestService'
   deploymentModel = 'com.helpco.helpdesk.LocalSoftwareStore'
}
```

Note the line `project = 'com.ngc.seaside:helpdesk.descriptor:1.0.0-SNAPSHOT'`.  This configures which version of the
SD project should be referenced.  Replace `1.0.0-SNAPSHOT` with `1.1.0-SNAPSHOT`:

**build.gradle**
```groovy
systemDescriptor {
   project = 'com.ngc.seaside:helpdesk.descriptor:1.1.0-SNAPSHOT'
   model = 'com.helpco.helpdesk.SoftwareRequestService'
   deploymentModel = 'com.helpco.helpdesk.LocalSoftwareStore'
}
```

Now perform a new build with the command `./gradlew clean build`.  The project will not compile due to errors in
the sub-project `com.helpco.helpdesk.softwarerequestservice.impl`.  This is due to a missing method that is not
implemented.  This error is the result of adding a new scenario to `SoftwareRequestService` model.  An IDE can
automatically generate this method, or we can manually create it in the `SoftwareRequestService` class:

**SoftwareRequestService.java**
```java
@Override
public UnapprovedRequest doProcessEmailedRequest(EmailMessage emailedInstalledRequest) throws ServiceFaultException {
   // TODO: implement this
   throw new UnsupportedOperationException("not implemented");
}
```

The project should now correctly build.

Updating the remaining services and system is similar.  Simply update the `build.gradle` file of each root project.

# Conclusion
When making model changes, always release a new version of the model.  Then update the `build.gradle` file of each
dependent service to reference the new version of the model.  Simply running `./gradlew clean build` will update the
generated projects.  It is not necessary to run Jellyfish directly again.
