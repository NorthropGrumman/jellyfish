---
title: Ch. 5 Modeling a Simple Component
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 6 Top Down Modeling - Decomposing Components
next-page: top-down-modeling-decomposing-components
prev-title: Ch. 4 Modeling Data
prev-page: modeling-data
---
{% include base.html %} So far we have modeled several individual components of an alarm clock but have yet to model the
complete system.  In this chapter, we'll be exploring the top down modeling capabilities of the System Descriptor (SD)
language.  We'll start by creating a top level component of the entire alarm clock system.  We'll then decompose the
system into individual components.

First, we'll create the model that represents the top level element that we will call `AlarmClock`.  Our system is a
simple standalone system.  It contains all the components it needs to function.  As a result, it won't have inputs or
outputs.  It will only consist of sub-components which will we model as _parts_.

**AlarmClock.sd**
```sd
package alarm
 
import alarm.Clock
 
model AlarmClock {
   
  parts {
      Clock clock
  }
}
```

Our initial model contains only a single 'part' field named clock.  Like other types of fields, a 'part' field contains
a model type (Clock) and a name (clock).  The field is enclosed in the `parts` block.

Our alarm clock already contains a _clock_ component that tracks time.  We need to create two additional components: a
component for managing the alarms and a component for allowing the user control the alarm clock.  We'll call these two
components _Alarm_ and _AlarmController_, respectively.  These will be parts of the `AlarmClock`.

**Adding new parts**
```sd
package alarm
 
import alarm.Clock
import alarm.Alarm
import alarm.AlarmController
 
model AlarmClock {
  parts {
      Clock clock
      Alarm alarm
      AlarmController controller
  }
}
```