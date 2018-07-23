---
title: Ch. 1 Introduction to the System Descriptor Language
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 2 Installation and Setup
next-page: installation-and-setup
---
{% include base.html %}

The System Descriptor (SD) is a Architecture-as-Code construct modeled after the DevOps Infrastructure-as-Code paradigm
that streamlines flow from Development to Operations.  The System Descriptor streamlines flow from System Engineering to
Development.  The System Descriptor is a collection of machine and human readable plain-text files that enables new
constructs for maintaining vertical (i.e., its top-down structure) and horizontal (i.e., how components interact with
one another) traceability, architecture and design quality assessment, and automation of system acceptance test. This is
all to increase predictability and reduce risk within a small-batch Agile program execution environments.

The System Descriptor language is essentially an architectural domain specific language or DSL used to describe the
structural and behavioral characteristics of a system and will more than likely trace to other types of engineering
artifacts (requirements, logical and physical architectures, etc). An example model of a simple alarm clock is given
below:

**An Example of a Simple SD Model**
```
package my.first.model
 
import my.first.model.Speaker
 
model AlarmClock {
  input {
    AlarmTime alarmType
  }
 
  parts {
    Speaker speaker
  }
 
  scenario triggerAlaram {
  }
}
```

The example above provides an introduction to the syntax of the language.  Each of the items in the model will be
covered in later chapters.

# Why Model with the System Descriptor Language?
Ambiguity between the system engineering teams and software engineering teams is an unfortunate reality for most
engineering efforts.  Ambiguity can lead to conflicts between these teams, reduce product quality, and profoundly
increases cost. This ambiguity can be attributed to the different level of abstraction between those who specify the
requirements and architectures of systems and those who breakdown and implement those requirements.  A rising trend to
help eliminate this ambiguity is the rise of Model Based Engineering (MBE) techniques throughout industry. MBE practices
provide the system engineering team a way to improve their precision and efficiency in communication with fellow Systems
Engineers and other team members.  The System Descriptor is a modeling artifact defined by its rigor of construction.
That is, the descriptor may contain no ambiguity: it must clearly and concisely communicate the intent of the system
components to a product development team.  The SD language differs from visual languages used in other MBE tools (such
as SysML and UML) in that it is designed to be a simple language that is consumed directly by software, without the need
of additional transformations.  This machine readable nature of the descriptor ensures that all ambiguity of behavior
and interactions be absent.

The SD language does not compete with other modeling techniques; rather it augments these techniques to help ensure the
artifacts produced drive downstream engineering efforts.  In some instances, it can be useful to first derive high level
designs in visual tool such as SysML and refine these designs in a System Descriptor.  This refinement process reveals
ambiguity in the original visual model and forces the modeler to resolve this ambiguity to proceed.

# Architecture-as-Code
Recent innovations within the software industry often center around the "Infrastructure-as-Code" paradigm.  Products
like Docker, Puppet, and Ansible exemplify this concept.  Infrastructure-as-Code involves the use of DSLs, scripts, or
"code" to describe the IT infrastructure of an organization, team, or software product.  For example, Docker uses
Dockerfiles to describe how to configure the operating environment for a software application as well as how to execute
that application.  The files that describe this infrastructure are both human and machine readable.  This allows an
organization's infrastructure to be configuration managed like the software it develops and deploys.  This techniques
reduces cost by enabling more automation and allows innovation to be less risky.

One of the founding tenets of modern day DevOps practices is the idea of increasing flow from product development to
operations.  Basically, deploy smaller product changes quicker and more often.  The techniques utilized by
Infrastructure-as-Code streamline the process from development to operations.  Environments are automatically,
accurately, and quickly created.  Applications are deployed with the correct parameters and configurations.  Duplicate
environments and instances can be easily created.  These capabilities are usually encapsulated within a continuous
pipeline.  This pipeline requires the "coded" infrastructure so it can determine how to build and deploy these
environments and applications.

The SD language builds upon this idea to create an "Architecture-as-Code" approach to system engineering and modeling.
By utilizing this "Architecture-as-Code" technique, the process from systems engineering to development can likewise be
improved.  The SD model describes how the system is to be decomposed, the responsibility of the system, how the system
will be tested, and how the system is deployed.  This makes it possible to create another type of pipeline from system
engineering to development.  This pipeline can only be possible if there exists some model that describes the design of
the system. The System Descriptor model is therefore the source artifact from which numerous other types of artifacts
can be created. These artifacts can be generated and evaluated as part of a pipeline and include but are not limited to:
* Documentation
* Boilerplate software infrastructure and configuration
* Compliance verification artifacts