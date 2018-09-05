#
# UNCLASSIFIED
# Northrop Grumman Proprietary
# ____________________________
#
# Copyright (C) 2018, Northrop Grumman Systems Corporation
# All Rights Reserved.
#
# NOTICE:  All information contained herein is, and remains the property of
# Northrop Grumman Systems Corporation. The intellectual and technical concepts
# contained herein are proprietary to Northrop Grumman Systems Corporation and
# may be covered by U.S. and Foreign Patents or patents in process, and are
# protected by trade secret or copyright law. Dissemination of this information
# or reproduction of this material is strictly forbidden unless prior written
# permission is obtained from Northrop Grumman.
#

Feature: TrackPriorityService getPrioritizedSystemTracks

  Scenario: Priority list
    Given The service has received the following TrackPriority objects:
      | trackId | sourceId                                                      | priority |
      |       0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       1 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |     0.75 |
      |       2 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |     0.25 |
      |       0 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |      0.4 |
      |       3 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.9 |
      |       1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.8 |
      |       0 | com.ngc.seaside.threateval.EngagementTrackPriorityService     |     0.33 |
      |       2 | com.ngc.seaside.threateval.DefendedAreaTrackPriorityService   |     0.15 |
    When A request is sent to the service
    Then The service should respond with an ordered set of SystemTrackIdentifier objects
    And The ordered set of SystemTrackIdentifier objects should be [1, 3, 2, 0]
    And The response should occur within 1 second of the request

  Scenario: Priority list, classification only
    Given The service has received the following TrackPriority objects:
      | trackId | sourceId                                                      | priority |
      |       0 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.5 |
      |       1 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.3 |
      |       2 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.1 |
      |       3 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.7 |
      |       4 | com.ngc.seaside.threateval.ClassificationTrackPriorityService |      0.8 |
    When A request is sent to the service
    Then The service should respond with an ordered set of SystemTrackIdentifier objects
    And The ordered set of SystemTrackIdentifier objects should be [4, 3, 0, 1, 2]
    And The response should occur within 1 second of the request

  Scenario: Priority list with duplicate priorities
    Given The service has received the following TrackPriority objects:
      | trackId | sourceId                                                  | priority |
      |       0 | com.ngc.seaside.threateval.EngagementTrackPriorityService |      0.5 |
      |       1 | com.ngc.seaside.threateval.EngagementTrackPriorityService |      0.5 |
      |       2 | com.ngc.seaside.threateval.EngagementTrackPriorityService |      0.1 |
      |       3 | com.ngc.seaside.threateval.EngagementTrackPriorityService |      0.7 |
      |       4 | com.ngc.seaside.threateval.EngagementTrackPriorityService |      0.5 |
      |       5 | com.ngc.seaside.threateval.EngagementTrackPriorityService |      0.7 |
    When A request is sent to the service
    Then The service should respond with an ordered set of SystemTrackIdentifier objects
    And The ordered set of SystemTrackIdentifier objects should be [3, 5, 0, 1, 4, 2]
    And The response should occur within 1 second of the request
