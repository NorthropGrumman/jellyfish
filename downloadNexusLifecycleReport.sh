#!/bin/sh

LOGFILE=$1
OUTPUT_DIRECTORY=$2
USERNAME=$3
PASSWORD=$4

if [ -z "$LOGFILE" ]
then
  echo 'You must pass in the path to the Jenkins log file'
  exit
fi
if [ -z "$OUTPUT_DIRECTORY" ]
then
  echo 'You must specify the output directory for the PDF report.'
  exit
fi

SEARCH_STRING='https://nexuslifecycle.ms.northgrum.com/ui/links/application/jenkins/report/'

BUILD_ID=$(grep $SEARCH_STRING $LOGFILE | tail -1 | awk -F $SEARCH_STRING '{print $2}')

export no_proxy=".northgrum.com"

echo "Downloading Nexus Lifecycle PDF report at '${SEARCH_STRING}${BUILD_ID}/pdf'"

curl -L -k -u "$USERNAME:$PASSWORD" "${SEARCH_STRING}${BUILD_ID}/pdf" > $OUTPUT_DIRECTORY/Nexus-Lifecycle-Report-$BUILD_ID.pdf