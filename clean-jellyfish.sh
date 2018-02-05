#!/usr/bin/env bash

set -e -u -x

cd seaside-bootstrap-api \
&& gradle clean \
&& cd ../seaside-bootstrap \
&& gradle clean \
&& cd ../jellyfish-systemdescriptor-dsl \
&& gradle clean \
&& cd ../jellyfish-systemdescriptor \
&& gradle clean \
&& cd ../jellyfish-cli \
&& gradle clean
