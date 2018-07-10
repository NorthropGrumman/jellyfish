# jellyfish-cli-commands
Jellyfish-cli-commands consists of implementations to the jellyfish-cli, including implementations of the service api
and many commands dealing with model analysis and code generation directly from model files.

This project is packaged as a command line interface and as a set of Gradle plugins in the jellyfish-packaging project.

# Analysis
Analysis can be executed by running the `analyze` command. TODO TH: Add more

## analyze-inputs-outputs 
This analysis checks that models have at least one output.  See
[Ch. 1 Avoid components that have inputs but no outputs](http://10.166.134.55/confluence/display/SEAS/Ch.+1+Avoid+components+that+have+inputs+but+no+outputs)
for details
