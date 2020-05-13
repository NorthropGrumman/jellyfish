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
