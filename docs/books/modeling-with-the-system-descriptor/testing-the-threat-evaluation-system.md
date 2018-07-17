---
title: Ch. 10 Testing the Threat Evaluation System
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 11 Configuration Management
next-page: configuration-management
prev-title: Ch. 9 A Case Study - Modeling a Threat Evaluation System as an MSA
prev-page: a-case-study
---
{% include base.html %}
The `ThreatEvaluation` system of Archipelago was modeled in the last chapter.  We now need to
create the feature files that form test cases and help define the remaining behavior of the system.  These feature files
often must reference specific data fields in messages.  Therefore, our first effort will be to model the data of the
services we wish to test.  In this chapter, we'll be creating feature files for the `EngagementTrackPriorityService` and
`DefendedAreaTrackPriorityService.`  The remaining tests cases and files are left as an exercise to the reader.

# Modeling Detailed Data
The data for the `EngagementTrackPriorityService` will be modeled first since that is the first feature file we'll
create. This service has an input of `TrackEngagementStatus` and an output of `TrackPriority`.  These messages are
straightforward.

**TrackEngagementStatus.sd**
```
package com.ngc.seaside.archipelago.engagementplanning.datatype
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrackIdentifier
 
data TrackEngagementStatus {
    SystemTrackIdentifier trackIdentifier
    int plannedEngagementCount
    float probabilityOfKill
}
```

**TrackPriority.sd**
```
package com.ngc.seaside.archipelago.threat.datatype
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrackIdentifier
 
data TrackPriority {
    SystemTrackIdentifier trackIdentifier
    string sourceId {
        "description": "Identifies the specific service that generated this message."
    }
    float priority {
        "validation": {
            "min": 0.0,
            "max": 1.0
        }
    }
}
```

We can now create a first iteration of the feature file for the `EngagementTrackPriorityService`.  Since this service
only has a single scenario (c`alculateTrackPriority`), only one feature file is created.  This file will be placed in
the `src/test/gherkin` directory within the package/directory structure of `com.ngc.seaside.archipelago.threat`.  The
name of the file is `EngagementTrackPriorityService.calculateTrackPriority.feature`.

**EngagementTrackPriorityService.calculateTrackPriority.feature**
```plaintext
Feature: EngagementTrackPriorityService calculateTrackPriority
 
  Background:
    Given an absolute tolerance of 0.01
         
  Scenario Outline: Priority tests.
    The track priority should be calculated as:
    priority = 1.0 - probabilityOfKill
 
    Given a TrackEngagementStatus object
    And the trackId is <id>
    And the plannedEngagementCount is <count>
    And the probabilityOfKill is <probability>
    When the TrackEngagementStatus object is received by the service
    Then the service should respond with a TrackPriority object
    And the trackId should be <id>
    And the sourceId should be "service:com.ngc.seaside.archipelago.threat.EngagementTrackPriorityService"
    And the priority should be <priority>
 
    Examples:
      | id | count | probability | priority |
      |  0 |    10 |         0.5 |      0.5 |
      |  1 |    24 |         1.0 |      0.0 |
      |  2 |  5737 |         0.0 |      1.0 |
      |  3 |    24 |        0.25 |     0.75 |
```

The first test scenario is a scenario output that sets up some input and defines the expected output.  We can add a
scenario that tests for invalid input data to define how the service handles invalid inputs:

**EngagementTrackPriorityService.calculateTrackPriority.feature**
```
Feature: EngagementTrackPriorityService calculateTrackPriority
 
  Background:
    Given an absolute tolerance of 0.01
         
  Scenario Outline: Priority tests.
    The track priority should be calculated as:
    priority = 1.0 - probabilityOfKill
 
    Given a TrackEngagementStatus object
    And the trackId is <id>
    And the plannedEngagementCount is <count>
    And the probabilityOfKill is <probability>
    When the TrackEngagementStatus object is received by the service
    Then the service should respond with a TrackPriority object
    And the trackId should be <id>
    And the sourceId should be "service:com.ngc.seaside.archipelago.threat.EngagementTrackPriorityService"
    And the priority should be <priority>
 
    Examples:
      | id | count | probability | priority |
      |  0 |    10 |         0.5 |      0.5 |
      |  1 |    24 |         1.0 |      0.0 |
      |  2 |  5737 |         0.0 |      1.0 |
      |  3 |    24 |        0.25 |     0.75 |
 
  Scenario Outline: Invalid probability
    Given a TrackEngagementStatus object
    When the TrackEngagementStatus object is received by the service
    And the probabilityOfKill is <probability>
    Then a fault is raised
 
    Examples:
      | probability |
      |          -1 |
      |    -0.00001 |
      |     1.00001 |
      |       1e500 |
      |      1e-500 |
```

