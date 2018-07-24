---
title: Ch. 4 Modeling Data
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 5 Modeling a Simple Component
next-page: modeling-a-simple-component
prev-title: Ch. 3 The Basics of Modeling
prev-page: the-basics-of-modeling
---
{% include base.html %}
The System Descriptor (SD) language contains constructs for modeling data.  Modeling data is essential to defining the
public interface or contracts of components.  Once data types have been created, they may be referenced by models.

We'll start by creating data types for a model of a basic alarm clock application for a smart phone.  We'll use this
example for the next several chapters as we explore some of the capabilities of the SD language.  Our alarm clock will
consist of these basic software components:
* A clock that is responsible for determining the current time
* A component that is responsible for managing alarm times and triggering alarms
* A UI component that shows the current time

Althought SD language can also be used to model hardware, we'll keep this example simple and just worry about software.

In order to create this model, we first need an a project.  Start Eclipse and follow the instructions in [Ch. 2]({{
safebase }}/books/modeling-with-the-system-descriptor/installation-and-setup) to create a new project.  Place the
project in an existing workspace (any workspace will do) and name the project `AlarmClock`.  Delete the package
`com.ngc.mysdproject` as we will replace it with our own packages.

# Creating Data Types to Represent Time
In order to model the alarm clock, we need to model the data that flows through the system.  We don't have to model all
the data up front; we just need enough data to define the interface for the first component.  First, we need to create
data types that represent a moment in time as well as a date.
1. Right click the directory structure `src/main/sd` and select **New** -> **Other.**..
1. Expand **JellyFish**
1. Select **System Descriptor File**. 
1. Select **Next**. 
1. Use a **Package** name of alarm and a **Name** of Time. 
1. Select **Data** and 
1. Select **Finish**. 
Eclipse will open the file Time.sd for editing.  Its contents should look something like this:

**Time.sd**
```
package alarm
 
data Time {
}
```

Data types consists of _fields_.  Fields contain a type and a unique name.  We'll add fields for hour, minute and second
to the time structure.  These fields will have a type of integer which is denoted with the `int` keyword:

**Time.sd**
```
package alarm
 
data Time {
   int hour
   int minute
   int second
}
```

Each field must be listed on a separate line.  These fields are considered primitive fields because they reference a
built in primitive type.  The SD language has support for the following primitive types:
* int
* float
* string
* boolean

The `many` keyword can also be used to declare that a field has multiple values (i.e. a list or array of values).  The
`many` keyword appears before the type:

**The many keyword**
```
package examples
 
data Vector {
   many int components
}
```
The data we are currently modeling for the AlarmClock project has no need for the `many` keyword, so we omit it.

# Enumerations
It would be useful to be able to represent the time zone that is associated with the time.  If we limit the set of time
zones to North America, there are only 4 possible values.  We'll use an enumeration to model time zones.  As mentioned
previously, the `enum` keyword can be used to represent this construct.  Create a new file named `TimeZone` in the
`alarm` package.
1. Right click the alarm package
1. Select **New** -> **Other...**
1. Expand **JellyFish**
1. Select **System Descriptor File**
1. Enter **TimeZone** in the **Name** field
1. Select **Enum**
1. Select **Finish**

**TimeZone.sd**
```
package alarm
 
enum TimeZone {
    EST
    CST
    MST
    PST
}
```

Enumerations are made up of values which may be separated by newlines (as shown in the example) as well as spaces or
commas.  Any of the examples below would be valid:

**Alternative 1**
```
package alarm
 
enum TimeZone {
    EST,
    CST,
    MST,
    PST
}
```

**Alternative 2**
```
package alarm
 
enum TimeZone {
    EST CST MST PST
}
```

# Metadata
The SD language includes support for metadata in order for modelers to include problem specific information in models.
All metadata is represented as [JSON](http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-404.pdf).  JSON
allows for objects to be defined as key/value pairs.

**Example JSON**
```javascript
{
  "greeting": "hello world"
}
```

JSON objects are contained in braces ('{', '}').  Keys and value are separated by semi-colons (:).  Keys are strings and
must be surrounded in double quotes.  Strings used as values must also be surrounded in double quotes.  Multiple values
or arrays are surrounded with square brackets ('[', ']') with commas separating elements in the array.

**Example JSON Array**
```javascript
{
  "greetings": ["hello world", "hola", "konchiwa"]
}
```

Finally, JSON objects may contain nested objects or arrays of objects:

**Example of nested JSON objects**
```javascript
{
  "greetings": [{
      "language": "English",
      "phrase": "hello world"
    }, {
      "language": "Spanish",
      "phrase": "hola"
    }, {
      "language": "Japanese",
      "phrase": "konchiwa"
  }]
}
```
Numerical values should not be enclosed in quotes.

Data types declare metadata using the `metadata` keyword after declaring the data type.  For example, we could add a
description to the `Time` data type:

**Time.sd**
```
package alarm
 
data Time {
    metadata {
        "description": "Represents a moment in time."
    }
 
    int hour
    int minute
    int second
}
```

Modelers are free to define metadata however they please.  Some metadata have standard support, such as for specifying
validation details.  This type of metadata is applied directly to fields as shown below:

**Time.sd**
```
package alarm
 
data Time {
    metadata {
        "description": "Represents a moment in time."
    }
 
    int hour {
    	metadata {
           "validation": {
              "min": 0,
              "max": 23
           }
        }
    }
    int minute {
    	metadata {
           "validation": {
              "min": 0,
              "max": 59
           }
        }
    }
    int second {
    	metadata {
           "validation": {
              "min": 0,
              "max": 59
           }
        }
    }
}
```

Metadata for fields is declared inside a `metadata { }` block after the field is declared.  Adding validation and other
types of metadata does not have an impact on the actual model.  Rather, this information helps provide additional
semantics for tooling that may reference the model to generate additional artifacts.

# Data Composition and Inheritance
Data types can extend other data types to form a "is a" relationship.  When data type `A` extends another data type `B`,
`A` inherits all the fields declared in `B`.  `B` is referred to as the base type.  The syntax for this example is given
below. Data types declare they extend other data types by using the `extends` keyword followed by the name of the data
type they are extending.  The base type must be imported.  Data types can extend at most one data type.  Enumeration can
not extend other types or be extended.

**Data inheritance**
```
package example
 
import example.B
 
import A extends B {
  ...
}
```

The language allows for data to be compromised of other complex data types.  We'll update our example to create a new
data type, `ZonedTime` that extends `Time` and uses composition to declare a new field of the enumeration type 
`TimeZone`.

**Data composition with inheritance**
```
package alarm
 
import alarm.Time
import alarm.TimeZone
 
data ZonedTime extends Time {
    TimeZone timeZone
}
```

The `timeZone` field has a type of `TimeZone`.  Note the need to import the `TimeZone` with `import alarms.TimeZone`.
Even types that reside in the same package must be imported.  Other data types and enumerations can be referenced in
this way.

The final `ZonedTime` type has the following fields:
* hour (inherited)
* minute (inherited)
* second (inherited)
* timeZone

Data inheritance and composition can be used to generate advanced data structures which may be referenced as inputs or
outputs of models which is covered in [Ch. 5]({{ safebase }}/books/modeling-with-the-system-descriptor/modeling-a-simple-component).