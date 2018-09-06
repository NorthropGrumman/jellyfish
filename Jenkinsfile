pipeline {
   agent {
      label {
         label ''
         customWorkspace "${JENKINS_HOME}/workspace/jellyfish/${JOB_NAME}"
      }
   }

   environment {
      // Specific the JDK required here.
      JAVA_HOME = "${JAVA_10_HOME}"
      PATH = "${JAVA_10_HOME}/bin:${PATH}"
   }

   options {
      // We can use this resource to ensure only one build updates the mavenLocal repo at a time.
      // This is important because the last stage in this pipeline removes all snapshots from the local repo.  We
      // don't want to remove a snapshot another build are just installed but that other build isn't finish yet (ie,
      // the build needs to reference the snapshot).  This happens if a pipeline has multiple invocations of Gradle.
      lock resource: 'mavenLocal'
      buildDiscarder(logRotator(numToKeepStr: '15'))
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
                   defaultValue: false)
   }

   stages {
      // Prepare for a release if necessary.

      stage('Prepare For Release') {
         when {
            expression { env.BRANCH_NAME == 'master' && params.performRelease }
         }
         steps {
            dir('jellyfish-systemdescriptor-dsl') {
               sh 'chmod a+x ../gradlew'
               sh '../gradlew removeVersionSuffix'
               sh '../gradlew createReleaseTag'
            }
         }
      }

      stage('Build jellyfish-systemdescriptor-dsl') {
         steps {
            dir('jellyfish-systemdescriptor-dsl') {
               sh 'chmod a+x ../gradlew'
               sh '../gradlew clean license ci'
               junit '**/build/test-results/test/*.xml'
            }
         }
      }

      stage('Build jellyfish-systemdescriptor') {
         steps {
            dir('jellyfish-systemdescriptor') {
               sh '../gradlew clean license ci --parallel'
               junit '**/build/test-results/test/*.xml'
            }
         }
      }

      stage('Build jellyfish-cli') {
         steps {
            dir('jellyfish-cli') {
               sh '../gradlew clean license ci --parallel'
               junit '**/build/test-results/test/*.xml'
            }
         }
      }

      stage('Build jellyfish commands') {
         parallel {
            stage('Build jellyfish-cli-commands') {
               steps {
                  dir('jellyfish-cli-commands') {
                     sh '../gradlew clean license ci --parallel'
                     junit '**/build/test-results/test/*.xml'
                  }
               }
            }
            stage('Build jellyfish-cli-analysis-commands') {
               steps {
                  dir('jellyfish-cli-analysis-commands') {
                     sh '../gradlew clean license ci --parallel'
                     junit '**/build/test-results/test/*.xml'
                  }
               }
            }
         }
      }

      stage('Build jellyfish-packaging') {
         steps {
            dir('jellyfish-packaging') {
               sh '../gradlew clean license ci -x :jellyfish.cli.gradle.plugins:test --parallel'
               withCredentials([usernamePassword(credentialsId: 'ngc-nexus-repo-mgr-pipelines',
                                                 passwordVariable: 'nexusPassword',
                                                 usernameVariable: 'nexusUsername')]) {
                  sh "../gradlew :jellyfish.cli.gradle.plugins:test -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword"
               }
            }
         }
         post {
            always {
               dir('jellyfish-packaging') {
                  junit '**/build/test-results/test/*.xml'
               }
            }
         }
      }

      stage('Build jellyfish-systemdescriptor-lang') {
         steps {
            dir('jellyfish-systemdescriptor-lang') {
               // No "ci" task for SD projects.
               sh '../gradlew clean license build install --parallel'
            }
         }
      }

      stage('Test jellyfish') {
         steps {
            dir('jellyfish-examples') {
               sh '../gradlew clean license build --parallel -S --continue'
            }
         }
      }

      stage('Docs') {
         steps {
            dir('docs') {
               sh 'chmod a+x build-site.sh'
               sh './build-site.sh'
            }
         }
      }

      stage('Build offline support') {
         when {
            expression { params.offlineSupport }
         }
         steps {
            dir('jellyfish-examples') {
               // By running this for a generated service, we get all the dependencies.  The only exception is the
               // transport providers.  We need to run an audit task for each different type of deployment model
               // so we get all the possible transport providers.  Regression 1 gets us the multicast provider.
               sh '../gradlew audit1'
               // We need to run audit12 since it uses a deployment model which requires ZeroMQ.  If we don't do this
               // we don't get the ZeroMQ dependencies.
               sh '../gradlew audit12'
            }
            dir('jellyfish-offline-support') {
               // Finally, we may need some extra dependencies.
               sh '../gradlew populateM2repo'
            }
            dir('build') {
               // Collect the m2 repository files inside a single ZIP.
               sh 'zip -r dependencies-m2.zip dependencies-m2'
            }
         }
      }

      stage('Nexus Lifecycle') {
         when {
            expression { params.nexusLifecycle }
         }
         steps {
            // Evaluate the items for security, license, and other issues via Nexus Lifecycle.
            withCredentials([usernamePassword(credentialsId: 'ngc-nexus-lifecycle-pipelines',
                                              passwordVariable: 'lifecyclePassword',
                                              usernameVariable: 'lifecycleUsername')]) {
               script {
                  def policyEvaluationResult = nexusPolicyEvaluation(
                        failBuildOnNetworkError: false,
                        iqApplication: 'jellyfish',
                        iqStage: 'build',
                        jobCredentialsId: 'ngc-nexus-lifecycle-pipelines'
                  )
                  sh 'mkdir -p build'
                  sh "curl -s -S -L -k -u \"\$lifecycleUsername:\$lifecyclePassword\" '${policyEvaluationResult.applicationCompositionReportUrl}/pdf' > build/Nexus-Lifecycle-Report.pdf"
               }
            }
         }
      }

      stage('Upload') {
         when {
            expression { params.upload || (env.BRANCH_NAME == 'master' && params.performRelease) }
         }
         steps {
            withCredentials([usernamePassword(credentialsId: 'ngc-nexus-repo-mgr-pipelines',
                                              passwordVariable: 'nexusPassword',
                                              usernameVariable: 'nexusUsername')]) {
               dir('jellyfish-systemdescriptor-dsl') {
                  sh '../gradlew upload -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword'
               }
               dir('jellyfish-systemdescriptor') {
                  sh '../gradlew upload -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword'
               }
               dir('jellyfish-cli') {
                  sh '../gradlew upload -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword'
               }
               dir('jellyfish-cli-commands') {
                  sh '../gradlew upload -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword'
               }
               dir('jellyfish-cli-analysis-commands') {
                  sh '../gradlew upload -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword'
               }
               dir('jellyfish-packaging') {
                  sh '../gradlew upload -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword'
               }
               dir('jellyfish-systemdescriptor-lang') {
                  sh '../gradlew upload -PnexusUsername=$nexusUsername -PnexusPassword=$nexusPassword'
               }
            }
         }
      }

      stage('Release') {
         when {
            expression { env.BRANCH_NAME == 'master' && params.performRelease }
         }
         steps {
            // Finish up the release.
            dir('jellyfish-systemdescriptor-dsl') {
               sh '../gradlew bumpTheVersion'
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
                        sh 'GIT_ASKPASS=true ../gradlew releasePush'
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
            sh '''zip -j -r build/jellyfish-all.zip
				  jellyfish-packaging/com.ngc.seaside.systemdescriptor.updatesite/build/updatesite/com.ngc.seaside.systemdescriptor.updatesite-*.zip
				  jellyfish-packaging/com.ngc.seaside.jellyfish/build/distributions/jellyfish-*.zip
				  jellyfish-packaging/com.ngc.seaside.jellyfish.sonarqube.plugin/build/libs/jellyfish.sonarqube.plugin-*.jar
              build/site.zip
				  build/dependencies-m2.zip
				  build/dependencies.tsv
				  build/deploy.sh
				  build/settings.xml
				  build/*.pdf
			   '''.replaceAll('\\s+', ' ')

            // Archive the zip that has everything.
            archiveArtifacts allowEmptyArchive: true,
                             artifacts: 'build/jellyfish-all.zip',
                             caseSensitive: false,
                             defaultExcludes: false,
                             onlyIfSuccessful: true
         }
      }
   }

   post {
      always {
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
