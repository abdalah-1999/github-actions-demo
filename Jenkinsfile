pipeline {
    agent any

    environment {
        SONARQUBE_SERVER = 'sonarqube'
        SONARQUBE_URL = 'http://sonarqube:9000'
        IMAGE_NAME = 'abdalahahmad/github-actions-demo:latest'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build - Java 17') {
            steps {
                sh '''
                docker run --rm \
                  --volumes-from jenkins \
                  -w /var/jenkins_home/workspace/java-ci-cd \
                  maven:3.9.6-eclipse-temurin-17 \
                  mvn -B clean package -DskipTests
                '''
            }
        }

        stage('Test - Java 11') {
            steps {
                sh '''
                docker run --rm \
                  --volumes-from jenkins \
                  -w /var/jenkins_home/workspace/java-ci-cd \
                  maven:3.9.6-eclipse-temurin-11 \
                  mvn -B test
                '''
            }
        }

        stage('SonarQube Analysis - Java 17') {
            steps {
                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    withCredentials([string(credentialsId: 'sonarqube-token', variable: 'SONAR_TOKEN')]) {
                        sh '''
                        docker run --rm \
                          --network cicd-network \
                          --volumes-from jenkins \
                          -w /var/jenkins_home/workspace/java-ci-cd \
                          maven:3.9.6-eclipse-temurin-17 \
                          mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar \
                          -Dsonar.projectKey=java-app \
                          -Dsonar.host.url=http://sonarqube:9000 \
                          -Dsonar.token=${SONAR_TOKEN}
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t ${IMAGE_NAME} .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                    docker push ${IMAGE_NAME}
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                kubectl apply -f deployment.yaml
                kubectl apply -f service.yaml
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
