---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 5 Modeling a Simple Component
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 6 Top Down Modeling - Decomposing Components
next-page: top-down-modeling-decomposing-components
prev-title: Ch. 4 Modeling Data
prev-page: modeling-data
---
{% include base.html %}

Data itself isn't very useful without models.  A model can represent anything: a software
service or component, a hardware element, an entire system, etc.  The System Descriptor (SD) language allows models to
declare both their structure (i.e. their inputs, outputs, and sub-components) as well as the behavior they should
exhibit.  Models are made up of the following:
* input fields: used to declare a model requires some type of data.
* output fields:  used to declare a model produces some type of data.
* part fields: used to declare sub-components or parts of a model.
* scenarios: declare the behavior of a model.

We'll cover input and output fields as well as basic scenarios in this section.  Part fields will be covered in
[Ch. 6]({{ safebase }}/books/modeling-with-the-system-descriptor/top-down-modeling-decomposing-components.html).

The next component we'll create is a model of a clock.  This clock will by used by our alarm clock system to determine
the current type.  Models are constructed using the `model` keyword.  Like data, models are declared within packages.
Our `Clock` model will be declared in the `alarm` package.
1. Right click on the alarm package
1. Select **New** -> **Other...**
1. Expand **JellyFish**
1. Select **System Descriptor File**
1. Select **Next**
1. Enter Clock for the **Name** (leave the Model radio button selected)
1. Select **Finish**
1. Edit the file to look similar to the below model

**Clock.sd**
```
package alarm
 
model Clock {
    metadata {
        "description": "Determines the current time and periodically publishes the current time."
    }
}
```

This declares a new model.  Like data types, models may have also contain metadata.  Note you can format the file using
the keyboard shortcut `Ctrl + Shift + F` We can use metadata to add a human readable description of the `Clock`
component.

**Learning Eclipse shortcuts**
```note-info
You can format the file using the keyboard shortcut Ctrl + Shift + F
```

# Modeling Structure
Most models contain structural information, such as data inputs and outputs.  These can be declared using the the
`input` and `output` keyword respectively.  Our `Clock` component contains a single output which is the current time.

**Declaring output fields**
```
package alarm
 
import alarm.ZonedTime
 
model Clock {
    metadata {
        "description": "Determines the current time and periodically publishes the current time."
    }
     
    output {
        ZonedTime currentTime
    }
}
```

**Learning Eclipse shortcuts**
```note-info
If you forgot to import the ZonedTime data object. You can easily import it using Eclipse.
1. Make sure your file is selected/focused on in Eclipse
2. Hover your mouse over ZonedTime and wait for the menu to pop up
3. Select Import ZonedTime (alarm)
```

We have declared that the `Clock` component produces `ZonedTime `as an output by declaring a new output field.  Output
fields are like data fields; they consists of a data type and a name.  The name must be unique among all types of fields
contained in the model (`input`, `output`, and `part`).  Again, note that the `ZonedType` data type must be imported.
Only data types can be declared as output fields.

All types of fields, including output fields may contain metadata.  Metadata is declared using the same syntax as
metadata for data fields.  The example below adds a description to the field.

**Adding metadata to fields**
```
package alarm
 
import alarm.ZonedTime
 
model Clock {
    metadata {
        "description": "Determines the current time and periodically publishes the current time."
    }
     
    output {
        ZonedTime currentTime {
            "description": "Contains the current time and time zone."
        }
    }
}
```

Input fields are similar to output fields in that they consist of a data type and a field name except they are declared
within the `input` block of a model.  Our `Clock` model has no input fields, but assume the current time could be set.
It would be declared like so (note the need to use a different name):

**Input fields**
```note-info
If you're following along with this example you should not model the input. This is being used as an example only.
```

**Example inputs fields**
```
package alarm
 
import alarm.ZonedTime
 
model Clock {
    metadata {
        "description": "Determines the current time and periodically publishes the current time."
    }
 
    input {
      ZonedTime timeToSet
    }
     
    output {
        ZonedTime currentTime {
            "description": "Contains the current time and time zone."
        }
    }
}
```

# Modeling Behavior
We have declared that the `Clock` will produce `ZonedTime` data but we have declared under what
circumstances this data is generated.  This falls under modeling behavior.  The SD language uses the "given, when, then"
approach to modeling behavior.  Each model may contain any number of scenarios, which describe the model's behavior.
Each scenario is made up of multiple steps.  Steps come in 3 forms:

