#
# UNCLASSIFIED
#
# Copyright 2020 Northrop Grumman Systems Corporation
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of
# this software and associated documentation files (the "Software"), to deal in
# the Software without restriction, including without limitation the rights to use,
# copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
# Software, and to permit persons to whom the Software is furnished to do so,
# subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
# INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
# PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
# HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
# OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
# SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
