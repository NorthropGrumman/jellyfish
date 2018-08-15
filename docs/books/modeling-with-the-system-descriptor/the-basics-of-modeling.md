---
restrictions: UNCLASSIFIED Copyright (C) 2018, Northrop Grumman Systems Corporation
title: Ch. 3 The Basics of Modeling
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 4 Modeling Data
next-page: modeling-data
prev-title: Ch. 2 Installation and Setup
prev-page: installation-and-setup
---
{% include base.html %}
Modeling with the System Descriptor (SD) language is simple: files are created inside a
directory which represents a project.  A project directory has the following structure:

**The structure of an SD project**
```
- project directory
  - src
    - main
      - sd
      - resources
    - test
      - gherkin
      - resources
```

By convention, the name of the project is also the name of the directory.  Project names should not contain whitespace
or special characters.  When a new project is created with Eclipse, a following directory strucuture is automatically 
generated:
* `src/main/sd`: the location of all the System Descriptor (SD) files (i.e. the model).
* `src/main/resources`: contains extra files that may be referenced by models or external parser.
* `src/test/gherkin`: contains text files which are referred to as a feature files.  These files describe fine grained behavior and describe how to test each component.
* `src/test/resources`: contains files that may be referenced by features files.  In many cases, this directory contains data files that my be used during a test.

# Model Elements and Packages
As mentioned previously, a System Descriptor project is just a set of text files that end in the .sd extension.  All of
these files share a common structure:
* a `package` declaration (1)
* a set of optional `import` statements that make it possible to reference other elements
* the declaration of an element, which is ether a **model**, **data type**, or **enumeration** (2)

**An Example Model (AlarmClock.sd)**
```
// (1)
package myfirstmodel

// (2)  
model AlarmClock {
}
```

The name of the file should match the name of the element declared within (minus the `.sd` extension).  As shown in (1),
all files must declare which package the described element is contained in.  This package must match the name of the
directory that contains the file.  Given these rules, the contents above would be located in a file named
`AlarmClock.sd`. This file would be located within the directory `myfirstmodel`.  The full path of the file within the
project directory would be `src/main/sd/myfirstmodel/AlarmClock.sd`.

Packages may contain other packages which are referred to as sub-packages.  In this case, package names are separated
with the '`.`' character.  A file should list all of its packages, including sub-packages.  In the example above, the
`AlarmClock` model could be moved to the package `my.first.model`:

**Using nested packages**
```
package my.first.model
  
model AlarmClock {
}
```

This indicates that `AlarmClock` is contained in the package `model` which is contained in the package `first`.  `first`
is contained in the package `my`.  The resulting file path for such an file would be
`src/main/sd/my/first/model/AlarmClock.sd.`  Take care to ensure directory structures match package names.  This is
handled automatically when creating new packages or elements with the Eclipse JellyFish wizards (select **New** ->
**Other...** then expand **JellyFish** to see your options.  See "Using Eclipse to Create Packages" for more information.

# Types of Model Elements
The SD language had 3 primitive types of elements:
* **model**: Models are the most common types of elements.  Models are simple constructs that can represent both real (ie,
  hardware) and logical components (software).  Models can also represent construct of any complexity.  Both top level
  and decomposed elements are represented both as models.
* **data**: Data types describe the structure of data.  Models typically have data inputs and outputs.  By modeling this
  data, we help define the public interface of components.  Data types contain fields and may be composed of other data
  types.
* **enum**: Enumerations are the simplest element and are used to represent a finite group of values.  Enumerations may be
  referenced (ie, imported) by data types but not by models.

Models are denoted with the `model` keyword.  Data types used the data keyword as show below.

**An example data type**
```
package my.first.datatype
  
data DateTime {
}
```

Likewise, enumerations use the `enum` keyword.  Enumerations may only contain values:

**An example enumeration**
```
package my.first.datatype
  
enum TimeZone {
  EST
  CST
  MST
  PST
}
```

Finally, single line comments start with `//` and multiline comments use `/* */`.  Comments can occur anywhere.

**Using comments**
```
package my.first.datatype
 
// This is a single line comment.
data DateTime {
  /* This is a multiline comment.
     This comment won't end until the
     symbol below is reached.
   */
}
```

The string that follows each keyword is the name of element.  **Each element in a package must have a unique name.** 

# Fully Qualified Names
In many cases, elements may be referred to by their _fully qualified names_.  An element's fully qualified name is the
name of the element prefixed with the package name.  The `.` character is used to separate the element name and package
name.  More examples are given below:

| Package           | Name       | Fully Qualified Name
|-------------------|------------|---------------------------
| my.first.model    | AlarmClock | my.first.model.AlarmClock
| my.first.datatype | DateTime   | my.first.datatype.DataType
| my.first.datatype | TimeZone   | my.first.datatype.TimeZone

# Using Eclipse to Create Packages
[Ch. 2]({{ safebase }}/books/modeling-with-the-system-descriptor/installation-and-setup) covers the details of creating
a new project with Eclipse.  The same wizard can be used to create new elements and packages.  To create a new package,
1. Right click the project in the Package Explorer and select New -> Other.  If creating a sub-package, right click the parent package.
1. Select System Descriptor Package under Jellyfish. 
1. Select Next.
1. Enter the name of the package and select Finish. 

![creating a new SD package][create]

Models, data types, and enums can be created by right click the package they they should reside in.  Use the same
process described above but select **System Descriptor File** instead.

# Conventions
Below are some standard conventions that can be followed when creating SD models:\
* The name of a project should match the directory that contains the project.  Project names should not contain 
whitespaces.
* Package names are lowercase and contain no underscores.  Use sub-packages to denote organization and containment.
* Element names (i.e. the names of models, data, and enums) use UpperCamelCase notation.
* The default formatter for the SD language uses Kernighan and Ritchie style notation for braces.  Intentions consist of
  3 spaces.

[create]: {{ safebase }}/assets/images/create-project.png