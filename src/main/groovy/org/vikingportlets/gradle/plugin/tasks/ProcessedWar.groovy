package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.vikingportlets.gradle.plugin.utils.JarUtils

/**
 * Created by mardo on 5/18/15.
 */
class ProcessedWar extends DefaultTask {

    @TaskAction
    def exec() {

        def deployDir = new File(project.deployDir)
        def webappsDir = new File(project.webappsDir)
        def logFile = new File(project.logFile)

        def srcWarFile = new File("$project.war.archivePath")

        def projectName = srcWarFile.name - ".war"

        def deployedDir = new File(webappsDir, projectName)
        if (deployedDir.exists()) {
            deployedDir.delete()
        }

        project.copy {
            from project.war.archivePath
            into deployDir
        }

        def reader

        try {
            reader = logFile.newReader()
            reader.skip(logFile.length())

            def line
            def tries = 0
            while (tries < 90) {
                line = reader.readLine()
                if (line) {
                    if (line.contains("$projectName are available for use")) {
                        break;
                    }
                } else {
                    Thread.sleep(1000)
                    tries++
                }
            }
        } finally {
            reader?.close()
        }

        def tempWarFile = File.createTempFile(projectName, ".war")
        JarUtils.createJarArchive(deployedDir, tempWarFile)

        URL obj = new URL("$project.liferayUrl/manager/text/undeploy?path=/$projectName")
        HttpURLConnection con = (HttpURLConnection) obj.openConnection()
        String basicAuth = "Basic " + "$project.tomcatUser:$project.tomcatPass".bytes.encodeBase64()
        con.setRequestProperty ("Authorization", basicAuth)
        con.setRequestMethod("GET")

        if (con.responseCode != 200) {
            throw new Exception("Can't undeploy $projectName")
        }

        if (srcWarFile.exists()) srcWarFile.delete()

        tempWarFile.renameTo(srcWarFile)

    }

}
