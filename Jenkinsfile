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

        stage('Publish') {
            steps {
                script {

                }
            }
        }

//        stage("Quality Gate") {
//            steps {
//                script {
//                    step([$class: 'JacocoPublisher', execPattern:'**/build/jacoco/*.exec', classPattern: '**/build/classes/main', sourcePattern: 'src/main/java'])
//                    sleep time:1000, unit:"MILLISECONDS"
//                    timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
//                        def qualitygate = waitForQualityGate()
////                        if (qualitygate.status != "OK" && qualitygate.status != "WARN") {
////                            error "Pipeline aborted due to quality gate coverage failure: ${qualitygate.status}"
////                        }
//                    }
//                }
//            }
//        }

        stage('Deploy') {
            steps {
                sh './gradlew upload'
            }
        }

        stage('Trigger Downstream Projects') {
            steps {
                build job: 'jellyfish-cli', wait: false
            }
        }
    }
}
