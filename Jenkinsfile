pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'chmod +x gradlew && ./gradlew clean build -x test'
            }
        }

        stage('Scan & Test') {
            steps {
                parallel (
                      "SonarQube" : {
                          sleep time:1000, unit:"MILLISECONDS"
                          withSonarQubeEnv('SEASIDE-sonarqube') {
                              sh './gradlew --info sonarqube'
                          }
                      },
                      "Test" : {
                          sh './gradlew check -PtestIgnoreFailures=true'
                          junit '**/build/test-results/test/*.xml'
                      }
                )

            }
        }

        stage('Publish Links') {
            steps {
                script {
                    currentBuild.description = "The Eclipse update site for this build is available at "
                }
            }
        }

        stage('Deploy & Archive') {
            steps {
                parallel (
                      'Upload': {
                          sh './gradlew upload'
                      },
                      'Archive': {
                          archiveArtifacts allowEmptyArchive: true,
                                           artifacts: 'com.ngc.seaside.systemdescriptor.ext.updatesite/build/com.ngc.seaside.systemdescriptor.ext.updatesite-*.zip',
                                           caseSensitive: false,
                                           defaultExcludes: false,
                                           onlyIfSuccessful: true
                      }
                )
            }
        }

        stage('Trigger Downstream Projects') {
            steps {
                build job: 'jellyfish-cli', wait: false
            }
        }
    }
}
