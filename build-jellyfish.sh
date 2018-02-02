#!/usr/bin/env bash

set -e -u -x

cd seaside-bootstrap-api \
&& gradle clean build install \
&& cd ../seaside-bootstrap \
&& gradle clean build install \
&& cd ../jellyfish-systemdescriptor-dsl \
&& gradle clean build install \
&& cd ../jellyfish-systemdescriptor \
&& gradle clean build install \
&& cd ../jellyfish-cli \
&& gradle clean build install
