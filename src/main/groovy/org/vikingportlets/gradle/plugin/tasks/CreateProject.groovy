package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
class CreateProject extends DefaultTask {

    @TaskAction
    def create() {
        // Project Structure definition
        def projectStructure = [
                new File("$project.projectDir/conf"),
                new File("$project.projectDir/sitebuilder"),
                new File("$project.projectDir/viking/models"),
                new File("$project.projectDir/viking/controllers"),
                new File("$project.projectDir/viking/views"),
                new File("$project.projectDir/public/images"),
                new File("$project.projectDir/public/css"),
                new File("$project.projectDir/public/js"),
                new File("$project.projectDir/public/coffee"),
        ]

        println "Creating new project..."
        projectStructure.each { it.mkdirs() }

        def coffeGitIgnore = new File("$project.projectDir/public/coffee/.gitignore")
        if (!coffeGitIgnore.exists()) {
            coffeGitIgnore.createNewFile()
        }

        project.copy {
            from '.templates/conf'
            into "conf"
            include 'portlet.conf'
            include 'dev.conf'
            include 'sitebuilder.conf'
            include 'log4j.properties'
            expand([projectName: project.name, projectDir: project.projectDir])
        }
        
        project.copy {
            from '.templates/views/viking_macros'
            into "viking/views/viking_macros"
            include '*'
        }
        project.copy {
            from '.templates/public/css'
            into "public/css"
            include 'main.css'
        }
        project.copy {
            from '.templates/public/js'
            into "public/js"
            include '*/*'
        }
        project.copy {
            from '.templates/public/icon.png'
            into "public"
            include 'icon.png'
        }

        project.copy {
            from '.templates/public/coffee'
            into "public/coffee"
            include '.gitignore'
        }

        project.copy {
            from '.templates/sitebuilder'
            into "sitebuilder"
            include 'sites.groovy'
            expand(projectName: project.name)
        }

        project.copy {
            from '.templates/public/js/angular'
            into "public/js/angular"
            include 'init.js'
            expand(projectName: project.name)
        }

        project.viking.portletName = project.name
    }

}
