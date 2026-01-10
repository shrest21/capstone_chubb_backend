pipeline {
    agent any

    stages {

        stage('Checkout Code') {
            steps {
                echo 'Cloning repository...'
                git url: 'https://github.com/shrest21/capstone_chubb_backend', branch: 'main'
            }
        }

        stage('Build JARs') {
            steps {
                echo 'Building all microservices JARs'
                sh 'mvn clean package'
            }
            post {
                success {
                    echo 'JAR build successful'
                    archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images using Docker Compose...'
                sh 'docker compose build'
            }
        }

        stage('Deploy Services') {
            steps {
                echo 'Starting Dockerized services...'
                sh 'docker compose down || true'
                sh 'docker compose up -d'
            }
            post {
                success {
                    echo 'Services are running'
                    sh 'docker ps'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed: JARs built & services deployed'
        }
        failure {
            echo 'Pipeline failed. Check logs above.'
        }
    }
}
