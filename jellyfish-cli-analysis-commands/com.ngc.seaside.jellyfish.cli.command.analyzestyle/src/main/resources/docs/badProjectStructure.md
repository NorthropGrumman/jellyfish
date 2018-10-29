## System Descriptor projects should follow the standard folder structure
The standard folder structure for a System Descriptor project includes the following:

* All System Descriptor files (`.sd`) should be in the folder `<project-folder>/src/main/sd`
  * Each `.sd` file should have the same folder structure as its package; e.g., a model `ExampleModel` with the package
    `com.example.test` should be located at `<project-folder>/src/main/sd/com/example/test/ExampleModel.sd`
* All Gherkin feature files (`.feature`) should be in the folder `<project-folder>/src/test/gherkin`
  * Each `.feature` should have the same folder structure as the model that it refers to; e.g., the feature file for
    the aforementioned `ExampleModel` should be located at
    `<project-folder>/src/test/gherkin/com/example/test/ExampleModel.exampleScenario.feature`

See [Follow the standard project structure and naming conventions](http://10.166.134.55/confluence/display/SEAS/Ch.+5+Follow+the+standard+project+structure+and+naming+conventions)
for more information.
