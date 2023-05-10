pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Unit and Integration Tests') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Code Analysis') {
            steps {
                sh 'mvn sonar:sonar'
            }
        }
        stage('Security Scan') {
            steps {
                sh 'mvn dependency-check:check'
            }
        }
        stage('Deploy to Staging') {
            steps {
                sh 'scp target/app.war user@staging-server:/opt/tomcat/webapps'
            }
        }
        stage('Integration Tests on Staging') {
            steps {
                sh 'curl http://staging-server:8080/app'
            }
        }
        stage('Deploy to Production') {
            steps {
                sh 'scp target/app.war user@production-server:/opt/tomcat/webapps'
            }
        }
    }

    post {
        always {
            emailext subject: "Pipeline ${currentBuild.fullDisplayName} completed: ${currentBuild.result}",
                     body: "Pipeline ${currentBuild.fullDisplayName} completed: ${currentBuild.result}.",
                     to: "email@example.com",
                     attachmentsPattern: 'logs/*.txt'
        }
    }
}
