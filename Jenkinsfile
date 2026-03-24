pipeline {
    agent any

    stages {
        stage('Build - Java 17') {
            steps {
                sh '''
                docker run --rm \
                  -v "$WORKSPACE":/app \
                  -w /app \
                  maven:3.9.6-eclipse-temurin-17 \
                  mvn clean package
                '''
            }
        }

        stage('Test - Java 11') {
            steps {
                sh '''
                docker run --rm \
                  -v "$WORKSPACE":/app \
                  -w /app \
                  maven:3.9.6-eclipse-temurin-11 \
                  mvn test
                '''
            }
        }

        stage('SonarQube Analysis - Java 8') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                    docker run --rm \
                      -v "$WORKSPACE":/app \
                      -w /app \
                      maven:3.9.6-eclipse-temurin-8 \
                      mvn sonar:sonar \
                      -Dsonar.projectKey=java-app \
                      -Dsonar.host.url=$SONAR_HOST_URL \
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
