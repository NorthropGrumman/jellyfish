Feature: EngagementTrackPriorityService calculateTrackPriority

  Background: 
    Given a relative tolerance of 0.01

  Scenario Outline: Priority tests.
    The track priority should be calcuated as:
	  priority = 1.0 - probabilityOfKill
  	When a TrackEngagementStatus object is received by the service
  	And the trackId is <id>
  	And the plannedEngagementCount is <count>
  	And the probabilityOfKill is <probability>
    Then the service should respond with a TrackPriority object
    And the trackId should be <id>
    And the sourceId should be com.ngc.seaside.threateval.EngagementTrackPriorityService
	And the priority should be <priority>

    Examples: 
      | id | count | probability | priority |
      |  0 |    10 |         0.5 |      0.5 |
      |  1 |    24 |         1.0 |      0.0 |
      |  2 |  5737 |         0.0 |      1.0 |
      |  3 |    24 |        0.25 |     0.75 |

  Scenario Outline: Invalid probability
    When a TrackEngagementStatus object is received by the service
    And the probabilityOfKill is <probability>
    Then a fault is raised

    Examples: 
      | probability |
      |          -1 |
      |    -0.00001 |
      |     1.00001 |
      | 1e500       |
      | 1e-500      |
