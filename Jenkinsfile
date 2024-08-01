pipeline {
    agent any


    stages {
        stage('Pull Code'){
            steps{
                git branch: 'main', credentialsId: '6758ba3f-1ac5-4346-9e6d-c0ad051ed35a', url: 'https://github.com/niehmanyo/ISSCodeHosting.git'

            }
        }

        stage("Maven Build"){
            steps{
               sh '''ls
                mvn clean package'''
            }
        }

         stage("Docker Build"){
                    steps{
                       sh '''ls
                        cd Auth-Service
                        docker build -t Auth-Service
                        docker run -p 8081:8081 Auth-Service'''
                    }
                }

    }
}

