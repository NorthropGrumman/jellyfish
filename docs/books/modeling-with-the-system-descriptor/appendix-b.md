---
restrictions: UNCLASSIFIED Copyright (C) 2018, Northrop Grumman Systems Corporation
title: Appendix B - Standard Scenario Verbs
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Appendix C - The Jellyfish Command Line Interface
next-page: appendix-c
prev-title: Appendix A - System Descriptor Keywords
prev-page: appendix-a
---
{% include base.html %}
Verbs are used to create scenarios.  Verbs come in three forms: past tense, present tense, and
future tense.  Past tense verbs are used in given steps, present tense verbs are used in when steps, and future tense
verbs are used in then steps.  Below is a list of currently supported verbs.  New verbs can also be created.  Creating
new verbs will be covered in
[Appendix C - The Jellyfish Command Line Interface]({{ safebase }}/books/modeling-with-the-system-descriptor/appendix-c.html).

# Messaging
These verbs deal with messaging and managing input and output.

## Receive

| Past | Present | Future | Messaging Paradigm |
|------|---------|--------|--------------------|
| haveReceived | receiving | willReceive | Pub/sub |

This verb is used to indicate a component will subscribe for some input.  The receipt of input triggers the scenario.
Multiple steps combined with and indicate the scenario may be triggered if any of the inputs are received.  Its general
form is:

**Verb format**
```
(haveReceived|receiving|willReceive) <inputField>
```

## Publish

| Past | Present | Future | Messaging Paradigm |
|------|---------|--------|--------------------|
| havePublished | publishing | willPublish | Pub/sub |

This verb is used to indicate a component will publish some output.  This is most often used in then steps to indicate a
scenario will publish some value.  Its general form is:

**Verb format**
```
(havePublished|publishing|willPublish) <outputField>
```

## Request 

| Past | Present | Future | Messaging Paradigm |
|------|---------|--------|--------------------|
| haveRequested | requesting | willRequest | Request/response|

This verb is used to indicate a component will make a request to another component.  This is most often used in `then`
steps to indicate a scenario will "invoke" another scenario from another component.  This verb is used to model the
client's workflow as opposed to the server.  Its general from is:

**Verb format**
```
(haveRequested|requesting|willRequest) <partOrRequiredField> to <scenarioFromReferencedModel> (with <inputField>)?
```

## ReceiveRequest  

| Past | Present | Future | Messaging Paradigm |
|------|---------|--------|--------------------|
| haveReceivedRequest | receivingRequest | willReceiveRequest | Request/response|

This verb is used to indicate a component receive a request from another component.  This is most often used in `when`
steps to trigger a scenario when a request is received.  This verb is used to model the server's workflow as opposed to
the client.  Its general from is:

**Verb format**
```
(haveReceivedRequest|receivingRequest|willReceiveRequest) <inputField>
```

## Respond   

| Past | Present | Future | Messaging Paradigm |
|------|---------|--------|--------------------|
| haveResponded | responding | willRespond | Request/response|

This verb is used to indicate a component that has received a request is responding to the request and thus end the
request.  This is most often used in `then` steps to indicate what the response to a request is.  This verb is used by a
component that is acting as a server in a request/response workflow. 

**Verb format**
```
(haveResponded|responding|willRespond) with <outputField>
```

# Correlation and Data Aggregation
These verbs deal with managing and correlating data.

## Correlate    

| Past | Present | Future | Messaging Paradigm |
|------|---------|--------|--------------------|
| haveCorrelated | correlating | willCorrelate | Pub/sub|

This verb is used to indicate multiple pieces of data must be correlated together.  It is most often used in `when`
steps but may also be used in `then` steps.  This verb only supports two arguments at a time but the verb can be used in
multiple steps combined together with `and`.

When the present tense form of the verb is used in a `when` step, the referenced data should be correlated together
using the rule in the step.  When the future tense form of the verb is used in a `then` step, the referenced data should
be copied from the input to the output.

**Verb format**
```
(haveCorrelated|correlating|willCorrelate) <inputField|outputField>(.<dataField>)+ to <inputField|outputField>(.<dataField>)+
```

Note that an output field can only be referenced in a then step and that the only one field in the then step can
reference an output.

# Performance
These verbs deal with declaring performance constraints or other non-functional requirements.

## Complete 

| Past | Present | Future |
|------|---------|--------|
| hasCompleted | completing | willBeCompleted |

This verb is used to place some timing constraint on a scenario.  This is most often used in `then` steps to indicate a
scenario has some max time to complete.  Its general form is:

**Verb format**
```
(hasCompleted|completing|willBeCompleted) (within|atLeast) <duration:double> (nanoseconds|milliseconds|microseconds|seconds|minutes|hours|days)
```