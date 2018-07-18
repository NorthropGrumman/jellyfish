---
title: Ch. 9 A Case Study - Modeling a Threat Evaluation System as an MSA
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 10 Testing the Threat Evaluation System
next-page: testing-the-threat-evaluation-system
prev-title: Ch. 8 Introduction to Micro Service Architectures
prev-page: introduction-to-micro-service-architectures
---
{% include base.html %}
In this section, we'll deep dive into a specific use case.  We'll create a model of a complex
system and fully model one of this sub-components.  This sub-component will be responsible for doing threat evaluation
of tracks.  The overall system or product will have various responsibilities, all dealing with tracking objects,
classifying them, and potentially engaging them.  The name of this product will be Archipelago.  The architecture of the
product will be event/subscriber (ie, pub/sub) based.

First, create a new SD project named Archipelago.  Our default package will be `com.ngc.seaside.archipelago`.  The main
component that represents the entire system will reside in this package and be named `Archipelago`:

**Archipelago.sd**
```
package com.ngc.seaside.archipelago
 
model Archipelago {
  metadata {
    "stereotypes" : ["system", "product"]
  }
}
```

We use the `system` and `product` stereotypes to help identify what kind of component this model represents.  Our
Archipelago product has the following responsibilities:
* tracking objects
* classifying objects
* determining the areas or positions impacted or threatened by tracked objects
* creating engagement plans against objects
* and finally determining the threat of track objects

Each of these responsibilities can be allocated to a sub-system or sub-component of Archipelago.  We'll declare these
components using the `parts` keyword.  We'll create a model for each of these responsibilities.  Each of these models
carries the `system` stereotype.

**Adding sub systems to Archipelago**
```
package com.ngc.seaside.archipelago
 
import com.ngc.seaside.archipelago.tracking.Tracker
import com.ngc.seaside.archipelago.threat.ThreatEvaluation
import com.ngc.seaside.archipelago.engagementplanning.EngagementPlanner
import com.ngc.seaside.archipelago.defendedarea.AreaManager
import com.ngc.seaside.archipelago.classification.ObjectClassification
 
model Archipelago {
  metadata {
    "stereotypes" : ["system", "product"]
  }
   
  parts {
      ThreatEvaluation threatEval // determines the threat of track objects
      Tracker tracker // tracks objects
      EngagementPlanner planner // creates engagement plans against objects
      AreaManager areaManager // determines the areas or positions impacted or threatened by tracked objects
      ObjectClassification classification // classifies objects
  }
}
```

Note that we have placed each component into its own package to help organize the model.  Throughout this chapter, we'll
expand and refine the `ThreatEvaluation` system.  We will simply stub out the remaining systems in order to define the
interface between the `ThreatEvaluation` system and the other systems.

# Modeling ng Data
`ThreatEvaluation` needs data for tracks, classification, impact assessments, etc in order to prioritize threats.  This
data must be modeled and listed as inputs for the `ThreatEvaluation` system.  We can model the inputs to the system as
follows:

**ThreatEvaluation.sd**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrack
import com.ngc.seaside.archipelago.classification.datatype.Classification
import com.ngc.seaside.archipelago.engagementplanning.datatype.TrackEngagementStatus
import com.ngc.seaside.archipelago.defendedarea.datatype.ImpactAssessment
import com.ngc.seaside.archipelago.tracking.datatype.DroppedSystemTrack
 
model ThreatEvaluation {
    metadata {
        "stereotypes": ["system"]
    }
 
    input {
        SystemTrack systemTrack
        Classification systemTrackClassification
        TrackEngagementStatus trackEngagementStatus
        ImpactAssessment impactAssessment
        DroppedSystemTrack droppedSystemTrack
    }
}
```

Again, we have placed the data structure in various datatype packages.  We choose which package the data resides in by
making following decisions:
1. Tracking is responsible for reporting new, updated, and removed (i.e. dropped) system tracks.
1. Classification is responsible for reporting system track classification.
1. Defended Area Management is responsible for reporting impact assessments of tracks.
1. Engagement Planning is responsible for reporting on the engagement status of tracks.

Finally, `ThreatEvaluation` itself must report on track priorities.  We list `PrioritizedSystemTrackIdentifiers `as an
output of the system to capture this:

**Adding an output to ThreatEvaluation**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrack
import com.ngc.seaside.archipelago.classification.datatype.Classification
import com.ngc.seaside.archipelago.engagementplanning.datatype.TrackEngagementStatus
import com.ngc.seaside.archipelago.defendedarea.datatype.ImpactAssessment
import com.ngc.seaside.archipelago.tracking.datatype.DroppedSystemTrack
import com.ngc.seaside.archipelago.threat.datatype.PrioritizedSystemTrackIdentifiers
 
model ThreatEvaluation {
    metadata {
        "stereotypes": ["system"]
    }
 
    input {
        SystemTrack systemTrack
        Classification systemTrackClassification
        TrackEngagementStatus trackEngagementStatus
        ImpactAssessment impactAssessment
        DroppedSystemTrack droppedSystemTrack
    }
    
    output {
        PrioritizedSystemTrackIdentifiers prioritizedSystemTracks
    }
}
```

