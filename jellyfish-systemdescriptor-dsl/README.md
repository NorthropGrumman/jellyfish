# jellyfish-systemdescriptor-dsl
This project defines the DSL uses to describe a System Descriptor.  This is an Eclipse Xtext project with generates
* the parser and code for creating an AST (abstract syntax tree)
* validation rules of the DSL
* plugin mechanisms for extending the DSL
* content assists and Eclipse plugins for using Eclipse as a System Descriptor editor

See http://10.207.42.42:8080/display/SEAS/JellyFish+DSL+Keywords for a description of the keywords used by the DSL and http://10.207.42.42:8080/display/SEAS/JellyFish+-+a+MBE+Solution+for+Microservice+Architectures for an overview of the System Descriptor concept.

# Quick Setup Guide for using the JellyFish System Descriptor Grammar
Follow this guide to avoid having to build the plugins yourself.  Use this if you just want to create new System Descriptor projects without updating the grammer.

Download Eclipse with DSL support from http://10.207.42.43/resources/jellyfish/eclipse-dsl-neon-2-win32-x86_64.zip. (TODO: Change to Oxygen)  Download the SystemDescriptor update site from http://10.207.42.43/resources/jellyfish/com.ngc.seaside.systemdescriptor.updatesite.zip. (TODO: Change to Oxygen) Unzip the update site to any location you want.

Start Eclipse ande **ensure your proxy settings are correct**.  To install the System Descriptor support:
1. Select **Help**->**Install New Software**.
   1. Click **Add...** to create a new software site.
   1. Name the site JellyFish (or anything you want).
   1. Select **Local...** navigate to the directory where you unziped the update site ZIP downloaded above.
   1. Select **OK**.
1. Select the update site in the **Work with:** drop down above.
1. Unselect **Group items by category** at the bottom.
1. Check JellyFish SystemDescriptor DSL and select **Next** and follow the prompts.

## Creating a new JellyFish project in Eclipse
Currently, until new JellyFish Eclipse plugins are created, a new blank project should be created to write SystemDescriptor
DSL code.  
1. Create a blank project by selecting **File**->**New Project**->**General/Project**.
1. Once the blank project is added, add a new *.sd* file by right clicking the project and select **New**->**File**. 
   1. Enter a filename that ends with *.sd* and click OK.  
   1. When prompted, select yes to convert it to an XText project.

If you want to convert a pre-existing project, right click the project, select **Configure** menu item, and then select **Convert to XText project**.  This will allow namespace scoping (i.e., imports) to compile properly.  It is expected that future JellyFish plugins for Eclipse will automatically setup.

# Building
The project can be built both inside Eclipse or outside of Eclipse with Gradle.  You can also run the Gradle build directly within Eclipse.

## Building outside of Eclipse
The DSL uses gradle to build.  Run the command ``gradle clean build`` from the root directory of the repo to build with gradle directly outside of Eclipse.

## Building the project in Eclipse Neon (TODO: Change to Oxygen)
1. Open Eclipse
1. Switch the workspace
   1. File -> Switch Workspace -> Other...
   1. Select **Browse...** and select or Make a New Folder for the Eclipse project files to reside.
   1. **Important:** Do not select the cloned **jellyfish-systemdescriptor-dsl** directory
   1. I used a directory called **eclipse-project** sitting at the same level as the above directory.
1. Import the existing project
   1. **File** -> **Import...**
   1. Expand **General**
   1. Select **Existing Projects into Workspace**
   1. Select **Search for nested projects** under *Options*
   1. Select **Next**
   1. Select **Browse...** and navigate to the **jellyfish-systemdescriptor-dsl** directory
   1. Select **Finish**

### Build the project
1. Expand com.ngc.seaside.systemdescriptor.parent project
1. Right click on **build.gradle**
1. Select **Run As -> Run Configurations**
1. Double click **Gradle Project**
1. It should create a *New_configuration* that you can rename next to Name:
1. Under *Gradle Tasks:* enter **clean build**
1. Select the **Workspace...** button and select the **com.ngc.seaside.systemdescriptor.parent** directory.
1. Select **Run**

### Running the Unit Test
1. Expand the **com.ngc.seaside.systemdescriptor.tests** project
1. Expand src/com.ngc.seaside.systemdescriptor.tests 
1. Right click **SystemDescriptorParsingTest.xtend**
1. Select **Run As** -> **Junit Test**

### Creating the JellyFish SystemDescriptor DSL Plugin for Eclipse
It is possible to create a plugin of the JellyFish SystemDescriptor DSL grammar for use in Eclipse to be able
to use the Eclipse editing capabilities.  The steps below are required because of an Eclipse bug which is documented
at https://bugs.eclipse.org/bugs/show_bug.cgi?id=278673.  Also, refer to http://infobart.com/category-not-showing-up-in-eclipse-update-site/ for a workaround that is defined in
the steps below.
1. Open the **com.ngc.seaside.systemdescriptor.updatesite** project
1. Delete the **features** and **plugins** folders as well as the **artifacts.jar** and **content.jar** files.
1. Open the site.xml file.  This should display an **Update Site Map** window panel.  If the xml file is opened in the text editor, select the **Site Map** tab.
   1. If you see **JellyFish.sd.category** (or any other item) in the list box of the panel, then delete it.
   1. Click on the **New Category** button and enter **JellyFish.sd.category** for the ID.  Enter **XText DSL** for the Name.
   1. Select the category just added and select the **Add Feature...** button
      1. From the Feature Selection dialog, select the **com.ngc.seaside.systemdescriptor.feature** project and select the **OK** button
   1. Select the **Build All** button to create the appropriate Plugin jars.
   
### Installing the plugins into an Eclipse installation
Follow this guide if you want to install the version of the plugin *you have just updated/built* for tested.
1. In the **Help** menu, select **Install New Software**
1. Click on the **Add...** button
1. Click the **Local...** button and traverse the file system tree to the location of the plugin.
1. Select the location to be the **jellyfish-systemdescriptor-dsl** git clone directory.
   1. The plugin to select is the **com.ngc.seaside.systemdescriptor.updatesite** project
1. Click on the **Ok** button to close the Add Repository dialog (you don't have to enter a Name)
1. In the list box panel, you should see **XText DSL**.  Select the check box and click the **Next >** button to install the plugin.

