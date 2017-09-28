Feature: TrackPriorityService calculateConsolidatedTrackPriority

  Background:
    Given All tracks have been dropped

  Scenario Outline: Service responds with default values.
    Given a TrackPriority object exists
      And the trackId is <id>
      And the sourceId is <sourceId>
      And the priority is <priority>
    When the TrackPriority object is received by the service
    Then the service should respond with a PrioritizedSystemTrackIdentifiers object
      And the identifiers should be listed in the order <prioritizedIds>
      And the response should occur within 1 second of receiving each message
      
    Examples: 
      | id | sourceId                                                      | priority | prioritizedIds |
      |  0 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |      0.5 |              0 |
      |  1 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |      0.9 |              1 |
      |  2 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |     0.25 |              2 |
      

  Scenario Outline: Messages from the same source for the same track should override previous values.
    Given the service has received the following TrackPriority objects:
      | trackId | sourceId                                                      | priority |
      |       0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |     0.25 |
      And a TrackPriority object exists
      And the trackId is <id>
      And the sourceId is <sourceId>
      And the priority is <priority> 
     When the TrackPriority object is received by the service
     Then the service should respond with a PrioritizedSystemTrackIdentifiers object
       And the identifiers should be listed in the order <prioritizedIds>
       And the response should occur within 1 second of receiving each message  
 
     Examples: 
      | id | sourceId                                                      | priority | prioritizedIds |
      |  1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.1 |            0,1 |
      |  1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.9 |            1,0 |

    
  Scenario Outline: Track priorities from different sources should be averaged together and then
    sorted so that the tracks with the highest priority are ordered first.
    Given the service has received the following TrackPriority objects:
      | trackId | sourceId                                                      | priority |
      |       0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       0 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |      0.4 |
      |       0 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |     0.33 |
      |       1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       1 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |     0.75 |
      |       1 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |     0.33 |
      |       2 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.1 |
      |       2 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |     0.25 |
      |       2 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |      0.4 |
      And a TrackPriority object exists
      And the trackId is <id>
      And the sourceId is <sourceId>
      And the priority is <priority> 
     When the TrackPriority object is received by the service
     Then the service should respond with a PrioritizedSystemTrackIdentifiers object
       And the identifiers should be listed in the order <prioritizedIds>
       And the response should occur within 1 second of receiving each message
      
      Examples: 
      | id | sourceId                                                      | priority | prioritizedIds |
      |  0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |     0.95 |          0,1,2 |
      |  1 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |     0.20 |          0,1,2 |
      |  2 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |      1.0 |          1,2,0 |
      |  1 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |      0.0 |          0,1,2 |
    
    
  Scenario Outline: Priorities from the same source should override previous values,
    even for different tracks.
    Given the service has received the following TrackPriority objects:
      | trackId | sourceId                                                      | priority |
      |       0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.7 |
      |       2 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.1 |
      And a TrackPriority object exists
      And the trackId is <id>
      And the sourceId is <sourceId>
      And the priority is <priority> 
     When the TrackPriority object is received by the service
     Then the service should respond with a PrioritizedSystemTrackIdentifiers object
       And the identifiers should be listed in the order <prioritizedIds>
       And the response should occur within 1 second of receiving each message
      
      Examples: 
      | id | sourceId                                                      | priority | prioritizedIds |
      |  0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |     0.95 |          0,1,2 |
      |  1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |     0.20 |          0,1,2 |
      |  2 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      1.0 |          2,1,0 |
      |  1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.0 |          0,2,1 |
    
  
  Scenario Outline: If the consolidate priority is the same for multiple tracks, the 
    IDs of the track should be used to sort the tracks with the same priorities. 
    Given the service has received the following TrackPriority objects:
      | trackId | sourceId                                                      | priority |
      |       0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       0 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |      0.4 |
      |       0 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |     0.33 |
      |       1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       1 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |      0.4 |
      |       1 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |     0.33 |
      |       2 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       2 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |      0.4 |
      |       2 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |     0.33 |
      And a TrackPriority object exists
      And the trackId is <id>
      And the sourceId is <sourceId>
      And the priority is <priority> 
     When the TrackPriority object is received by the service
     Then the service should respond with a PrioritizedSystemTrackIdentifiers object
       And the identifiers should be listed in the order <prioritizedIds>
       And the response should occur within 1 second of receiving each message
       
    Examples: 
      | id | sourceId                                                      | priority | prioritizedIds |
      |  0 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |      0.9 |          0,1,2 |
      |  1 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |      0.9 |          1,0,2 |
      |  2 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.9 |          2,0,1 |
