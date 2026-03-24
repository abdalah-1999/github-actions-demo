pipeline {
    agent any

    stages {
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

        stage('SonarQube Analysis - Java 8') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                    docker run --rm \
                      --network cicd-network \
                      --volumes-from jenkins \
                      -w /var/jenkins_home/workspace/java-ci-cd \
                      maven:3.9.6-eclipse-temurin-8 \
                      mvn -B sonar:sonar \
                      -Dsonar.projectKey=java-app \
                      -Dsonar.host.url=$SONAR_HOST_URL \
                      -Dsonar.login=$SONAR_AUTH_TOKEN
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t abdalahahmad/github-actions-demo:latest /var/jenkins_home/workspace/java-ci-cd'
            }
        }
    }
}