We will define the content of these data messages later, as needed.  For now, we simply declare them as empty data
structures.

# Defining High Level Behavior and Interaction
At this point, we can add high level scenarios to the `ThreatEvaluation` system to refine its responsibilities:

**Adding an scenarios to ThreatEvaluation**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrack
import com.ngc.seaside.archipelago.classification.datatype.Classification
import com.ngc.seaside.archipelago.engagementplanning.datatype.TrackEngagementStatus
import com.ngc.seaside.archipelago.defendedarea.datatype.ImpactAssessment
import com.ngc.seaside.archipelago.tracking.datatype.DroppedSystemTrack
import com.ngc.seaside.archipelago.threat.datatype.PrioritizedSystemTrackIdentifiers
 
model ThreatEvaluation {
    metadata {
        "stereotypes": ["system"]
    }
 
    input {
        SystemTrack systemTrack
        Classification systemTrackClassification
        TrackEngagementStatus trackEngagementStatus
        ImpactAssessment impactAssessment
        DroppedSystemTrack droppedSystemTrack
    }
    
    output {
        PrioritizedSystemTrackIdentifiers prioritizedSystemTracks
    }
 
    scenario calculateTrackPriority {
        when receiving systemTrack
        and receiving impactAssessment
        and receiving systemTrackClassification
        and receiving trackEngagementStatus
        then willPublish prioritizedSystemTracks
    }
     
    scenario calculateTrackPriorityWhenTrackDropped {
        when receiving droppedSystemTracks
        then willPublish prioritizedSystemTracks
    }
}
```

These new scenarios indicate that the system must calculate the priority of a track whenever a related update for that
track is received.  Likewise, the system must recalculate the priority of tracks when a track is dropped.

We can now move back up the system hierarchy and update the other systems.  We won't be adding any scenarios to these
models because we are not interested in their behavior at the moment.  Instead, we list the outputs of each system.  We
can then link the outputs of each system to the inputs of the `ThreatEvaluation` system.  This provides more context as
to how the `ThreatEvaluation` system is utilized in the overall product.

First, we'll update the `Tracker` system to output information about tracks:

**Tracker.sd**
```
package com.ngc.seaside.archipelago.tracking
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrack
import com.ngc.seaside.archipelago.tracking.datatype.DroppedSystemTrack
 
model Tracker {
  metadata {
    "stereotypes" : ["system"]
  }
   
  output {
      SystemTrack systemTrack
      DroppedSystemTrack droppedSystemTrack
  }
}
```

Next, we'll update `EngagementPlanner`:

**EngagementPlanner.sd**
```
package com.ngc.seaside.archipelago.engagementplanning
 
import com.ngc.seaside.archipelago.engagementplanning.datatype.TrackEngagementStatus
 
model EngagementPlanner {
  metadata {
    "stereotypes" : ["system"]
  }
   
  output {
      TrackEngagementStatus trackEngagementStatus
  }
}
```

Finally, we'll update `AreaManager` and `ObjectClassification`:

**AreaManager.sd**
```
package com.ngc.seaside.archipelago.defendedarea
 
import com.ngc.seaside.archipelago.defendedarea.datatype.ImpactAssessment
 
model AreaManager {
  metadata {
    "stereotypes" : ["system"]
  }
   
  output {
      ImpactAssessment impactAssessment
  }
}
```

**ObjectClassification.sd**
```
package com.ngc.seaside.archipelago.classification
 
