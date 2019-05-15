## Scenarios within a feature file should be uniquely named
The name of a scenario in a feature file is the text following scenario. For example, this would invalid:

```
Feature: My feature

  Scenario: Say Hello
  When someone enters the room
  Then I will say hello

  Scenario: Say Hello
  When many people enter the room
  Then I will say hello to everyone
```