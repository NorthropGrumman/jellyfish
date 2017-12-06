Feature: ClassificationTrackPriorityService calculateTrackPriority

  Background: 
    Given An absolute tolerance of 1e-8

  Scenario Outline: Priority tests
    Given a Classification object
    And the trackId is <id>
    And the objectType is <type>
    And the correlationEventId is "correlation-1"
    When the Classification object is received by the service
    Then the service should respond with a TrackPriority object
    And the priority should be <priority>
    And the sourceId should be "service:com.ngc.seaside.threateval.ClassificationTrackPriorityService"
    And the correlationEventId should be "correlation-1"

    Examples: 
      | id | type      | priority |
      |  0 | FISH_HEAD |      0.9 |
      |  1 | BACKBONE  |     0.75 |
      |  2 | FIN       |      0.1 |
      |  3 | TAIL      |      0.0 |
