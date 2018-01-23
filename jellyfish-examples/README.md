# jellyfish-examples

## Summary
This repo contains examples of System Descriptor models and regression tests for the jellyfish-cli.

## Running jellyfish-examples

### Basic execution

| Command                           | Description                                                       |
| --------------------------------- | ----------------------------------------------------------------- |
| `gradle build`                    | Runs all regression tests                                         |
| `gradle build --parallel`         | Runs all regression tests in parallel                             |
| `gradle build --continue`         | Runs all regression tests without stopping if some fail           |
| `gradle regression5`              | Runs the regression test with the name "5" (All *Regressions tasks have corresponding tasks for each regression test) |
| `gradle generateRegressions`      | Generates the output from jellyfish for all regression tests      |
| `gradle buildGeneratedRegressions`| Builds the generated output for all regression tests              |
| `gradle buildExpectedRegressions` | Builds the expected output for all regression tests               |
| `gradle buildRegressions`         | Builds the generated and expected output for all regression tests |
| `gradle updateExpectedRegressions`| Replaces the expected output with the generated output. This command can be used to update the expected examples after an update to jellyfish-cli has made breaking changes (This task is *not* run unless explicitly given) |
| `gradle audit5`                   | Runs the `m2repo` on the project generated for the regression test with the name "5" (All *Regressions tasks have a corresponding tasks for each test). This task is not run unless explicitly requested.  | 
