Feature: DefendedAreaTrackPriorityService calculateTrackPriority

  Background: 
    Given a csv file "SystemTrackData.csv" of SystemTrack objects

  Scenario Outline: Defeneded Area Priority list
    When a SystemTrack object is received by the service
    And the identifier is <id>
    And a request to the DefendedAreaService returns an impactProbability of <probability>
    Then the service should respond with a TrackPriority object
    And the trackId should be <id>
    And the priority should be <priority>
    And the sourceId should be com.ngc.seaside.threateval.DefendedAreaTrackPriorityService

    Examples: 
      | trackId | probability | priority |
      |       0 |          .2 |       .2 |
      |       1 |          .8 |       .8 |
      |       2 |          .1 |       .1 |

  Scenario Outline: Invalid probability
    When a request to the DefendedAreaService returns an impactProbability of <probability>
    Then a fault is raised

    Examples: 
      | probability |
      |          -1 |
      |    -0.00001 |
      |     1.00001 |
      | 1e500       |
      | 1e-500      |
