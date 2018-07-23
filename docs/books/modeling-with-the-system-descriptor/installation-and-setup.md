---
title: Ch. 2 Installation and Setup
book-title: Modeling with the System Descriptor
book-page: modeling-with-the-system-descriptor
next-title: Ch. 3 The Basics of Modeling
next-page: the-basics-of-modeling
prev-title: Ch. 1 Introduction to the System Descriptor Language
prev-page: introduction-to-the-system-descriptor-language
---
{% include base.html %}
Tooling for System Descriptor language is provided in the form of a custom Eclipse installation.
Once installed, Eclipse can be used to create and edit SD models.  Two items must be downloaded to continue:
1. the base Eclipse installation for either [Linux]({{ site.eclipse_linux_download_link }}) or [Windows]({{ site.eclipse_windows_download_link }})
1. the [System Descriptor language update site]({{ safebase }}/index.html#get-jellyfish)

Note that the latest versions can be located on the
[GitHub releases page](https://github.ms.northgrum.com/CEACIDE/jellyfish/releases).

# Setting up Eclipse
1. Unzip the Eclipse zip to any location you wish.
1. Start Eclipse:
    * Windows: double click `eclipse.exe`.
    * Linux: cd to the directory in which you unzipped eclipse and enter `./eclipse`.
    * If Eclipse fails to start, see _Eclipse Troubleshooting_ below.
1. Choose any directory when Eclipse prompts for the location of its workspace.
    * *optional:* On the bottom right of the Welcome tab unselect Always show Welcome at start up and close the Tab.
1. Install the SD update site once eclipse has started.
    1. Select Help → Install New Software.
    1. Click Add... to create a new software site.
    1. Name the site System Descriptor Language Support (note this name can be anything).
    1. Select Archive...
    1. Navigate to the directory where the update site zip is located.
    1.  Select the com.ngc.seaside.systemdescriptor.updatesite-<version>.zip (note that <version> denotes the version you downloaded).
    1. Select Open.
    1. Select OK.
    1. **Unselect Group items by category at the bottom**
    1. Unselect Contact all update sites during install to find required software.
    1. Check JellyFish SystemDescriptor DSL.
    1. Select Next and follow the prompts.
1. Restart Eclipse when prompted.

![setting up eclipse][setup1]

## Eclipse Troubleshooting
If Eclipse fails to the start, it is usually because Eclipse is using an older version of Java.  It may be neccessary to
configure the Java executable Eclipse uses when starting.  In most cases, you can configure your system so a Java 8 JDK 
is installed and that JDK is listed _first_ on the `$PATH` envionrment variable.  If you cannot configure the `$PATH` 
for you system, you can edit the `eclipse.ini` file.  Add the lines below:

**eclipse.ini**
```
-vm
<PATH_TO_YOUR_JAVA_EXE>
```

These lines must occur direclty after the `openFile` line.  Include a full path to the `java.exe` executable.  Below is
a complete example.

**eclipse.ini**
```plaintext
-startup
plugins/org.eclipse.equinox.launcher_1.5.0.v20180512-1130.jar
--launcher.library
plugins/org.eclipse.equinox.launcher.win32.win32.x86_64_1.1.700.v20180518-1200
-product
org.eclipse.epp.package.dsl.product
-showsplash
org.eclipse.epp.package.common
--launcher.defaultAction
openFile
--launcher.defaultAction
openFile
-vm
C:/java/jdk1.8.xxx/bin/javaw.exe
--launcher.appendVmargs
-vmargs
-Dosgi.requiredJavaVersion=1.8
-Dosgi.instance.area.default=@user.home/eclipse-workspace
-XX:+UseG1GC
-XX:+UseStringDeduplication
--add-modules=ALL-SYSTEM
-Dosgi.requiredJavaVersion=1.8
-Dosgi.dataAreaRequiresExplicitInit=true
-Xms256m
-Xmx2024m
--add-modules=ALL-SYSTEM
```

## Cleaning the Project
Sometimes, Eclipse will show errors in files even if the files are syntacticallyd valid.  This is expecially true when
importing other types of data types.  When this happens,
1. Select **Project** -> **Clean**.
1. Select **Clean all projects**.
1. Clock **Clean**.
1. After cleaning, right click a project in the Package Explorer and select **Refresh**.

This should remove the errors.  If all else fails, restart Eclipse.

# Creating an Initial Project
Once Eclipse has a restarted an initial project can be created to make sure the installation was successful.  Make sure
the Package Explorer window is visible and follow the steps below.  If for some reason Package Explorer isn't shown
(or you accidentally close it) just select Window -> Show View -> Package Explorer in the menu.
1. Select File -> New -> Other in the menu.  You may also right click in the Package Explorer and select New -> Other.
1. Expand JellyFish
1. Select System Descriptor Project
1. Select Next.
1. Enter a project name of HelloWorld.
    1. Leave Use default location checked
1. Select Finish.

![setting up eclipse][setup2]

A new project should appear in the project explorer. 
1. Expand the `HelloWorld` project and expand `src/main/sd`
1. Expand the item labeled `com/ngc/mysdproject` 
1. Double click `MyModel.sd` to open the example model

Its contents should look something similar to this:

**MyModel.sd**
```
package com.ngc.mysdproject
 
model MyModel {
  metadata {
    "name" : "MyModel",
    "description" : "MyModel description",
    "stereotypes" : ["model", "example"]
  }
}
```
This indicates that the tooling has been successfully installed.

[setup1]: {{ safebase }}/assets/images/eclipse-install-1.png
[setup2]: {{ safebase }}/assets/images/eclipse-install-2.png