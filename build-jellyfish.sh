#!/usr/bin/env bash

set -e -u -x

if [ $# -eq 0 ]
then
  params='build clean install'
else
  params="$@"
fi

echo "Using build parameters to '$params'"

cd ./jellyfish-systemdescriptor-dsl \
&& ../gradlew $params \
&& cd ../jellyfish-systemdescriptor \
&& ../gradlew $params \
&& cd ../jellyfish-cli \
&& ../gradlew $params
