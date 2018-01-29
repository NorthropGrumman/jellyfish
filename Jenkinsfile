pipeline {
    agent {
        label {
            label ''
            customWorkspace "${JENKINS_HOME}/workspace/jellyfish/${JOB_NAME}"
        }
    }

    options {
        // We can use this resource to ensure only one build updates the mavenLocal repo at a time.
        // This is important because the last stage in this pipeline removes all snapshots from the local repo.  We
        // don't want to remove a snapshot another build are just installed but that other build isn't finish yet (ie,
        // the build needs to reference the snapshot).  This happens if a pipeline has multiple invocations of Gradle.
        lock resource: 'mavenLocal'
    }

    parameters {
        booleanParam(name: 'upload',
                     description: 'If true, artifacts will be uploaded to the build\'s remote repository.',
                     defaultValue: false)
        booleanParam(name: 'buildOfflineSupport',
                     description: 'If true, a maven2 repository will be created that can be used for offline deployments.',
                     defaultValue: false)
    }

    stages {
        // The following stages actually build each project.

        stage('Build seaside-bootstrap-api') {
            steps {
                dir('seaside-bootstrap-api') {
                    sh './gradlew clean build install --refresh-dependencies'
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
            when {
                expression { params.buildOfflineSupport }
            }
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
                dir('jellyfish-systemdescriptor-ext') {
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

        stage('Upload') {
            when {
                expression { params.upload }
            }
            steps {
                dir('seaside-bootstrap-api') {
                    sh './gradlew upload'
                }
                dir('jellyfish-systemdescriptor-dsl') {
                    sh './gradlew upload'
                }
                dir('jellyfish-systemdescriptor-api') {
                    sh './gradlew upload'
                }
                dir('jellyfish-systemdescriptor-ext') {
                    sh './gradlew upload'
                }
                dir('jellyfish-cli') {
                    sh './gradlew upload'
                }
            }
        }

        stage('Archive artifacts & cleanup') {
            steps {
                // Create a ZIP that has everything.
                sh 'mkdir -p build'
                sh 'zip -j -r build/jellyfish-all.zip jellyfish-systemdescriptor-dsl/com.ngc.seaside.systemdescriptor.updatesite/build/com.ngc.seaside.systemdescriptor.updatesite-*.zip jellyfish-systemdescriptor-ext/com.ngc.seaside.systemdescriptor.ext.updatesite/build/com.ngc.seaside.systemdescriptor.ext.updatesite-*.zip jellyfish-cli/com.ngc.seaside.jellyfish/build/distributions/jellyfish-*.zip build/dependencies-m2.zip build/dependencies.tsv'

                // Archive the zip that has everything.
                archiveArtifacts allowEmptyArchive: true,
                                 artifacts: 'build/jellyfish-all.zip',
                                 caseSensitive: false,
                                 defaultExcludes: false,
                                 onlyIfSuccessful: true

                // We do this to avoid keeping any snapshots in the local maven repo after the build.  When we run
                // 'gradle populateM2repo', snapshots may be inserted into that local maven repo.  Here is the problem:
                // since mavenLocal() is configured before nexus is all of our Gradle builds, the snapshot in the maven
                // repo will always be used by builds on the CI server.  This means that the Gradle will never download a
                // newer version of the snapshot from Nexus because the snapshot is always in maven local.  This is not a
                // problem when we do releases (since we don't use snapshots during releases) but it can be a problem when
                // building a development branch that is not finished yet.
                sh 'find ~/.m2/repository/ -type d -name \'*-SNAPSHOT\' | xargs rm -rf'
            }
        }
    }
}
