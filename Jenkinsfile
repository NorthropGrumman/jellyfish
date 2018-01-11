pipeline {
                agent {
                    label {
                        label ""
                        customWorkspace "${JENKINS_HOME}/workspace/${JOB_NAME}"
                    }
                }
    
                stages {
                    // The following stages actually build each project.
                    
                    stage("Build seaside-bootstrap-api") {
                        steps {
                            dir('jellyfish/seaside-bootstrap-api') {
                                sh "./gradlew clean build install"
                                junit '**/build/test-results/test/*.xml'
                            }
                        } 
                    }
                    stage("Build seaside-bootstrap") {
                        steps {
                            dir('jellyfish/seaside-bootstrap') {
                                sh "./gradlew clean build install"
                                junit '**/build/test-results/test/*.xml'
                            }
                        } 
                    }
                    stage("Build jellyfish-systemdescriptor-dsl") {
                        steps {
                            dir('jellyfish/jellyfish-systemdescriptor-dsl') {
                                sh "./gradlew clean build install"
                                junit '**/build/test-results/test/*.xml'
                            }
                        } 
                    }
                    stage("Build jellyfish-systemdescriptor-api") {
                        steps {
                            dir('jellyfish/jellyfish-systemdescriptor-api') {
                                sh "./gradlew clean build install"
                                junit '**/build/test-results/test/*.xml'
                            }
                        } 
                    }
                    stage("Build jellyfish-systemdescriptor-ext") {
                        steps {
                            dir('jellyfish/jellyfish-systemdescriptor-ext') {
                                sh "./gradlew clean build install"
                                junit '**/build/test-results/test/*.xml'
                            }
                        } 
                    }
                    stage("Build jellyfish-cli") {
                        steps {
                            dir('jellyfish/jellyfish-cli') {
                                sh "./gradlew clean build install"
                                junit '**/build/test-results/test/*.xml'
                            }
                        } 
                    }
                    stage("Build jellyfish-examples") {
                        steps {
                            dir('jellyfish/jellyfish-examples') {
                                sh 'chmod a+x ./gradlew'
                                sh "./gradlew clean build install --stacktrace --continue"
                            }
                        } 
                    }
                    stage("Archive artifacts") {
                        steps {
                            archiveArtifacts allowEmptyArchive: true,
                                             artifacts: 'jellyfish/jellyfish-systemdescriptor-dsl/com.ngc.seaside.systemdescriptor.updatesite/build/com.ngc.seaside.systemdescriptor.updatesite-*.zip, jellyfish/jellyfish-systemdescriptor-ext/com.ngc.seaside.systemdescriptor.ext.updatesite/build/com.ngc.seaside.systemdescriptor.ext.updatesite-*.zip, jellyfish/jellyfish-cli/com.ngc.seaside.jellyfish/build/distributions/jellyfish-*.zip',
                                             caseSensitive: false,
                                             defaultExcludes: false,
                                             onlyIfSuccessful: true
                        }
                    }
                }
            }