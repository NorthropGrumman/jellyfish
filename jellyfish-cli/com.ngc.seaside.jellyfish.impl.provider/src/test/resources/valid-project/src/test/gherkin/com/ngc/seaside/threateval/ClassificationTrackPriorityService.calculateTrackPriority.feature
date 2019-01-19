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

Feature: ClassificationTrackPriorityService calculateTrackPriority

  Background: 
    Given An absolute tolerance of 1e-8

  Scenario Outline: Priority tests
    Given a Classification object is received by the service
    And the trackId is <id>
    And the objectType is <type>
    Then the service should respond with a TrackPriority object
    And the priority should be <priority>
    And the sourceId should be com.ngc.seaside.threateval.ClassificationTrackPriorityService

    Examples: 
      | id | type      | priority |
      |  0 | fish head |      0.9 |
      |  1 | backbone  |     0.75 |
      |  2 | fin       |      0.1 |
      |  3 | tail      |        0 |
