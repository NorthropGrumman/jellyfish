Feature: DefendedAreaTrackPriorityService calculateTrackPriority

  Background:
    Given a csv file "com.ngc.seaside.threateval.SystemTrackData.csv" of SystemTrack objects
    And an absolute tolerance of 0.01

  Scenario Outline: Defended Area Priority list

    Given a SystemTrack object
    And the identifier is <id>
    And the StateVector of the SystemTrack object is retrieved
    When the SystemTrack object is received by the service
    Then the service should respond with a TrackPriority object
    And the trackId should be <id>
    And the priority should be <priority>
    And the sourceId should be "service:com.ngc.seaside.threateval.DefendedAreaTrackPriorityService"

    Examples:
      | id | priority |
      |  0 |     0.50 |
      |  1 |     0.25 |
      |  2 |     1.00 |
      |  3 |     0.75 |

