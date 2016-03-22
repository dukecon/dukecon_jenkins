/**
 * Created by ascheman on 24.01.16.
 */

mavenJob ("dukecon") {
    scm {
        github ("dukecon/dukecon", branch)
    }
    triggers {
        scm("H/10 * * * *")
    }
    mavenInstallation('maven-3.2.5')
    goals ('clean install')
}
