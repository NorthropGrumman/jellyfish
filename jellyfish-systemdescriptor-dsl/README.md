# jellyfish-systemdescriptor-dsl
This is an Eclipse XText project which defines the System Descriptor language and generates the parser and Eclipse based tooling.  This project generates:
* the parser and code for creating an AST (abstract syntax tree) of System Descriptor files
* the validation logic and rules of the DSL
* content assists and Eclipse plugins for using Eclipse as a System Descriptor editor

Checkout [Modeling with the System Descriptor](http://10.207.42.137/confluence/display/SEAS/Modeling+with+the+System+Descriptor) for a detailed guide of language.

| Platform | Download Link |
|----------|---------------|
| Windows  | http://10.207.42.137/resources/jellyfish/eclipse-dsl-oxygen-2-win32-x86_64.zip |
| Linux    | http://10.207.42.137/resources/jellyfish/eclipse-dsl-oxygen-2-linux-gtk-x86_64 |

# Installation and Setup with Eclipse
Installation requires Eclipse and the update site.  Download Eclipse from the links below.  You can download the update site from [Releases](https://github.ms.northgrum.com/CEACIDE/jellyfish/releases).  Unzip the update site to any location you want.  **Ignore the com.ngc.seaside.systemdescriptor.updatesite files.  Those have been deprecated.** 

See [Ch. 2 Installation and Setup](http://10.207.42.137/confluence/display/SEAS/Ch.+2+Installation+and+Setup) for detailed instructions and images.  Alternatively, you can follow the quick setup below:
1. Select **Help**->**Install New Software**.
   1. Click **Add...** to create a new software site.
   1. Name the site System Descriptor (or anything you want).
   1. Select **Local...** and navigate to the directory where you unziped the update site ZIP downloaded above.
   1. Select **OK**.
1. Select the update site in the **Work with:** drop down above.
1. Unselect **Group items by category** at the bottom.
1. Check JellyFish SystemDescriptor DSL and select **Next** and follow the prompts.
1. Restart Eclipse to complete the installation.

**Note:** If errors occur during installation, verify your proxy settings are correct.  You can view these settings at Window -> Preferences -> General -> Network Connections.

# Creating a New System Descriptor Project
See [Ch. 2.2 Creating and Inital Project](http://10.207.42.137/confluence/display/SEAS/Ch.+2+Installation+and+Setup) for  instructions to create a new System Descriptor project.

# Building the Project
The project builds with Gradle.  Simply run the following command from the `jellyfish-systemdescriptor-dsl` directory of the repository.  Since the Gradle wrapper is used, Gradle will download itself.
```
./gradlew clean build 
```
This will build project, run the tests, and create the Eclipse update site.  The update site can be found at `com.ngc.seaside.systemdescriptor.updatesite/build`.  Note the Gradle build will download Eclipse if needed.  This is required to build the update site and can take a minute or so to actually complete the download.

# Importing the Project with Eclipse
The project can be imported into Eclipse as a Gradle project.  Make sure your have downloaded the version of Eclipse referenced in **Installation and Setup with Eclipse**.
1. Start Eclipse.
1. Select File -> Import.
1. Select Gradle -> Existing Gradle Project and click Next.
1. Click Next again and make sure the root directory is set to the `jellyfish-systemdescriptor-dsl` directory of the repository.
1. Click Finish to complete the import.  
Sometimes, Eclipse doesn't play well with the Gradle wrapper.  If an error happends during the import, download Gradle yourself, and set the **Local installation directory** field in the last screen to the location where you have already installed Gradle.  Click finish to try the import again.
