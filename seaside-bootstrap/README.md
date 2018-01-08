# Bootstrap
The bootstrap project provides a command line tool for scaffolding new gradle projects.

# Quickstart
1. Build the source
   1. clone the repository 
      * git clone https://github.ms.northgrum.com/CEACIDE/starfish-bootstrap.git
   1. cd to the *starfish-bootstrap/com.ngc.seaside.starfish.bootstrap* directory
   1. Run **gradlew build clean**
1. Add the script to your PATH
   1. The build/distributions directory contains a tar and zip file
      1. Uncompress one of these files to your location of choice (outside the build directory)
      1. Add the bin directory to your PATH variable
1. Run the script
   1. cd to the location you wish to create a new project
   1. Run **com.ngc.seaside.starfish.bootstrap bundle -o ./**
      1. Note: this creates a "bundle" using the template in the resources/config/ directory in your unzipped directory 

# References
For a more detailed look at the usage of this script and extending the functionality to meet your needs see the links below:
* [How to create a new project using the bootstrap script](http://10.207.42.42:8080/display/SEAS/How+to+create+a+new+project+using+the+Bootstrap+script)
* [How to create a project boostrap template](http://10.207.42.42:8080/display/SEAS/How+to+create+a+project+bootstrap+template)

