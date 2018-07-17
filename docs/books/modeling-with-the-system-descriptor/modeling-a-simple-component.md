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
```
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
```
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

# The Alarm Component
The `Alarm` component is responsible for triggering alarms and deactivating alarms.  The `AlarmController` component
will handle the user interaction and provide the actual alarm time settings as provided by the user.  The controller
will also handle notifying the user of an alarm.  As a result, our first cut of the `Alarm` model looks something like
this:

**Alarm.sd**
```
package alarm
 
model Alarm {
     
    scenario triggerAlarm {
    }
     
    scenario deactivateAlarm {
    }
}
```

This model has two empty scenarios: one for triggering an alarm and one for deactivating an alarm.  We need to define
some inputs and outputs for completing these scenarios.  We'll also need new data types for acknowledging the alarm as
well as triggering an alarm.  Will create these two data types as shown below.  `AlarmAcknowledgement` is used to
acknowledge and deactivate an alarm while `AlarmStatus` is used to notify that an alarm has been triggered.

**AlarmAcknowledgement.sd**
```
package alarm
 
data AlarmAcknowledgement {
    int alarmId
    boolean alarmAcknowledged
}
```

**AlarmStatus.sd**
```
package alarm
 
data AlarmStatus {
    int id
    boolean active
}
```

We can now complete the `Alarm` component.  This component will receive the configured alarm time, the current time, and
acknowledged alarms as input.  It will produce alarms as outputs.  We can now add these fields and complete the
scenarios.

**Updating Alarm.sd**
```
package alarm
 
import alarm.AlarmStatus
import alarm.ZonedTime
import alarm.AlarmAcknowledgement
 
model Alarm {
     
  input {
        ZonedTime currentTime
        ZonedTime alarmTime
        AlarmAcknowledgement alarmAcknowledgement
    }
     
    output {
        AlarmStatus alarmStatus
    }
     
    scenario triggerAlarm {
        given haveReceived alarmTime
        when receiving currentTime
        then willPublish alarmStatus
    }
     
    scenario deactivateAlarm {
        when receiving alarmAcknowledgement
        then willPublish alarmStatus
    }
}
```

The scenario triggerAlarm indicates that the alarm component will publish alarm status events when the current time is
received.  In order for this scenario to be activated, the alarm time must have been received.  We don't define under
what circumstances that the alarm is generated.  We'll define this level of detail in the accompanying test cases that
we'll describe in [Ch. 7]({{ safebase }}/books/modeling-with-the-system-descriptor/writing-feature-files-to-describe-behavior.html).  
The scenario deactivateAlarm will trigger when an acknowledgment is received.  The component will then publish an 
updated alarm status indicating the alarm should be deactivated.

# The AlarmController Component
The `AlarmController` component is how users interact with the alarm clock. We don't model actual users.  Instead, we
consider the `AlarmController` component to be at the boundary of our system.  We'll use special verbs to indicate that
this component interacts with components that are not modeled.

The `AlarmController` will output alarm times and alarm acknowledgements as configured by the user.  The controller will
receive alarm status that indicate an alarm has been triggered or deactivated.  We model this as follows:

**AlarmController.sd**
```
package alarm
 
import alarm.ZonedTime
import alarm.AlarmStatus
import alarm.AlarmAcknowledgement
 
model AlarmController {
     
    input {
        AlarmStatus alarmStatus
    }
     
    output {
        ZonedTime alarmTime
        AlarmAcknowledgement alarmAcknowledgement
    }
     
    scenario setAlarmTime {
        when interacting with user
        then willPublish alarmTime
    }
     
    scenario notifyUserOfAlarm {
        when recieving alarmStatus
        then willInteract with user
    }
     
    scenario acknowledgeAlarm {
        when interacting with user
        then willPublish alarmAcknowledgement
    }
}
```

This model has three scenarios:
* `setAlarmTime` - This scenario describes the behavior when the user sets the alarm time.
* `notifyUserOfAlarm` - This scenario describes what happens when an alarm is trigger.
* `acknowledgeAlarm` - This scenario describes what happens when the user acknowledges as alarm.

Note the use of the `interact` verb.  We use this verb to indicate that this component will interact with an external
component.  It this case, that is the user.  We don't define how this interaction occurs, only that it does.

# Linking Parts Together
We now have three components of our alarm clock.  Recall our `AlarmClock` model looks like this:

**AlarmClock.sd**
```
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

We have defined each of these components but we haven't described how these components interact together to form the
upper level `AlarmClock` system.  We can use the language's concept of a `link` to do this.  Links are used to connect
inputs and outputs from one part to another.  For example, we can connect the `currentTime` output of the `clock` part
to the `currentTime` input of the `alarm` part.

**Adding links**
```
package alarm
 
import alarm.Alarm
import alarm.AlarmController
import alarm.Clock
 
model AlarmClock {
   
  parts {
      Alarm alarm
      AlarmController controller
      Clock clock
  }
   
  links {
      link clock.currentTime -> alarm.currentTime
  }
}
```

The link notation follows this pattern: `link` _partName.fieldName_ `->` _partName.fieldName_

Links are directional.  The direction of the arrow indicates the direction of data flow.  Outputs are typically listed
on the left side of the arrow and inputs are listed on the right side of the arrow.  The fields on both sides of the
arrow that are linked must be the same type.

We can now connect the outputs of the controller to the inputs of the alarm.  We can also connect the outputs of the
alarm to the inputs of the controller:

**Adding links**
```
package alarm
 
import alarm.Alarm
import alarm.AlarmController
import alarm.Clock
 
model AlarmClock {
   
  parts {
      Alarm alarm
      AlarmController controller
      Clock clock
  }
   
  links {
      link clock.currentTime -> alarm.currentTime
       
      link controller.alarmTime -> alarm.alarmTime
      link controller.alarmAcknowledgement -> alarm.alarmAcknowledgement
       
      link alarm.alarmStatus -> controller.alarmStatus
  }
}
```

Any model can contain links or parts.  In many cases, links and parts are only found in models of integrated systems as
opposed to individual components.  It is also possible to connect an input field of the model itself to an input of part
of the model.  Likewise, an output field of the model can be linked to the output of a model's part.  Links should not
be used to create connections directly between inputs and outputs of the same model.  Use scenarios to convey that.