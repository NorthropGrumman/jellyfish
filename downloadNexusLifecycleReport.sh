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

SEARCH_STRING='The detailed report can be viewed online at'

REPORT_URL=$(grep "$SEARCH_STRING" $LOGFILE | tail -1 | awk '{print $NF}')

export no_proxy=".northgrum.com"

echo "Downloading Nexus Lifecycle PDF report at '${REPORT_URL}/pdf'"

curl -L -k -u "$USERNAME:$PASSWORD" "${REPORT_URL}/pdf" > $OUTPUT_DIRECTORY/Nexus-Lifecycle-Report.pdf