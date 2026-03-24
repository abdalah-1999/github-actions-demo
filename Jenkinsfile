pipeline {
    agent any

    stages {
        stage('Build - Java 17') {
            steps {
                sh 'docker exec java17-builder mvn clean package'
            }
        }

        stage('Test - Java 11') {
            steps {
                sh 'docker exec java11-tester mvn test'
            }
        }

        stage('SonarQube Analysis - Java 8') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                    docker exec java8-analyzer mvn sonar:sonar \
                    -Dsonar.projectKey=java-app \
                    -Dsonar.host.url=http://sonarqube:9000 \
                    -Dsonar.login=$SONAR_AUTH_TOKEN
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t abdalahahmad/github-actions-demo:latest .'
            }
        }
    }
}
