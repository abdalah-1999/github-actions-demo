pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "abdalahahmad/github-actions-demo:latest"
    }

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

        stage('SonarQube Analysis - Java 17') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh '''
                        docker run --rm \
                        --network cicd-network \
                        --volumes-from jenkins \
                        -w /var/jenkins_home/workspace/java-ci-cd \
                        maven:3.9.6-eclipse-temurin-17 \
                        mvn -B org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar \
                        -Dsonar.projectKey=java-app \
                        -Dsonar.host.url=http://sonarqube:9000 \
                        -Dsonar.token=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t abdalahahmad/github-actions-demo:latest .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    docker push abdalahahmad/github-actions-demo:latest
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                docker run --rm \
                --network cicd-network \
                --volumes-from jenkins \
                -v /c/k8s:/k8s \
                bitnami/kubectl:latest apply -f /k8s/deployment.yaml
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
