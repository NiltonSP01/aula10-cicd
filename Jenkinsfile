pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }

        stage('Analise estatica (PMD)') {
            steps {
                sh 'mvn -B pmd:check'
            }
        }

        stage('Aprovacao') {
            steps {
                input message: 'Aprovar deploy para staging?', ok: 'Deploy'
            }
        }

        stage('Deploy to Staging') {
            when {
                branch 'main'
            }
            steps {
                script {
                    env.PREVIOUS_IMAGE = sh(script: 'kubectl get deployment myapp -o jsonpath="{.spec.template.spec.containers[0].image}" || true', returnStdout: true).trim()
                    try {
                        sh 'docker build -t myapp:${GIT_COMMIT} .'
                        sh 'kubectl set image deployment/myapp myapp=myapp:${GIT_COMMIT}'
                        sh 'kubectl rollout status deployment/myapp --timeout=120s'
                    } catch (err) {
                        echo "Deploy falhou, fazendo rollback"
                        sh 'kubectl rollout undo deployment/myapp || true'
                        throw err
                    }
                }
            }
        }
    }

    post {
        failure {
            echo "Build falhou: ${env.JOB_NAME}"
        }
    }
}