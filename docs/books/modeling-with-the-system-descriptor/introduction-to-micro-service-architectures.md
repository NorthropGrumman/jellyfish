---
title: Ch. 8 Introduction to Micro Service Architectures
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 9 A Case Study - Modeling a Threat Evaluation System as an MSA
next-page: a-case-study
prev-title: Ch. 7 Writing Feature Files to Describe Behavior
prev-page: writing-feature-files-to-describe-behavior
---
{% include base.html %}
Micro-service architectures provide a means for software developers to create loosely coupled
software systems that can be modified, tested, and deployed quickly and safety.  In simplest terms, a micro-service is a
software component that "does one thing, and does it well".

The SD language was designed to allow complex systems to be decomposed into micro-services.  Models that represent
systems typically have _parts_.  These parts may be subsystems or micro-services.  Subsystems themselves are also refined
into micro-services.  Models that represent micro-services do not have parts.  They have inputs, outputs, and scenarios
which are all closely related to a single capability, feature, or function.  When a system is physically developed and
deployed, it consists only of micro-services; no physical components are built for the models that represent systems or
subsystems.  These items are purely logical constructions that help engineers reason about the system.  Since a complex
systems has many interaction services, _links_ are used to associate services together.  This makes it easier to trace
execution of the system as well as form various analysis or trade studies of modifications to the system.

Metadata can be used to indicate if a model is a model of a system or service.  The `stereotypes` metadata attribute is
used to categorize or _tag_ models.  This attribute may contain a list of values.  We use the value `service` to indicate
a model is a micro-service.  The value `system` is used to indicate a system is being modeled.

**Using the service stereotype**
```
model MyService {
  metadata {
    "stereotypes": ["service"]
  }
}
```

**Using the system stereotype**
```
model MySystem {
  metadata {
    "stereotypes": ["system"]
  }
}
```

We do not model beyond the micro-service level.  This helps establish a "definition of done" for modeling and lets
ensure our modeling efforts contribute the most value to the overall effort.

**Features Files for Services and Systems**
```note-info
We may write feature files for both services and systems.  This provides a means to test individual
services as well as integrated services.  Although no single physical component presents the entire system, we can use
the system feature files as test cases to exercise the entire system.
```

# The Service Checklist
Deciding when a component becomes an individual service or when multiple components should become a single service can
be challenging.  We provide a checklist to be used as a starting point and to help make this process easier.  This
checklist is not comprehensive and should not be followed verbatim.  Instead, it should be used to generate thoughts and
ideas about partitioning services.

Parnas(3) defines a module as an assignable unit of work.  For our case, we'll relax this definition and define a
module, `M`, as any bounded element of your system.  `M` can be an entire monolithic application, a traditional program
module or library, an existing service, etc.  What criteria do we use to decompose `M` into micro-services?  When do we
stop decomposing?

Let `M` be a module of any size.  We'll use the criteria below to determine if we should split `M` into separate parts
`A` and `B`.  Record the number of points for each question you answer with a yes.
1. **(5 points)** Does `M` change frequently?  Do changes to `M` cause frequent re-deployments? (1)
1. **(3 points)** Do `A` and `B` require significantly different computing resources?  Does `A` need lots of memory?  Is `B` algorithmically expensive?  Would I want to scale `A` and `B` differently? (1)
1. **(5 points)** Can `A` and/or `B` be developed by a separate team or developer?  Is it important that the other team be able to develop `A` or `B` as independently as possible? (3)
1. **(5 points)** Can `A` or `B` be used to hide a difficult design decision embedded within `M` that may change later?  Is there low or wavering confidence in this decision? (2)
1. **(3 points)** Is `M` conceptually challenging or complex?  Is it easier to reason about or comprehend the system by just studying `A` and `B` separately? (3)
1. **(3 points)** Are there other services that would use `B` but not `A`?  What about `A` but not `B`?  In other words, are `A` and `B` useful by themselves?
1. **(5 points)** Splitting `M` into `A` and `B` would not create a cycle in the dependency graph between `A` and `B`?  Are `A` and `B` are naturally in-cohesive and both do not depend on one another? (2)
1. **(3 points)** Would the interfaces for `A` and `B` still allow for an efficient implementation?  Would splitting `M` into `A` and `B` not have a overly negative impact on the performance of the system? (2)
1. **(5 points)** Do either `A` or `B` have multiple implementations, possibility in different languages?  
1. **(5 points)** Can `A` or `B` be deployed separately?  Can they be deployed in different types of environments?  Can one be deployed without the other?

If the total number of points is greater than 27 then the decomposition of `M` is not finished and `M` can be broken
down into `A` and `B`.  If the total number of points is less than 27, `M` is a micro-service and doesn't need to be
broken down any more.

| >= 27 | `M` is not a service, split it into `A` and `B`.
| < 27 | `M` is already a micoservice, don't split it into `A` and `B`.

We can go through this process again and set `M` = `A` to determine if we need to keep breaking `A` down.  The same
applies for `B`.  Eventually, we won't have a enough points and the decomposition ends.  Depending on the size and scope
of `M`, `A` and `B` could end up being micro-services after a single iteration.

# Existing Components and Functionality
There may be times when existing micro-services should be merged into a single service. Let `A` and `B` be any two
micro-services that we are considering to merge into a single service.

If the total number of points is greater than 12 then consider merging `A` and `B` into a single service.  Otherwise,
keep them separate.

| >= 12 | Merge `A` with `B`. 
| < 12 | Don't merge `A` and `B`. 

# New Components and Functionality
There may be cases where you don't have an existing module, but you aren't sure if you need to introduce a new component
into your system as a service or not.  Below are some use cases that support designing and deploying a component as a
service:
1. You want to hide or limit the use of a 3rd party dependency in your system.
1. You want to use the facade pattern to provide a single interface to multiple existing systems, libraries, or components.  

If you answered yes to any of the cases above, it's likely that your component should be a service.

# Conclusion
* Models of systems or subsystems typically have parts.  At some point, these parts become micro-services.  Modeling stops at this point.  Micro-services are the most fine grained components that we model.
* Models of micro services do not have parts or links.
* Use `stereotypes` to tag or mark models.  Modelers may use custom stereotypes.
* More information about micro-services can be found at https://12factor.net/.

# References
1. https://www.nginx.com/blog/refactoring-a-monolith-into-microservices/
1. Mucon_2016.pdf, https://speakerdeck.com/acolyer/on-the-criteria-for-decomposing-systems-into-microservices
1. _On the Criteria To Be Used in Decomposing Systems into Modules_, D.L. Parnas,  https://www.cs.umd.edu/class/spring2003/cmsc838p/Design/criteria.pdf