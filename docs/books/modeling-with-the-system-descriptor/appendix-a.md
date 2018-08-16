---
restrictions: UNCLASSIFIED Copyright © 2018, Northrop Grumman Systems Corporation
title: Appendix A - System Descriptor Keywords
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Appendix B - Standard Scenario Verbs
next-page: appendix-b
prev-title: Ch. 11 Configuration Management
prev-page: configuration-management
---
{% include base.html %} 
# Structural Keywords
Below is a list of structural keywords.  Structural keywords are used to indicate the organization and structure of a
model, as opposed to its behavior.

| Keyword  | Description |
|----------|-------------|
| package | Declares the package that the model resides within.  The package should align to the decomposition of the system being modeled.  Packages may be nested by using the `.` character.
| import | Imports a data, model, or enum object so it be referenced.  Imports are needed to reference objects declared in other files.
| model | Declares an object of model type.  A model is a type of object that contains various attributes and metadata.
| data | Declares an object of a data type.
| enum | Declares an enumeration type; an enumeration contain simple string constant values and can be referenced in order data objects.
| extends | Declares a data type extends another data type.
| input | Field of a model, declares the data the component receives.
| output | Field of a model, declares the data the component may produce.
| requires | Field of a model, indicates the current model _requires_ access to the referenced model.
| parts | Field of a model, defines sub-components of a model.
| many | Used within data fields to indicate a field can have multiple values.
| metadata | Defines metadata for an object, field, or scenario. The contents of metadata should use [JSON](http://www.json.org/) syntax. Metadata can be attached to models, data, enums, fields, enum values, and scenarios.
| links | Defines how components are connected together.
| link ... `->` ... | Defines how a particular input or output of a component is connected to another component.


# Behavioral Keywords
Behavioral Keywords are used indicate how a model should behavior or perform a task.

| Keyword  | Description |
|----------|-------------|
| when     | Introduces a condition step or trigger to be tested that determines the scenario should execute.
| then     | Introduces an invariant step or post condition which may be asserted once the scenario has executed.
| scenario | Declares a discreet, atomic behavior a component should exhibit.
| given    | Introduces a pre-condition step that must be satisfied prior to the scenario being executable.
| and      | Logical operator AND. This is used to chain given, whenn and then statements together.

# Primitive Data Types
The following primitive data types are available for use when modeling data.

| Type    | Description |
|---------|-------------|
| int     | A generic integer.  Metadata may be used to describe the physical type and its representation when implementing the model. |
| float   | A generic real number.  Metadata may be used to describe the physical type and its representation when implementing the model. |
| boolean | A boolean (true/false) value.
| string  | A text value.  Metadata may be used to describe the physical type, local, and its representation when implementing the model. |