import com.ngc.seaside.archipelago.classification.datatype.Classification
 
model ObjectClassification {
  metadata {
    "stereotypes" : ["system"]
  }
   
  output {
      Classification systemTrackClassification
  }
}
```

At this point, we can create links between the `ThreatEvaluation` system and the other systems by linking outputs of the
other systems to inputs of the `ThreatEvaluation` system.  These links are added to the top level `Archipelago` model:

**Adding links to Archipelago.sd**
```
package com.ngc.seaside.archipelago
 
import com.ngc.seaside.archipelago.tracking.Tracker
import com.ngc.seaside.archipelago.threat.ThreatEvaluation
import com.ngc.seaside.archipelago.engagementplanning.EngagementPlanner
import com.ngc.seaside.archipelago.defendedarea.AreaManager
import com.ngc.seaside.archipelago.classification.ObjectClassification
 
model Archipelago {
  metadata {
    "stereotypes" : ["system", "product"]
  }
   
  parts {
      ThreatEvaluation threatEval // determines the threat of track objects
      Tracker tracker // tracks objects
      EngagementPlanner planner // creates engagement plans against objects
      AreaManager areaManager // determines the areas or positions impacted or threatened by tracked objects
      ObjectClassification classification // classifies objects
  }
   
  links {
      link tracker.systemTrack -> threatEval.systemTrack
      link tracker.droppedSystemTrack -> threatEval.droppedSystemTrack
      link planner.trackEngagementStatus -> threatEval.trackEngagementStatus
      link areaManager.impactAssessment -> threatEval.impactAssessment
      link classification.systemTrackClassification -> threatEval.systemTrackClassification
  }
}
```

The flow of information from the rest of the system to `ThreatEvaluation` is now more obvious.  We have also verified
that all data needs for the system have been met.

# Decomposing ThreatEvaluation into Services
We can now turn our attention to the `ThreatEvaluation system`.  The system will be composed of four services.  Recall
that a track's priority is computed using data from three sources: engagement, area and position, and classification.  A
separate service will handle each of these areas.  A fourth service will aggregate the results from each service
together and produce a consolidate report of track priories.  The next iteration would include these four services as
parts of the `ThreatEvaluation` system:

**Adding services to ThreatEvaluation.sd**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrack
import com.ngc.seaside.archipelago.classification.datatype.Classification
import com.ngc.seaside.archipelago.engagementplanning.datatype.TrackEngagementStatus
import com.ngc.seaside.archipelago.defendedarea.datatype.ImpactAssessment
import com.ngc.seaside.archipelago.tracking.datatype.DroppedSystemTrack
import com.ngc.seaside.archipelago.threat.datatype.PrioritizedSystemTrackIdentifiers
import com.ngc.seaside.archipelago.threat.ClassificationTrackPriorityService
import com.ngc.seaside.archipelago.threat.EngagementTrackPriorityService
import com.ngc.seaside.archipelago.threat.DefendedAreaTrackPriorityService
import com.ngc.seaside.archipelago.threat.TrackPriorityService
 
model ThreatEvaluation {
    metadata {
        "stereotypes": ["system"]
    }
 
    input {
        SystemTrack systemTrack
        Classification systemTrackClassification
        TrackEngagementStatus trackEngagementStatus
        ImpactAssessment impactAssessment
        DroppedSystemTrack droppedSystemTrack
    }
 
    output {
        PrioritizedSystemTrackIdentifiers prioritizedSystemTracks
    }
 
    scenario calculateTrackPriority {
        when receiving systemTrack
        and receiving impactAssessment
        and receiving systemTrackClassification
        and receiving trackEngagementStatus
        then willPublish prioritizedSystemTracks
    }
     
    scenario calculateTrackPriorityWhenTrackDropped {
        when receiving droppedSystemTrack
        then willPublish prioritizedSystemTracks
    }
 
    parts {
        ClassificationTrackPriorityService classificationTrackPriorityService
        EngagementTrackPriorityService engagementTrackPriorityService
        DefendedAreaTrackPriorityService defendedAreaTrackPriorityService
        TrackPriorityService trackPriorityService
    }
}
```

All of these services reside in the `com.ngc.seaside.archipelago.threat` package.

