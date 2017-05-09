# jellyfish-systemdescriptor-dsl
SystemDescriptor-DSL for CEAC-295 Story

## Load the project in Eclipse Neon
1. Open Eclipse
1. Switch the workspace
..* File -> Switch Workspace -> Other...
..* Select **Browse...** and select or Make a New Folder for the Eclipse project files to reside.
..* **Important:** Do not select the cloned **jellyfish-systemdescriptor-dsl** directory
..* I used a directory called **eclipse-project** sitting at the same level as the above directory.
1. Import the existing project
..1. **File** -> **Import...**
..1. Expand **General**
..1. Select **Existing Projects into Workspace**
..1. Select **Search for nested projects** under *Options*
..1. Select **Next**
..1. Select **Browse...** and navigate to the **jellyfish-systemdescriptor-dsl** directory
..1. Select **Finish**

## Build the project
1. Expand com.ngc.seaside.systemdescriptor.parent project
1. Right click on **build.gradle**
1. Select **Run As -> Run Configurations**
1. Double click **Gradle Project**
1. It should create a *New_configuration* that you can rename next to Name:
1. Under *Gradle Tasks:* enter **clean build**
1. Select the **Workspace...** button and select the **com.ngc.seaside.systemdescriptor.parent** directory.
1. Select **Run**

## Running the Unit Test
1. Expand the **com.ngc.seaside.systemdescriptor.tests** project
1. Expand src/com.ngc.seaside.systemdescriptor.tests 
1. Right click **SystemDescriptorParsingTest.xtend**
1. Select **Run As** -> **Junit Test**
