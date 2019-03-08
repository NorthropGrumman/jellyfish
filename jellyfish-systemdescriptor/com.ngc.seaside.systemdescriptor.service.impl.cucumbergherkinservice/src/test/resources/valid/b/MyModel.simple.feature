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

@my-feature-tag
Feature: A simple example

   This is a longer description

   Background:
      Given the room has been built

   @example1
   Scenario: Say hello

      This scenario is a test for greeting someone.

      Given Bob is in the room
      And the lights are on
      When Adam enters the room
      Then Bob should greet Adam

   Scenario: A table example
      When this table is created
         | First Name | Last Name |
         | Bob        | Smith     |
         | Adam       | Smith     |
      Then the API should have a representation of it
      And this docstring should also have a representation
      """
      I don't seem to use this
      construct that ofter.  I wonder what we would
      use this for?
      """

   @example2 @an-outline
   Scenario Outline: Say hello to lots of people

      Given Bob is in the room
      And the lights are on
      When <person> enters the "room"
      Then Bob should greet <person> by saying <greeting>

      Examples:
         | Person  | Greeting |
         | Adam    | Hello    |
         | Charles | Hi       |
