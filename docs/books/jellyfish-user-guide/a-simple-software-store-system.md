---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 2 A Simple Software Store System
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
next-title: Ch. 3 A Lightweight Micro-Service Architecture for Java
next-page: a-lightweight-micro-service-architecture-for-java
prev-title: Ch. 1 Introduction to Jellyfish
prev-page: introduction-to-jellyfish
---
{% include base.html %}

We'll reference a simple example system throughout the next several chapters.  We use this example as we explore how
Jellyfish works.

This example is a "Software Store" system.  This system allows users to request a new application be installed on their
PC.  The system verifies the request, performs the installation, and then notifies the user of the status of the
request.  It might look something like this:

![software store][design1]

This system has the following components:
* SoftwareRequestService: This service receives an initial request, verifies the request is valid, and then forwards the
request with additional info to the rest of the system.
* LicenseService: This service verifies the license for the actual software is available and all requirements to install
the service are met.
* PcManagementService: This service performs the actual installation of the requested software onto a user's PC.
* EmailService: This service generates emails and notifies requesters of updates to their requests.

Using the System Descriptor (SD) language, we might model each component like so:

**SoftwareRequestService.sd**
```
package com.helpco.helpdesk

import com.helpco.helpdesk.datatype.InstallationRequest
import com.helpco.helpdesk.datatype.UnapprovedRequest

model SoftwareRequestService {
   input {
      InstallationRequest installationRequest
   }

   output {
      UnapprovedRequest unapprovedRequest
   }

   scenario processRequest {
      when receiving installationRequest
      then willPublish unapprovedRequest
   }
}
```

**LicenseService.sd**
```
package com.helpco.helpdesk

import com.helpco.helpdesk.datatype.UnapprovedRequest
import com.helpco.helpdesk.datatype.ReviewedRequest

model LicenseService {
   input {
      UnapprovedRequest unapprovedRequest
   }

   output {
      ReviewedRequest reviewedRequest
   }

   scenario reviewRequest {
      when receiving unapprovedRequest
      then willPublish reviewedRequest
   }
}

```

**PcManagementService.sd**
```
package com.helpco.helpdesk

import com.helpco.helpdesk.datatype.ReviewedRequest
import com.helpco.helpdesk.datatype.InstallationStatus

model PcManagementService {
   input {
      ReviewedRequest reviewedRequest
   }

   output {
      InstallationStatus installationStatus
   }

   scenario performInstallation {
      when receiving reviewedRequest
      then willPublish installationStatus
   }
}

```

**EmailService.sd**
```
package com.helpco.helpdesk

import com.helpco.helpdesk.datatype.InstallationStatus
import com.helpco.helpdesk.datatype.EmailMessage

model EmailService {
   input {
      InstallationStatus installationStatus
   }

   output {
      EmailMessage emailMessage
   }

   scenario notifyRequestorOfStatus {
      when receiving installationStatus
      then willPublish emailMessage
   }
}

```

Finally, the top level model for the entire system might look like this:

**SoftwareStore.sd**
```
package com.helpco.helpdesk

import com.helpco.helpdesk.datatype.InstallationRequest
import com.helpco.helpdesk.datatype.EmailMessage
import com.helpco.helpdesk.SoftwareRequestService
import com.helpco.helpdesk.LicenseService
import com.helpco.helpdesk.PcManagementService
import com.helpco.helpdesk.EmailService

model SoftwareStore {
   input {
      InstallationRequest installationRequest
   }

   output {
      EmailMessage emailMessage
   }

   scenario handleInstallationRequest {
      when receiving installationRequest
      then willPublish emailMessage
   }

   parts {
      SoftwareRequestService softwareRequestService
      LicenseService licenseService
      PcManagementService pcManagementService
      EmailService emailService
   }

   links {
      link installationRequest -> softwareRequestService.installationRequest
      link emailService.emailMessage -> emailMessage

      link softwareRequestService.unapprovedRequest -> licenseService.unapprovedRequest

      link licenseService.reviewedRequest -> pcManagementService.reviewedRequest

      link pcManagementService.installationStatus -> emailService.installationStatus
   }
}

```

Note this system as a single input `InstallationRequest` and a single output `EmailMessage`.

We'll omit design of the data types because their actual contents aren't very important for this example.

[design1]: {{ safebase }}/assets/images/software-store.png
