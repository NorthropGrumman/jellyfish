---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Ch. 3 A Lightweight Micro-Service Architecture for Java
book-title: Jellyfish User Guide
book-page: jellyfish-user-guide
next-title: Ch. 4 Running Jellyfish
next-page: running-jellyfish
prev-title: Ch. 2 A Simple Software Store System
prev-page: a-simple-software-store-system
---
{% include base.html %}

[Ch. 8]({{ safebase }}/books/modeling-with-the-system-descriptor/introduction-to-micro-service-architectures.html) of 
_Modeling with the System Descriptor_ provides an introduction to micro-services in general.  In this chapter, we'll 
explore a software framework written in Java that supports creating these types of applications.

# Architectures for Systems
Our `SoftwareStore` system contains four sub-components (which are modeled as _parts_).  We haven't declared how this 
system is deployed and how the sub-components are realized.  Before we can do, when need to explore some potential
Architectures

## Single Application
One architecture for the `SoftwareStore` system might deployed the system as a single application.  In this 
architecture, the entire system runs as a single process on a single host.  The sub-components might be implemented as
individual classes.  For example, a Java based application might implement each component as a Spring Bean, Guice POJO,
or OSGi Declarative Service component dependency.  This forms the simplest architecture must results in the least 
flexibility.  As we add more components, the application still runs as a single app or process.

## MSA
An alternative architecture might deploy the `SoftwareStore` system using micro-services.  In this style of deployment,
each sub-component is deployed as a seperate micro-service application and process.  This means that four processes must
be executing together to form the behavior of the `SoftwareStore`.  This also means that **no single component**
represents the entire `SoftwareStore` system.  All four services must be started and running for the system to be 
optional.

This provides several advanages as it allows different parts of the system to scale at different rates.  It also allows
each sub-component of the system be independently managed, developed, and maintained.

This is the default architecture Jellyfish perfers.  Jellyfish currently only generates for this style of architecture.
However, it's possible to extend Jellyfish with new commands that will generate an application using desired 
architecture.

# A Java MSA framework
In order to allow Jellyfish to generate code as simply as possible, the generated code makes heavy use of two 
infrastructure providers:
* the [BLoCS](https://github.ms.northgrum.com/BLoCS/blocs) project
* the [Starfish](https://github.ms.northgrum.com/CEACIDE/starfish) project

Both of these projects provide Java libraries which make it easier to develop modular micro-services.  These projects
use a framework called [OSGi](https://www.osgi.org/).  This technology introduces a powerful modularity layer into
Java.  Jellyfish generated micro-service run in an application stack that looks like this:

![application stack][stack]

This stack uses Java, OSGi, BLoCS, and Starfish to form the runtime environment for these services.

When we deploy the `SoftwareStore` system, we'll deploy four indepnendice micro-service apps, each running within its 
own self-contained stack.  The result would look something like this:

![software-store-msa][stack2]

**Using BLoCS and Starfish with other types of architectures**
```note-info
Jellyfish only generates code for micro-service architectures.  The generated code makes heavy use of BLoCS and Starfish
infrastructure.  These projects can be used by any type of architecture, not just an MSA.  This means that much of
Starfish and BLoCS infrastructure can be used by any application.
```

# Additional Resources
Consider reviewing the following resources before continuing.  This provides an overview of OSGi and some BLoCS
components.

* [Introduction to BLoCS](https://pages.github.ms.northgrum.com/BLoCS/blocs/wikis/introduction.html)
* [BLoCS Apps and Architecture](https://pages.github.ms.northgrum.com/BLoCS/blocs/wikis/blocs-apps-and-arch.html)
* [OSGi Primer](https://pages.github.ms.northgrum.com/BLoCS/blocs/wikis/osgi-primer.html)

The main documentation for BLoCS can be found at
[https://pages.github.ms.northgrum.com/BLoCS/blocs/docs.html](https://pages.github.ms.northgrum.com/BLoCS/blocs/docs.html).

[stack]: {{ safebase }}/assets/images/msa-layers.png
[stack2]: {{ safebase }}/assets/images/software-store-msa.png