Feature: ThreatEvaluation calculateTrackPriority

  Background: 
    Given all tracks have been dropped from the TrackPriorityService
    Given a csv file "com.ngc.seaside.threateval.SystemTrackData.csv" of SystemTrack objects

  Scenario: Multiple SystemTrack scenario
    Given the service has received the following SystemTrack objects:
      | id |
      |  0 |
      |  1 |
    When the service receives the following SystemTrack objects:
      | id |
      |  2 |
    Then a PrioritizedSystemTrackIdentifiers object should be published
    And the identifiers should be listed in this order:
      | prioritizedIds |
      |          2,0,1 |
    And the response should occur within 1 second

  Scenario: Multiple SystemTracks and a Classification
    Given the service has received the following SystemTrack objects:
      | id |
      |  0 |
      |  1 |
      |  2 |
    When the service receives a Classification object:
      | id | type |
      |  2 | FIN  |
    Then a PrioritizedSystemTrackIdentifiers object should be published
    And the identifiers should be listed in this order:
      | prioritizedIds |
      |          2,0,1 |
    And the response should occur within 1 second

  Scenario: SystemTracks, Classifications, and TrackEngagementStatuses
    Given the service has received the following SystemTrack objects:
      | id |
      |  0 |
      |  1 |
      |  2 |
      |  3 |
      |  4 |
    And the service has received the following Classification objects:
      | id | type      |
      |  0 | FISH_HEAD |
      |  2 | FIN       |
    And the service has received the following TrackEngagementStatus objects:
      | id | count | probability |
      |  0 |    10 |          .5 |
      |  1 |    24 |         1.0 |
    When the service receives a TrackEngagementStatus object:
      | id | count | probability |
      |  3 |    24 |        0.25 |
    Then a PrioritizedSystemTrackIdentifiers object should be published
    And the identifiers should be listed in this order:
      | prioritizedIds |
      |      3,0,2,4,1 |
    And the response should occur within 1 second
