pipeline {
    agent any

    parameters {
        booleanParam(name: 'PERFORM_RELEASE',
                     defaultValue: false,
                     description: 'If true, a release build will be performed.')
    }	
	
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

        stage('Upload') {
            when {
                expression { return !(params.PERFORM_RELEASE ==~ /(?i)(Y|YES|T|TRUE|ON|RUN)/) && env.BRANCH_NAME == 'master' }
            }
            steps {
                sh './gradlew upload'
            }
        }

        stage('Release') {
            when {
                expression { return params.PERFORM_RELEASE ==~ /(?i)(Y|YES|T|TRUE|ON|RUN)/ && env.BRANCH_NAME == 'master' }
            }
            steps {
                sh './gradlew clean build release'
                echo 'Release has been completed, please manually trigger the release of jellyfish-systemdescriptor-ext.'
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts allowEmptyArchive: true,
                                 artifacts: 'com.ngc.seaside.systemdescriptor.updatesite/build/com.ngc.seaside.systemdescriptor.updatesite-*.zip',
                                 caseSensitive: false,
                                 defaultExcludes: false,
                                 onlyIfSuccessful: true
            }
        }		
	

        stage('Trigger Downstream Projects') {
            when {
                expression { return !(params.PERFORM_RELEASE ==~ /(?i)(Y|YES|T|TRUE|ON|RUN)/) && env.BRANCH_NAME == 'master' }
            }		
            steps {
                build job: 'jellyfish-systemdescriptor-ext', wait: false
            }
        }
    }
}
