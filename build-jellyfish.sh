#!/usr/bin/env bash

set -e -u -x

if [ $# -eq 0 ]
then
  params='clean build install'
else
  params="$@"
fi

echo "Using build parameters to '$params'"

cd ./jellyfish-systemdescriptor-dsl \
&& ../gradlew $params \
&& cd ../jellyfish-systemdescriptor \
&& ../gradlew $params \
&& cd ../jellyfish-cli \
&& ../gradlew $params \
&& cd ../jellyfish-cli-commands \
&& ../gradlew $params \
&& cd ../jellyfish-cli-analysis-commands \
&& ../gradlew $params \
&& cd ../jellyfish-packaging \
&& ../gradlew $params \
&& cd ../jellyfish-systemdescriptor-lang \
&& ../gradlew $params