In order to model each individual service, we need to create a new data structure that the three specific services and
the fourth aggregating service will use to exchange information.  We'll place this data in the
`com.ngc.seaside.archipelago.threat.datatype` package and name it `TrackPriority`.

We'll first model the `ClassificationTrackPriorityService`:

**ClassificationTrackPriorityService.sd**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.classification.datatype.Classification
import com.ngc.seaside.archipelago.threat.datatype.TrackPriority
 
model ClassificationTrackPriorityService {
    metadata {
        "stereotypes": ["service"]
    }
 
    input {
        Classification systemTrackClassification
    }
 
    output {
        TrackPriority trackPriority
    }
 
    scenario calculateTrackPriority {
        when receiving systemTrackClassification
        then willPublish trackPriority
    }
}
```

This service only has a single responsibility: it must receive the classification of a system track and publish a track
priority.  The `EngagementTrackPriorityService` is very similar:

**EngagementTrackPriorityService.sd**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.engagementplanning.datatype.TrackEngagementStatus
import com.ngc.seaside.archipelago.threat.datatype.TrackPriority
 
model EngagementTrackPriorityService {
    metadata {
        "stereotypes": ["service"]
    }
 
    input {
        TrackEngagementStatus trackEngagementStatus
    }
 
    output {
        TrackPriority trackPriority
    }
 
    scenario calculateTrackPriority {
        when receiving trackEngagementStatus
        then willPublish trackPriority
    }
}
```

This service only differs in the type of input.  The next service is the `TrackPriorityService`:

**TrackPriorityService.sd**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.threat.datatype.PrioritizedSystemTrackIdentifiers
import com.ngc.seaside.archipelago.tracking.datatype.DroppedSystemTrack
import com.ngc.seaside.archipelago.threat.datatype.TrackPriority
 
model TrackPriorityService {
    metadata {
        "stereotypes": ["service"]
    }
 
    input {
        TrackPriority trackPriority
        DroppedSystemTrack droppedSystemTrack
    }
 
    output {
        PrioritizedSystemTrackIdentifiers prioritizedSystemTracks
    }
 
    scenario calculateConsolidatedTrackPriority {
        when receiving trackPriority
        then willPublish prioritizedSystemTracks
    }
 
    scenario calculateConsolidatedTrackPriorityWhenTrackDropped {
        when receiving droppedSystemTrack
        then willPublish prioritizedSystemTracks
    }
 
}
```
This service must deal with system tracks which are removed or dropped, hence the extra input and the extra scenario. 

# Dealing with Multiple Inputs
The final service to model is the `DefendedAreaTrackPriorityService`.  This service requires two inputs and is shown
below:

**DefendedAreaTrackPriorityService.sd**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.defendedarea.datatype.ImpactAssessment
import com.ngc.seaside.archipelago.threat.datatype.TrackPriority
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrack
 
model DefendedAreaTrackPriorityService {
    metadata {
        "stereotypes": ["service"]
    }
 
    input {
        SystemTrack systemTrack
        ImpactAssessment impactAssessment
    }
 
    output {
        TrackPriority trackPriority
    }
}
```

This service requires a `SystemTrack` message and an `ImpactAssessment` message together to compute its output.  We can
attempt to model this as a scenario as shown below:

**Adding a scenario to DefendedAreaTrackPriorityService.sd**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.defendedarea.datatype.ImpactAssessment
import com.ngc.seaside.archipelago.threat.datatype.TrackPriority
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrack
 
model DefendedAreaTrackPriorityService {
    metadata {
        "stereotypes": ["service"]
    }
 
    input {
        SystemTrack systemTrack
        ImpactAssessment impactAssessment
    }
 
    output {
        TrackPriority trackPriority
    }
 
    scenario calculateTrackPriority {
        when receiving impactAssessment
         and receiving systemTrack
        then willPublish trackPriority
    }
}
```

The `calcuateTrackPriority` scenario indicates that any impact assessment and any system track can be used together to
publish a track priority.  However, the system track message and the impact assessment message must be for the same
track.  We can utilize the `correlate` verb to indicate this.  Before we update the scenario, we'll model the data for
the `SystemTrack` and the `ImpactAssessment` messages:

**SystemTrackIdentifier.sd**
```
package com.ngc.seaside.archipelago.tracking.datatype
 
data SystemTrackIdentifier {
    int trackId
}
```

**SystemTrack.sd**
```
package com.ngc.seaside.archipelago.tracking.datatype
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrackIdentifier
 
