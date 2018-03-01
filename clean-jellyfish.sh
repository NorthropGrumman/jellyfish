#!/usr/bin/env bash

set -e -u -x

find . -name src-gen -type d | xargs rm -rf
find . -name xtend-gen -type d | xargs rm -rf

cd jellyfish-systemdescriptor-dsl \
&& gradle clean \
&& cd ../jellyfish-systemdescriptor \
&& gradle clean \
&& cd ../jellyfish-cli \
&& gradle clean
