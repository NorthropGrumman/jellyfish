#set( $D = '$' )
#!/bin/sh
#
#
#  Northrop Grumman Proprietary
#  ____________________________
#
#   Copyright (C) 2017, Northrop Grumman Systems Corporation
#   All Rights Reserved.
#
#  NOTICE:  All information contained herein is, and remains the property of
#  Northrop Grumman Systems Corporation. The intellectual and technical concepts
#  contained herein are proprietary to Northrop Grumman Systems Corporation and
#  may be covered by U.S. and Foreign Patents or patents in process, and are
#  protected by trade secret or copyright law. Dissemination of this information
#  or reproduction of this material is strictly forbidden unless prior written
#  permission is obtained from Northrop Grumman.
#


# Run this script to start the application.

export SCRIPT_DIRECTORY="${D}( cd "${D}( dirname "${D}{BASH_SOURCE[0]}" )" && pwd )"
export NG_FW_HOME=`dirname "${D}SCRIPT_DIRECTORY"`
export EQUINOX_OPTS="-clean -console -consoleLog"

# Require JAVA_HOME to be set.
: ${D}{JAVA_HOME:?"Environment variable JAVA_HOME not set!  This variable must be set."}

"${D}JAVA_HOME/bin/java" "-DNG_FW_HOME=${D}NG_FW_HOME" -Dgov.nasa.worldwind.app.config.document=${D}NG_FW_HOME/resources/config/app/map/worldwind.xml -Djavax.xml.accessExternalSchema=all -jar "${D}NG_FW_HOME/platform/equinox-osgi-3.10.0.jar" ${D}EQUINOX_OPTS