This scenario uses the step "Then a fault is raised".  We typically use this phrase to indicate the service should raise
an exception and halt processing of the request.  The infrastructure will handle reporting the actual fault.

# Referencing Data from Other Files
The feature file for the `DefendedAreaTrackPriorityService` provides us with a more complex example.  Like before, we
need to finish modeling the data for this service.  Since this service has two inputs, we need to finish the
`ImpactAssessment` and `SystemTrack` messages.  The `ImpactAssessment` message is straightforward:

**ImpactAssessment.sd**
```
package com.ngc.seaside.archipelago.defendedarea.datatype
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrackIdentifier
 
data ImpactAssessment {
    SystemTrackIdentifier systemTrackIdentifier
    float impactProbability
    float impactedAreaValue
}
```

The `SystemTrack` message requires more complex data.  We need to model its position and state using more complex data
structures.  We'll use a set of vectors to organize the state.  The state data objects will reside in the
`com.ngc.seaside.archipelago.common.datatype` package.

**Vector3.sd**
```
package com.ngc.seaside.archipelago.common.datatype
 
data Vector3 {
    float x
    float y
    float z
}
```

**StateVector.sd**
```
package com.ngc.seaside.archipelago.common.datatype
 
import com.ngc.seaside.archipelago.common.datatype.Vector3
 
data StateVector {
    Vector3 ecefPosition
    Vector3 ecefVelocity
    Vector3 ecefAcc
}
```

Finally, we just need to update the `SystemTrack` data itself.

**SystemTrack.sd**
```
package com.ngc.seaside.archipelago.tracking.datatype
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrackIdentifier
import com.ngc.seaside.archipelago.common.datatype.StateVector
 
data SystemTrack {
    SystemTrackIdentifier identifier
    StateVector state
}
```

The feature file can now reference these files.

The state vector is effectively a 3x3 matrix.  Specifying these values directly in the feature file can be messy.  As an
alternative, we can store these values in another data file such as a CSV file.  In our example, we store state
information for specific tracks in a CSV.  Data that is referenced in tests is stored under the directory
`src/test/resources/data`.  Common data that is reused for multiple tests can be stored directly in this directory.  By
convention, data that is referenced only by tests in a single package is placed inside that package.  For our tests,
we'll create a CSV in the directory `src/test/resources/data/com/ngc/seaside/archipelago/threat`.  The name of this file
doesn't manner so we'll use `DefendedAreaSystemTrackData.csv` for consistency.  The contents of this file might look
something like this:

| identifier |state.ecefPosition.x | state.ecefPosition.y | state.ecefPosition.z | tate.ecefVelocity.x | state.ecefVelocity.y | state.ecefVelocity.z | state.ecefAcc.x | state.ecefAcc.y | state.ecefAc
|------------|---------------------|----------------------|----------------------|---------------------|----------------------|----------------------|-----------------|-----------------|-------------
| 0 | 22.58422 | 90.86906 | 97.60875 | 21.01349 | 93.01078 | 45.71687 | 45.1883 | 88.56428 | 80.38899523
| 1 | 9999.347 | 123444.5 | 33.71943 | 19.43223 | 13.03871 | 73.78143 | 28.03295 | 70.35227 | 44.99680078
| 2 | 3194442 | -3194442 | 4487380 | 99.12156 | 52.96208 | 84.78288 | 5.413042 | 39.38792 | 81.13938798
| 3 | 3194442 | -3194442 | -4487380 | 93.89776 | 28.81023 | 78.46608 | 38.39385 | 89.20086 | 68.58333996