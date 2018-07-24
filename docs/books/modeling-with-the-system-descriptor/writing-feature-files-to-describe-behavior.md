---
title: Ch. 7 Writing Feature Files to Describe Behavior
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 8 Introduction to Micro Service Architectures
next-page: introduction-to-micro-service-architectures
prev-title: Ch. 6 Top Down Modeling - Decomposing Components
prev-page: top-down-modeling-decomposing-components
---
{% include base.html %} 
We now have a complete model of an alarm clock.  However, the alarm clock's behavior is only
defined at a high level.  There isn't enough detail to actually build such a component yet.

In this chapter, we'll learn how to further specify the behavior of a component by refining this behavior into _feature
files_. Feature files effectively form executable test cases for a component.  The objective of a feature file is to
provide an automated mechanism to ensure a product is compliant with its specification.  Models and feature files are
typically created in tandem, with modelers and test engineers working close together.  In general, the modeler is
concerned with the high level details of the normal cases and the test engineer ensures behavior for the off nominal
cases is also defined.

# Introduction To Gherkin
Features files are defined using the [Gherkin](https://github.com/cucumber/cucumber/wiki/Gherkin) language.  This language was made popular by the behavior driven
development product [Cucumber](https://cucumber.io/). From the Gherkin wiki:

> "Cucumber understands the language Gherkin. It is a Business Readable, Domain Specific Language that lets you describe
> software's behavior without detailing how that behavior is implemented."

Cucumber provides a way to _execute_ feature files written in the Gherkin syntax.  Feature files can be written by
almost anyone, regardless of their background.  Software developers then write code for each line in the feature file to
perform the logic of the test.  Cucumber then executes the feature file by invoking the appropriate code in sequence.

**Why Cucumber?**
```note-info 
You may ask, why do I need a tool like Cucumber, isn't this what unit test do? The answer
is no. Cucumber is intended to test a running application. This means that if you expect messages to be received in
order to perform some capability, your test code should send that message using the same mechanism your code expects to
receive it. Your test code should also assert that your code responds with the appropriate response message.
```

Gherkin is made up of a series of keywords.  Each line in a feature file must begin with an keyword.  Keywords can be
followed by any text.  This is what makes Gherkin so expressive.  The main keywords are listed below:
* `Feature`
* `Scenario`
* `Given`, `When`, `Then`, `And`, `But` (Steps)
* `Background`
* `Scenario Outline`
* `Examples`

**System Descriptor vs Gherkin**
```note-warning
Gherkin scenarios are not to be confused with System Descriptor scenarios, as they are separate entities.
```

A feature file contains one or more _scenarios_.  Each scenarios is made up of multiple steps.  Like the System
Descriptor scenario steps, these steps must start with either the `given`, `when`, or `then` keyword.  Below is an
example of a scenario pulled directly from the Cucumber documentation:

**Gherkin Scenarios**
```gherkin
Scenario: feeding a small suckler cow
 Given the cow weighs 450 kg
 When we calculate the feeding requirements
 Then the energy should be 26500 MJ
 And the protein should be 215 kg
```

It is also possible to create _scenario outlines_.  These scenarios uses tables to describe variables and values:

**Scenario Outlines**
```gherkin
Scenario Outline: feeding a suckler cow
  Given the cow weighs <weight> kg
  When we calculate the feeding requirements
  Then the energy should be <energy> MJ
  And the protein should be <protein> kg
 
  Examples:
    | weight | energy | protein |
    |    450 |  26500 |     215 |
    |    500 |  29500 |     245 |
    |    575 |  31500 |     255 |
    |    600 |  37000 |     305 |
```

More information about Gherkin can be found at https://cucumber.io/docs/reference and
https://github.com/cucumber/cucumber/wiki/Gherkin  Be sure to review this material before preceding.  

# Writing a Feature File for the Alarm Component
Recall the alarm components looks like this:

**Alarm.sd**
```
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

We'll start by refining the behavior of the `deactivateAlarm` scenario.  We define a feature file for each scenario
defined in a model.  This feature file usually has multiple Gherkin scenarios for testing both normal cases and edge
cases of that scenario.

As discussed earlier, model files reside in the `src/main/sd` directory of a System Descriptor (SD) project.  Gherkin
feature files reside in the `src/test/gherkin` directory of an SD project.  The feature files are placed in a directory
structure that mirrors the package structure of the feature file's corresponding model.  Since the `Alarm` component
resides in a package named alarm, the feature files for this component will be placed in the alarm directory inside the
`src/test/gherkin` directory.  Feature files are named using the following format: _modelName.scenarioName_.feature

Since we are creating a feature file for the `deactivateAlarm` scenario of the `Alarm` component, the name of the
feature file is `Alarm.deactivateAlarm.feature`.  The full path of this file within with the project is
`src/test/gherkin/alarm/Alarm.deactivateAlarm.feature`.  To create this file, first create the package/directory. Within
Eclipse, right click the `src/test/gherkin` directory and select **New** -> **Package**.  Enter the name alarm and
select OK. Right click the new package and select **New** -> **File**.  Name the file `Alarm.deactivateAlarm.feature`.

Feature files always start with the `Feature` keyword.  What follows is a description of the entire feature or SD 
scenario.

**Alarm.deactivateAlarm.feature**
```gherkin
Feature: Alarm deactivateAlarm
  The alarm should be deactivated when the alarm component receives an
  alarm acknowledgement message.
```

The file will contain multiple scenarios which begin with the `scenario` keyword.  A descriptor follows each each work.
The remaining lines are steps that form the scenario itself.  Our first scenario will handle the normal case of
deactivating an active alarm that has been acknowledged.

**Alarm.deactivateAlarm.feature**
```gherkin
Feature: Alarm deactivateAlarm
  The alarm should be deactivated when the alarm component receives an
  alarm acknowledgement message.
   
   
  Scenario: Deactivate an acknowledged an alarm.
    Given the alarm has published an AlarmStatus message
      And the alarmId field is 1
      And the active field is true
    When the alarm receives an AlarmAcknowledgement message
      And the alarmId field is 1
      And the alarmAcknowledged field is true
    Then the alarm will publish an AlarmStatus message
      And the alarmId field will be 1
      And the active field will be false
```

This scenario has a `given` steps, `when` steps, and `then` steps.  The given steps set up the component to publish an
`AlarmStatus` message with some fields set to the given values.  This sets up the component, so we can acknowledge an
active alarm.  Note that we didn't specify how the alarm got activated.  We'll leave that to the test implementer.  The
when steps result in the component receiving an `AlarmAcknowledgement` message to acknowledge the alarm.  Finally, the
then steps make sure the component publishes an `AlarmStatus` message for the alarm that was acknowledged and that the
alarm is no longer active.

The scenarios should also cover the off nominal scenarios:
* Do not deactivate an alarm is the alarmAcknowledge field of an acknowledgement is false.
* Publish a deactivated alarm status when an acknowledgement is received even if the alarm is not currently active.
* Do not reactivate a previously acknowledged alarm.

The complete feature file is given below:

**Alarm.deactivateAlarm.feature**
```gherkin
Feature: Alarm deactivateAlarm
  The alarm should be deactivated when the alarm component receives an
  alarm acknowledgement message.
   
   
  Scenario: Deactivate an acknowledged an alarm.
    Given the alarm has published an AlarmStatus message
      And the alarmId field is 1
      And the active field is true
    When the alarm receives an AlarmAcknowledgement message
      And the alarmId field is 1
      And the alarmAcknowledged field is true
    Then the alarm will publish an AlarmStatus message
      And the alarmId field will be 1
      And the active field will be false
   
   
  Scenario: Do not deactivate an alarm if the acknowledgement is false.
    Given the alarm has published an AlarmStatus message
      And the alarmId field is 2
      And the active field is true
    When the alarm receives an AlarmAcknowledgement message
      And the alarmId field is 2
      And the alarmAcknowledged field is false
    Then the alarm will publish an AlarmStatus message
      And the alarmId field will be 2
      And the active field will be true
   
   
  Scenario: Deactivate an alarm if an acknowledgement is received for an unknown
    alarm.
    When the alarm receives an AlarmAcknowledgement message
      And the alarmId field is 3
      And the alarmAcknowledged field is true
    Then the alarm will publish an AlarmStatus message
      And the alarmId field will be 3
      And the active field will be false
       
       
  Scenario: Do not reactivate a previously acknowledged alarm.
    Given the alarm has published an AlarmStatus message
      And the alarmId field is 4
      And the active field is true
      And the alarm has received an AlarmAcknowledgement message
      And the alarmId field is 4
      And the alarmAcknowledged field is true
      And the alarm has published an AlarmStatus message
    When the alarm receives an AlarmAcknowledgement message
      And the alarmId field is 4
      And the alarmAcknowledged field is false 
    Then the alarm will publish an AlarmStatus message
      And the alarmId field will be 4
      And the active field will be false
```

**Cucumber Language Support**
```note-info
Cucumber supports many different language. However, we shouldn't care which language
the test are written. For example, we successfully tested a C++ application using a Java implementation of the test.
Using a micro-service architecture (MSA) is a great way to help facilitate this type of flexibility. See the next
chapter for an introduction to MSA.
```

# Conclusion
Use feature files to help describe fine grained behavior of a component (nominal and off-nominal).  Feature files should
be **refined** from the scenarios defined in a component's model.  The scenarios defined in a feature file should
support complete autonomy and should have all the detail necessary to ensure the test can pass repeatedly.  The SD model
and the feature files together form a specification for a component or system.  Conflicts between model and feature
files should be resolved before the publishing the specification.