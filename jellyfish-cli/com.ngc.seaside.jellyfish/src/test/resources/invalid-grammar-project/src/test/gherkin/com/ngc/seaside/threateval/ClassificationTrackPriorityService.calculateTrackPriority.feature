Feature: ClassificationTrackPriorityService calculateTrackPriority

  Background: 
    Given An absolute tolerance of 1e-8

  Scenario Outline: Priority tests
    Given a Classification object is received by the service
    And the trackId is <id>
    And the objectType is <type>
    Then the service should respond with a TrackPriority object
    And the priority should be <priority>
    And the sourceId should be com.ngc.seaside.threateval.ClassificationTrackPriorityService

    Examples: 
      | id | type      | priority |
      |  0 | fish head |      0.9 |
      |  1 | backbone  |     0.75 |
      |  2 | fin       |      0.1 |
      |  3 | tail      |        0 |
