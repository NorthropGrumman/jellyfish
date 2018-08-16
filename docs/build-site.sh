#!/bin/bash

#  UNCLASSIFIED
#  Northrop Grumman Proprietary
# ____________________________
#
#   Copyright © 2018, Northrop Grumman Systems Corporation
#   All Rights Reserved.
#
#  NOTICE:  All information contained herein is, and remains the property of
#  Northrop Grumman Systems Corporation. The intellectual and technical concepts
#  contained herein are proprietary to Northrop Grumman Systems Corporation and
#  may be covered by U.S. and Foreign Patents or patents in process, and are
#  protected by trade secret or copyright law. Dissemination of this information
#  or reproduction of this material is strictly forbidden unless prior written
#  permission is obtained from Northrop Grumman.

# This script gets called from the Jenkinsfile.

source /etc/profile.d/rvm.sh
rvm use 2.5.1
bundle install
mkdir -p ../build
bundle exec jekyll build --destination ../build/site
cd ../build
zip -r site.zip site