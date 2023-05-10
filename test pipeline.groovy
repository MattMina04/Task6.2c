pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn -T 4 clean package'
            }
        }
        
        stage('Unit and Integration Tests') {
            steps {
                sh 'mvn -T 4 test'
            }
        }
        
        stage('Code Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn -T 4 sonar:sonar'
                }
            }
        }
        
        stage('Security Scan') {
            steps {
                sh 'npm install -g snyk && snyk test'
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                sh 'ansible-playbook deploy-staging.yml'
            }
        }
        
        stage('Integration Tests on Staging') {
            steps {
                sh 'python integration_tests.py'
            }
        }
        
        stage('Deploy to Production') {
            steps {
                sh 'ansible-playbook deploy-production.yml'
            }
        }
    }
    
    post {
        always {
            emailext body: "Pipeline ${currentBuild.result}: Job ${env.JOB_NAME} Build #${env.BUILD_NUMBER}\n\n${env.BUILD_URL}",
                    mimeType: 'text/html',
                    subject: "${currentBuild.result}: ${env.JOB_NAME} build #${env.BUILD_NUMBER}",
                    to: 'youremail@example.com',
                    attachLog: true
        }
    }
}
