//test

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
                     description: 'If true, artifacts will be uploaded to the build\'s remote repository.  Don\'t use this option with performRelease.',
                     defaultValue: false)
        booleanParam(name: 'performRelease',
                     defaultValue: false,
                     description: 'If true, a release build will be performed.  Releases can only be performed from master.')
        booleanParam(name: 'offlineSupport',
                     description: 'If true, a maven2 repository will be created that can be used for offline deployments.',
                     defaultValue: false)
		booleanParam(name: 'nexusLifecycle',
                     description: 'If true, Nexus Lifecycle will scan for security issues.',
                     defaultValue: true)			 
    }

    stages {
        // Prepare for a release if necessary.
		
		
        stage("Prepare For Release") {
            when {
                expression { params.performRelease }
            }
            steps {
                dir('seaside-bootstrap-api') {
                    sh 'chmod a+x ./gradlew'
                    sh './gradlew removeVersionSuffix --refresh-dependencies'
                    sh './gradlew createReleaseTag'
                }
            }
        }
	
        // The following stages actually build each project.
        stage("Build seaside-bootstrap-api") {
            steps {
                dir('seaside-bootstrap-api') {
                    sh 'chmod a+x ./gradlew'
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        
        stage('Build seaside-bootstrap') {
            steps {
                dir('seaside-bootstrap') {
                    sh 'chmod a+x ./gradlew'
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        
        stage('Build jellyfish-systemdescriptor-dsl') {
            steps {
                dir('jellyfish-systemdescriptor-dsl') {
                    sh 'chmod a+x ./gradlew'
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        
        stage('Build jellyfish-systemdescriptor') {
            steps {
                dir('jellyfish-systemdescriptor') {
                    sh 'chmod a+x ./gradlew'
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Build jellyfish-cli') {
            steps {
                dir('jellyfish-cli') {
                    sh 'chmod a+x ./gradlew'
                    sh './gradlew clean build install'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        
        stage('Test jellyfish') {
            steps {
                dir('jellyfish-examples') {
                    sh 'chmod a+x ./gradlew'
                    sh './gradlew clean build install --stacktrace --continue'
                }
            }
        }

        stage('Build offline support') {
            when {
                expression { params.offlineSupport }
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
                dir('jellyfish-systemdescriptor') {
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
        
        stage("Nexus Lifecycle") {
            when {
                expression { params.nexusLifecycle }
            }
			steps {
				// Evaluate the items for security, license, and other issues via Nexus Lifecycle.
				script {
					def policyEvaluationResult = nexusPolicyEvaluation(
						failBuildOnNetworkError: false,
						iqApplication: 'noalert',
						iqStage: 'build',
						jobCredentialsId: 'nexusiqCreds'
					)
					currentBuild.result = 'SUCCESS'
				}
				 withCredentials([usernamePassword(credentialsId: 'NexusLifecycle', passwordVariable: 'iqPassword', usernameVariable: 'iqUsername')]) {
					sh 'chmod +x downloadNexusLifecycleReport.sh'
					sh 'mkdir -p build'
					sh "curl ${BUILD_URL}consoleText >> build/jenkinsPipeline.log"
					sh "./downloadNexusLifecycleReport.sh build/jenkinsPipeline.log build/ \$iqUsername \$iqPassword"
                }
			}
		}
		
        stage('Upload') {
            when {
                expression { params.upload || (env.BRANCH_NAME == 'master' && params.performRelease) }
            }
            steps {
                dir('seaside-bootstrap-api') {
                    sh './gradlew upload'
                }
                dir('seaside-bootstrap') {
                    sh './gradlew upload'
                }
                dir('jellyfish-systemdescriptor-dsl') {
                    sh './gradlew upload'
                }
                dir('jellyfish-systemdescriptor') {
                    sh './gradlew upload'
                }
                dir('jellyfish-cli') {
                    sh './gradlew upload'
                }
            }
        }

        stage('Release') {
            when {
                expression { env.BRANCH_NAME == 'master' && params.performRelease }
            }
            steps {
               // Finish up the release.
               dir('seaside-bootstrap-api') {
                  sh './gradlew bumpTheVersion'
                  script {
                       try {
                           // This allows us to run Git commands with the credentials from Jenkins.  See
                           // https://groups.google.com/forum/#!topic/jenkinsci-users/BPdw6EOP0fQ
                           // and https://stackoverflow.com/questions/33570075/tag-a-repo-from-a-jenkins-workflow-script
                           // for more information.
                           withCredentials([usernamePassword(credentialsId: 'ngc-github-pipelines',
                                                             passwordVariable: 'gitPassword',
                                                             usernameVariable: 'gitUsername')]) {
                               // This allows us to use a custom credential helper that uses the values from Jenkins.
                               sh "git config credential.helper '!echo password=\$gitPassword; echo username=\$gitUsername; echo'"
                               sh 'GIT_ASKPASS=true ./gradlew releasePush'
                           }
                       }
                       finally {
                           sh 'git config --unset credential.helper'
                       }
                   }
               }
            }
        }
		
        		
        stage('Archive') {
            steps {
                // Create a ZIP that has everything.
                sh 'mkdir -p build'
                sh 'zip -j -r build/jellyfish-all.zip jellyfish-systemdescriptor-dsl/com.ngc.seaside.systemdescriptor.updatesite/build/com.ngc.seaside.systemdescriptor.updatesite-*.zip jellyfish-systemdescriptor/com.ngc.seaside.systemdescriptor.plus.updatesite/build/com.ngc.seaside.systemdescriptor.plus.updatesite-*.zip jellyfish-cli/com.ngc.seaside.jellyfish/build/distributions/jellyfish-*.zip build/dependencies-m2.zip build/dependencies.tsv build/deploy.sh build/settings.xml build/*.pdf'

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