data SystemTrack {
    SystemTrackIdentifier identifier
}
```

**ImpactAssessment.sd**
```
package com.ngc.seaside.archipelago.defendedarea.datatype
 
import com.ngc.seaside.archipelago.common.datatype.BaseData
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrackIdentifier
 
data ImpactAssessment {
    SystemTrackIdentifier systemTrackIdentifier
}
```

We needed to model this data because the `correlate` verb will reference the fields within the data.

**Using the correlate verb**
```
scenario calculateTrackPriority {
  when receiving impactAssessment
   and receiving systemTrack
   and correlating systemTrack.identifier.trackId to impactAssessment.systemTrackIdentifier.trackId
  then willPublish trackPriority
}
```
This indicates that the received impact assessment and system track must be correlated together by asserting the track
ID for the system assessment message and the track ID for the system track message match.  Messages received that do not
meet this condition will not result in triggering the scenario.

# Linking Services Together
The final step is to update the `ThreatEvaluation` system to link the different services together.  In this iteration, we
use the links construct to link inputs to the entire system directly to inputs of services.

**Adding links to ThreatEvaluation.sd**
```
package com.ngc.seaside.archipelago.threat
 
import com.ngc.seaside.archipelago.tracking.datatype.SystemTrack
import com.ngc.seaside.archipelago.classification.datatype.Classification
import com.ngc.seaside.archipelago.engagementplanning.datatype.TrackEngagementStatus
import com.ngc.seaside.archipelago.defendedarea.datatype.ImpactAssessment
import com.ngc.seaside.archipelago.tracking.datatype.DroppedSystemTrack
import com.ngc.seaside.archipelago.threat.datatype.PrioritizedSystemTrackIdentifiers
import com.ngc.seaside.archipelago.threat.ClassificationTrackPriorityService
import com.ngc.seaside.archipelago.threat.EngagementTrackPriorityService
import com.ngc.seaside.archipelago.threat.DefendedAreaTrackPriorityService
import com.ngc.seaside.archipelago.threat.TrackPriorityService
 
model ThreatEvaluation {
    metadata {
        "stereotypes": ["system"]
    }
 
    input {
        SystemTrack systemTrack
        Classification systemTrackClassification
        TrackEngagementStatus trackEngagementStatus
        ImpactAssessment impactAssessment
        DroppedSystemTrack droppedSystemTrack
    }
 
    output {
        PrioritizedSystemTrackIdentifiers prioritizedSystemTracks
    }
 
    scenario calculateTrackPriority {
        when receiving systemTrack
        and receiving impactAssessment
        and receiving systemTrackClassification
        and receiving trackEngagementStatus
        then willPublish prioritizedSystemTracks
    }
     
    scenario calculateTrackPriorityWhenTrackDropped {
        when receiving droppedSystemTrack
        then willPublish prioritizedSystemTracks
    }
 
    parts {
        ClassificationTrackPriorityService classificationTrackPriorityService
        EngagementTrackPriorityService engagementTrackPriorityService
        DefendedAreaTrackPriorityService defendedAreaTrackPriorityService
        TrackPriorityService trackPriorityService
    }
     
    links {
        link systemTrackClassification -> classificationTrackPriorityService.systemTrackClassification
        link trackEngagementStatus -> engagementTrackPriorityService.trackEngagementStatus
        link systemTrack -> defendedAreaTrackPriorityService.systemTrack
        link impactAssessment -> defendedAreaTrackPriorityService.impactAssessment
        link droppedSystemTrack -> trackPriorityService.droppedSystemTrack
         
        link classificationTrackPriorityService.trackPriority -> trackPriorityService.trackPriority
        link engagementTrackPriorityService.trackPriority -> trackPriorityService.trackPriority
        link defendedAreaTrackPriorityService.trackPriority -> trackPriorityService.trackPriority
    }
}
```

We now have a complete model of the threat evaluation sub-system of Archipelago and a high level but incomplete model of Archipelago itself.

# Conclusion
In this chapter we saw how to model a system from the top down.  We saw how to model sub-systems and
decompose them into services.  We also demonstrated how to completely model a sub-component independent of its sibling
components.  We were able to model the `ThreatEvaluation` system by only stubbing the remaining systems.