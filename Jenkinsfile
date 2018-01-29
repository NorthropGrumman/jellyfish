pipeline {
    agent {
        label {
            label ''
            customWorkspace "${JENKINS_HOME}/workspace/jellyfish/${JOB_NAME}"
        }
    }

    stages {
        // The following stages actually build each project.

        stage('Build seaside-bootstrap-api') {
            steps {
                dir('seaside-bootstrap-api') {
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Build seaside-bootstrap') {
            steps {
                dir('seaside-bootstrap') {
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Build jellyfish-systemdescriptor-dsl') {
            steps {
                dir('jellyfish-systemdescriptor-dsl') {
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Build jellyfish-systemdescriptor-api') {
            steps {
                dir('jellyfish-systemdescriptor-api') {
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Build jellyfish-systemdescriptor-ext') {
            steps {
                dir('jellyfish-systemdescriptor-ext') {
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Build jellyfish-cli') {
            steps {
                dir('jellyfish-cli') {
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Build jellyfish-examples') {
            steps {
                dir('jellyfish-examples') {
                    sh 'chmod a+x ./gradlew'
                    sh './gradlew clean build install --stacktrace --continue'
                }
            }
        }

        stage('Build offline support') {
            steps {
                dir('seaside-bootstrap-api') {
                    sh './gradlew populateM2repo'
                }
                dir('seaside-bootstrap') {
                    sh './gradlew populateM2repo'
                }
                dir('jellyfish-systemdescriptor-dsl') {
                    sh './gradlew populateM2repo'
                }
                dir('jellyfish-systemdescriptor-api') {
                    sh './gradlew populateM2repo'
                }
                dir('jellyfish-cli') {
                    sh './gradlew populateM2repo'
                }
                dir('jellyfish-examples') {
                    // Just collect the dependencies for a single generated service since the dependencies are always
                    // the same.
                    sh './gradlew audit1'
                }
                dir('build') {
                    // Collect the m2 repository files inside a single ZIP.
                    sh 'zip -r dependencies-m2.zip dependencies-m2'
                }
            }
        }

        stage("Archive artifacts") {
            steps {
                // Create a ZIP that has everything.
                sh 'zip -j -r build/jellyfish-all.zip jellyfish-systemdescriptor-dsl/com.ngc.seaside.systemdescriptor.updatesite/build/com.ngc.seaside.systemdescriptor.updatesite-*.zip jellyfish-systemdescriptor-ext/com.ngc.seaside.systemdescriptor.ext.updatesite/build/com.ngc.seaside.systemdescriptor.ext.updatesite-*.zip jellyfish-cli/com.ngc.seaside.jellyfish/build/distributions/jellyfish-*.zip build/dependencies-m2.zip build/dependencies.tsv'

                // Archive the zip that has everything.
                archiveArtifacts allowEmptyArchive: true,
                                 artifacts: 'build/jellyfish-all.zip',
                                 caseSensitive: false,
                                 defaultExcludes: false,
                                 onlyIfSuccessful: true
            }
        }
    }
}
