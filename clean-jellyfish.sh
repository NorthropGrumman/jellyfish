#!/usr/bin/env bash

set -e -u -x

cd jellyfish-systemdescriptor-dsl \
&& gradle clean \
&& cd ../jellyfish-systemdescriptor \
&& gradle clean \
&& cd ../jellyfish-cli \
&& gradle clean
