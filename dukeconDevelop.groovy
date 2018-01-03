/**
 * Created by ascheman on 24.01.16.
 */

String mavenVersion = 'maven-3.3.9'
String branch = "develop"

mavenJob ("dukecon_html5_${branch}") {
    scm {
        github ("dukecon/dukecon_html5", branch)
    }
    triggers {
        scm("H/10 * * * *")
    }
    mavenInstallation(mavenVersion)
    goals ('clean install')
}

mavenJob ("dukecon_server_${branch}") {
    scm {
        github ("dukecon/dukecon_server", branch)
    }
    triggers {
        scm("H/10 * * * *")
        upstream ("dukecon_html5_${branch}", 'SUCCESS')
    }
    mavenInstallation(mavenVersion)
    goals ('clean install -Djvm="${WORKSPACE}/xvfb.sh"')
}

mavenJob ("dukecon_server_docker_${branch}") {
    scm {
        github ("dukecon/dukecon_server_docker", branch)
    }
    triggers {
        scm("H/10 * * * *")
        upstream ("dukecon_server_${branch}", 'SUCCESS')
    }
    environmentVariables {
        env('DOCKER_HOST', 'unix:///var/run/docker.sock')
    }
    mavenInstallation(mavenVersion)
    goals ('clean install')
}

freeStyleJob ("dukecon_restart_docker_latest") {
    triggers {
        upstream ("dukecon_server_docker_${branch}", 'SUCCESS')
    }
    steps {
        shell("sudo /etc/init.d/docker-dukecon-latest restart")
    }
}

buildPipelineView('Build Pipeline Develop') {
    displayedBuilds(5)
    alwaysAllowManualTrigger()
    selectedJob("dukecon_html5_${branch}")
    showPipelineDefinitionHeader()
    showPipelineParametersInHeaders()
    showPipelineParameters()
    refreshFrequency(2)
    consoleOutputLinkStyle(OutputStyle.NewWindow)
}
