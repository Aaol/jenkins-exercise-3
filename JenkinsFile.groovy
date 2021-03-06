pipeline {
    agent any
    stages {

        stage("nettoyage")
        {
            steps(){
                cleanWs()
            }
        }
        stage("git pull") 
        {
            steps(){
                git "https://github.com/aaol/jenkins-exercise-3.git"
            }
        }
        stage("writeconf")
        {
            withCredentials([
                string(credentialsId: params.env + ".user", variable: 'user'), 
                string(credentialsId: params.env + ".db", variable: 'db'), 
                string(credentialsId: params.env + ".password", variable: 'password')
            ]) {
                steps()
                {

                    String config = readFile "conf/bdd.conf"
                    def result = new groovy.json.JsonSlurperClassic().parseText(config)
                    result.user = user
                    result.database = db
                    result.password = password
                    def json = groovy.json.JsonOutput.toJson(result)
                    writeFile file: "conf/bdd.conf", text: json
                }
            }
        
        }
        stage("saveconf")
        {
            archiveArtifacts 'conf/*'
        }
    }
}