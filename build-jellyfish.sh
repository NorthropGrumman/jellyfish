#!/usr/bin/env bash

set -euo pipefail

params=${*:-'clean build publishToMavenLocal'}

echo "Using build parameters: '$params'"

directories=(
    "jellyfish-systemdescriptor-dsl"
    "jellyfish-systemdescriptor"
    "jellyfish-cli"
    "jellyfish-cli-commands"
    "jellyfish-cli-analysis-commands"
    "jellyfish-packaging"
    "jellyfish-systemdescriptor-lang"
)

for dir in "${directories[@]}"; do
    (
        cd "$dir"
        ../gradlew $params
    )
done
