#!/bin/bash

# This script gets called from the Jenkinsfile.

source /etc/profile.d/rvm.sh
rvm use 2.5.1
bundle install
mkdir -p ../build
bundle exec jekyll build --destination ../build/site
cd ../build/site
zip -r site.zip .