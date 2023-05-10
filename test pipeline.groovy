pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Unit and Integration Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Code Analysis') {
            steps {
                withMaven(jdk: '11') {
                    sh 'mvn sonar:sonar'
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
                sh 'scp target/my-app-1.0.jar user@staging-server:/path/to/deploy'
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                sh 'ssh user@staging-server \'java -jar /path/to/deploy/my-app-1.0.jar\' && curl http://staging-server:8080'
            }
        }

        stage('Deploy to Production') {
            steps {
                sh 'scp target/my-app-1.0.jar user@production-server:/path/to/deploy'
            }
        }
    }

    post {
        always {
            archiveArtifacts 'target/*.jar'
        }
        success {
            mail to: 'email@example.com',
                 subject: 'Pipeline succeeded',
                 body: 'Pipeline succeeded. Check attached logs for details.',
                 attachmentsPattern: 'target/*.jar'
        }
        failure {
            mail to: 'email@example.com',
                 subject: 'Pipeline failed',
                 body: 'Pipeline failed. Check attached logs for details.',
                 attachmentsPattern: 'target/*.jar'
        }
    }
}
