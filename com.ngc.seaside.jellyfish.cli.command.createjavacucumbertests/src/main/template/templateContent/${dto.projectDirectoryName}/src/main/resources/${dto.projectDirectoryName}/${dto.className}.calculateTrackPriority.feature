Feature: ${dto.className} calculateTrackPriority

  Background:
    Given an absolute tolerance of 0.01

  Scenario Outline: Priority tests.
    The track priority should be calcuated as:
    priority = 1.0 - probabilityOfKill

    Given a TrackEngagementStatus object
    And the trackId is <id>
    And the plannedEngagementCount is <count>
    And the probabilityOfKill is <probability>
    When the TrackEngagementStatus object is received by the service
    Then the service should respond with a TrackPriority object
    And the trackId should be <id>
    And the sourceId should be "service:${dto.packageName}"
    And the priority should be <priority>

    Examples:
      | id | count | probability | priority |
      |  0 |    10 |         0.5 |      0.5 |
      |  1 |    24 |         1.0 |      0.0 |
      |  2 |  5737 |         0.0 |      1.0 |
      |  3 |    24 |        0.25 |     0.75 |

  Scenario Outline: Invalid probability
    Given a TrackEngagementStatus object
    When the TrackEngagementStatus object is received by the service
    And the probabilityOfKill is <probability>
    Then a fault is raised

    Examples:
      | probability |
      |          -1 |
      |    -0.00001 |
      |     1.00001 |
      | 1e500       |
      | 1e-500      |
