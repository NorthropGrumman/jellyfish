## Feature files should be associated with a model and scenario
Feature files, typically located in `src/test/gherkin` of a system descriptor project, should be associated with a
model and a scenario, and vice versa. This means that the folder path of the feature file relative to
`src/test/gherkin` should correspond with a model's package name. The name of the feature file should have the format
`<model-name>.<scenario-name>.feature`.

For example, consider a model with package `com.ngc.example`, a name of `ExampleModel`, and scenario `doSomething`.
There should be a feature file with the path `src/test/gherkin/com/ngc/example/ExampleModel.doSomething.feature`. 