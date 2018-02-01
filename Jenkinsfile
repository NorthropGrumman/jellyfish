pipeline {
    agent {
        label {
            label ""
            customWorkspace "${JENKINS_HOME}/workspace/jellyfish-test/${JOB_NAME}"
        }
    }

    parameters {
        string(name: 'TARGET_BRANCH',
               defaultValue: 'SEA18-11',
               description: 'The branch to checkout.')
        booleanParam(name: 'PERFORM_RELEASE',
                     defaultValue: false,
                     description: 'If true, a release build will be performed.')
    }

    stages {
        // The following stages actually build each project.

        stage("PrepareForRelaseBuild") {
            steps {
               dir('seaside-bootstrap-api') {
                  script {
                     if (params.PERFORM_RELEASE ==~ /(?i)(Y|YES|T|TRUE|ON|RUN)/) {
                        sh "./gradlew tasks"
                        sh "./gradlew removeVersionSuffix"
                        sh "./gradlew createReleaseTag"
                     }
                  }
               }
            }
        }

        stage("Build seaside-bootstrap-api") {
            steps {
                dir('seaside-bootstrap-api') {
                    sh "./gradlew clean build install"
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage("Build seaside-bootstrap") {
            steps {
                dir('seaside-bootstrap') {
                    sh "./gradlew clean build install"
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage("Build jellyfish-systemdescriptor-dsl") {
            steps {
                dir('jellyfish-systemdescriptor-dsl') {
                    sh "./gradlew clean build install"
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage("Build jellyfish-systemdescriptor-api") {
            steps {
                dir('jellyfish-systemdescriptor-api') {
                    sh "./gradlew clean build install"
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage("Build jellyfish-systemdescriptor-ext") {
            steps {
                dir('jellyfish-systemdescriptor-ext') {
                    sh "./gradlew clean build install"
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage("Build jellyfish-cli") {
            steps {
                dir('jellyfish-cli') {
                    sh "./gradlew clean build install"
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage("Build jellyfish-examples") {
            steps {
                dir('jellyfish-examples') {
                    sh 'chmod a+x ./gradlew'
                    sh "./gradlew clean build install --stacktrace --continue"
                }
            }
        }
        stage("Release") {
            when {
                expression { env.BRANCH_NAME == 'SEA18-11' && params.PERFORM_RELEASE ==~ /(?i)(Y|YES|T|TRUE|ON|RUN)/ }
            }
            steps {
               dir('seaside-bootstrap-api') {
                  sh "./gradlew upload"
                  sh "./gradlew bumpTheVersion"
                  sh "./gradlew releasePush"

               }
            }
        }
        stage("Archive") {
            steps {
             archiveArtifacts(allowEmptyArchive: true,
                              artifacts: 'jellyfish-systemdescriptor-dsl/com.ngc.seaside.systemdescriptor.updatesite/build/com.ngc.seaside.systemdescriptor.updatesite-*.zip, jellyfish-systemdescriptor-ext/com.ngc.seaside.systemdescriptor.ext.updatesite/build/com.ngc.seaside.systemdescriptor.ext.updatesite-*.zip, jellyfish-cli/com.ngc.seaside.jellyfish/build/distributions/jellyfish-*.zip',
                              caseSensitive: false,
                              defaultExcludes: false,
                              onlyIfSuccessful: true)
            }
        }
    }
}