1. **given step**: A given step establishes a precondition for some behavior. (usually implies a component that contains state)
1. **when step:** A when step declared some kind of trigger or event that activates the behavior of the scenario.
1. **then step**: A then step is effectively an assertion that will be true once the scenario has been activated or triggered.  Then steps describe the results of the behavior.

All scenarios must contain at least one `when` step and one `then` step while `given` steps are optional.  Multiple
given, when, or then steps may be combined with the `and` keyword.  Steps themselves consists of verbs and arguments.
Arguments are specific to verbs.

In easiest to learn the syntax of scenarios by studying examples.  Our `Clock` model publish the current time every
second.  We can model by creating a scenario inside the model:

**Declaring scenarios**
```
package alarm
 
import alarm.ZonedTime
 
model Clock {
    metadata {
        "description": "Determines the current time and periodically publishes the current time."
    }
     
    output {
        ZonedTime currentTime {
            "description": "Contains the current time and time zone."
        }
    }
     
    scenario publishCurrentTime {
        when waiting 1 second
        then willPublish currentTime
    }
}
```

As show above, a scenario is declared using the `scenario` keyword.  All scenarios must have unique names.  In this
case, we named this scenario `publishCurrentTime`.  This scenario consists of two steps: a single `when` step and a
single `then` step.  The first step is the when step:

**Steps**
```
when waiting 1 second
```

The `when` keyword is used to declare when steps.  In this step, the step verb is `waiting`.  The 1 second parameter
declares the length of time that should pass before the scenario is triggered.  Step verb parameters are separated by
white space.  The values used for each step depend on the verb used.  Verbs are discussed more later.

The second step is the `then` step:

**Steps**
```
then willPublish currentTime
```

The `when`, `given`, and `then` keywords may only appear once in a scenario.  However, multiple steps can be combined
with the `and` keyword.  For example, we could declare that the clock only has 5 milliseconds to publish the current
time using the verb `willBeCompleted`.

**Combining steps with and**
```
scenario publishCurrentTime {
    when waiting 1 second
    then willPublish currentTime
     and willBeCompleted within 5 milliseconds
}
```

This scenario effectively tells us that the current time must be published every second and that it should take no more
than 5 milliseconds to publish the time once the second has elapsed.

Many types of scenarios do not have given steps but may contain many `then` steps.  Scenarios should avoid using
multiple `when` steps to trigger on different circumstances. Instead, break the scenario into separate scenarios.  Place
only enough information in the scenario to remove ambiguity .  Scenarios are later refined to executable test cases
which contain much more detail.  This will be covered in [Ch. 9]({{ safebase }}/books/modeling-with-the-system-descriptor/a-case-study.html).

# Scenario Verbs
Verbs form the heart of scenarios.  What follows a step keyword (`given`, `when`, and `then`) is always a verb.
Everything after the verb are space separated parameters.  Each individual verb has its own parameters.  Verbs come in
three forms: past tense, present tense, and future tense.

* **Past tense**: Verbs used in `given` statements use past tense since these are preconditions that should already have occurred or be true for the scenario to activate.  For example, `haveReceived`, `havePublished`
* **Present tense**: Verbs used in when statements use present tense since these are conditions or events that are occurring to trigger a scenario.  For example, `receiving`, `publishing`
* **Future tense**: Verbs used in then statements use future tense since these are conditions that will be true after the scenario has completed
    For example, `willPublish`, `willRequest`

Not all verbs support all tenses.  This means a verb can support one or more steps (i.e. it may support `when` statements
but not `then` statements or visa versa).

The SD Language contains some default basic verbs that any project case use.  A sample of these is given in the table
below.  A complete list can be found in [Appendix B]({{ safebase }}/books/modeling-with-the-system-descriptor/appendix-b.html).
Appendix B also covers how to create custom verbs.  The SD Language is designed to be extended in this way to allow
modelers the freedom to express behavior in their domain's own language.

| Verb     | Past (given)  | Present (when) | Future (then)   | Description 
|----------|---------------|----------------|-----------------|------------
| receive  | haveReceived  | receiving      | willReceive     | This verb is used to indicate a component will subscribe for some input.  The receipt of input triggers the scenario.
| publish  | havePublished | publishing     | willPublish     | This verb is used to indicate a component will publish some output.
| complete | hasCompleted  | completing     | willBeCompleted | This verb is used to place some timing constraint on a scenario.