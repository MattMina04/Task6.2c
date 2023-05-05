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
                // Use a code analysis tool such as SonarQube or Checkstyle
            }
        }

        stage('Security Scan') {
            steps {
                // Use a security scan tool such as OWASP ZAP or Sonatype DepShield
            }
        }

        stage('Deploy to Staging') {
            steps {
                // Use a deployment tool such as AWS CodeDeploy or Jenkins Deploy Plugin
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                // Use an integration testing tool such as Selenium or JMeter
            }
        }

        stage('Deploy to Production') {
            steps {
                // Use a deployment tool such as AWS CodeDeploy or Jenkins Deploy Plugin
            }
        }
    }

    post {
        always {
            emailext subject: "Pipeline Status: ${currentBuild.result}", 
            body: "Pipeline Status: ${currentBuild.result}\n\nLogs:\n${currentBuild.rawBuild.getLog()}", 
            to: "example@email.com"
       
