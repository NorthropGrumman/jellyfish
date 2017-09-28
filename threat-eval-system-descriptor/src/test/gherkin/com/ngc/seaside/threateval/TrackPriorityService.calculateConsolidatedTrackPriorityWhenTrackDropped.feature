Feature: TrackPriorityService calculateConsolidatedTrackPriorityWhenTrackDropped

  Background:
    Given All tracks have been dropped

  Scenario Outline: Dropped tracks should result in the track not being listed in the 
    prioritized track list.
    Given the service has received the following TrackPriority objects:
      | trackId | sourceId                                                      | priority |
      |       0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.7 |
      |       2 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.1 |
      And a DroppedSystemTrack object exists
      And the trackId is <id>
     When the DroppedSystemTrack object is received by the service 
     Then the service should respond with a PrioritizedSystemTrackIdentifiers object
       And the identifiers should be listed in the order <prioritizedIds>
       And the response should occur within 1 second of receiving each message
      
      Examples: 
      | id | prioritizedIds |
      |  0 | 1,2            |
      |  1 | 0,2            |
      |  2 | 1,0            |
