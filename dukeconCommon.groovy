/**
 * Created by ascheman on 24.01.16.
 */

mavenJob ("dukecon") {
    scm {
        github ("dukecon/dukecon")
    }
    triggers {
        scm("H/10 * * * *")
    }
    goals ('clean install')
}
