pipeline {
	agent any
	
	environment {
		GIT_REPOSITORY = "https://github.com/MattMina04/SIT223-Task6.2C.git"
		GIT_BRANCH = "main"

		EMAIL_ADDRESS = "matthew2117@Hotmail.com"

		BUILD_PATH = 'build'
		TEST_PATH = 'test'

		CPPCHECK_LOG = 'cppcheck.log'

		PRODUCTION_ENVIRONMENT = 'mattprod'
		TESTING_ENVIRONMENT = 'matttest'
	}

	stages {
		stage('Build') {
			steps {

				echo "Fetch source code"
				git branch: "${GIT_BRANCH}", url: "${GIT_REPOSITORY}"

				echo 'Build code with Gradle'
				// bat 'gradle clean build'
			}
		}
		
		stage('Unit and Integration Tests') {
			steps {
				echo 'Run unit tests generated with the Google Test framework'
				// bat '%TEST_PATH%/test.exe'

				mail to: "${EMAIL_ADDRESS}", subject: "[JENKINS] Successful test", body: "${currentBuild.rawBuild.log}"
			}
			post {
				failure {
					mail to: "${EMAIL_ADDRESS}", subject: "[JENKINS] Failed test", body: "${currentBuild.rawBuild.log}"
				}
			}
		}
		
		stage('Code Analysis'){
			steps {
				echo 'Perform code analysis with Cppcheck'
				// bat 'cppcheck --enable=all %BUILD_PATH% 2> %CPPCHECK_LOG%'
			}
		}
		  
		stage('Security Scan'){
			steps {
				echo 'Security scan.'

				mail to: "${EMAIL_ADDRESS}", subject: "[JENKINS] Successful security scan", body: "${currentBuild.rawBuild.log}"
			}
			post {
				failure {
					mail to: "${EMAIL_ADDRESS}", subject: "[JENKINS] Failed security scan", body: "${currentBuild.rawBuild.log}"
				}
			}
		}
		
		stage('Deploy to Staging'){
			steps {
				echo "Deploy the app to testing environment ${TESTING_ENVIRONMENT}"
			}
		}
		
		stage('Integration Tests on Staging'){
			steps {
				sleep(10)
			}
		}
		
		stage('Deploy to Production'){
			steps {
				echo "Deploy the app to production environment ${PRODUCTION_ENVIRONMENT}"
			}
		}
	}
}
