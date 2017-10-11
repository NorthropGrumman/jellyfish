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
				sh './gradlew check -PtestIgnoreFailures=true'
                junit '**/build/test-results/test/*.xml'
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
                                           artifacts: 'com.ngc.seaside.systemdescriptor.updatesite/build/com.ngc.seaside.systemdescriptor.updatesite-*.zip',
                                           caseSensitive: false,
                                           defaultExcludes: false,
                                           onlyIfSuccessful: true
                      }
                )
            }
        }

        stage('Trigger Downstream Projects') {
            steps {
                build job: 'jellyfish-systemdescriptor-ext', wait: false
            }
        }
    }
}
