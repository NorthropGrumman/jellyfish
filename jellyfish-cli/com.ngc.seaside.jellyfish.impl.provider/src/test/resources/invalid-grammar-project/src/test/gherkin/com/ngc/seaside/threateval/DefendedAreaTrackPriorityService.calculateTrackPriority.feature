#
# UNCLASSIFIED
# Northrop Grumman Proprietary
# ____________________________
#
# Copyright (C) 2019, Northrop Grumman Systems Corporation
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
