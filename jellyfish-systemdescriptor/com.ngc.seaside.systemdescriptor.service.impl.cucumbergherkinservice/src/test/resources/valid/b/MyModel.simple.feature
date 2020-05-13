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
