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
                new File("$project.projectDir/i18n"),
                new File("$project.projectDir/public"),
                new File("$project.projectDir/sitebuilder"),
                new File("$project.projectDir/viking/models"),
                new File("$project.projectDir/viking/controllers"),
                new File("$project.projectDir/viking/views"),
        ]

        println "Creating new project..."
        projectStructure.each { it.mkdirs() }

        project.copy {
            from '.templates/conf'
            into "conf"
            include 'portlet.conf'
            include 'dev.conf'
            include 'sitebuilder.conf'
            include 'log4j.properties'
            include 'portal.properties'
            include 'portlet.properties'
            expand([projectName: project.name, projectDir: project.projectDir])
        }
        
        project.copy {
            from '.templates/views/viking_macros'
            into "viking/views/viking_macros"
            include '*'
        }

        project.copy {
            from '.templates/i18n'
            into "i18n"
            include '*'
        }

        project.copy {
            from '.templates/public'
            into "public"
            include '**/**'
        }

        project.copy {
            from '.templates/public'
            into "public"
            include 'js/angular/init.js'
            expand(projectName: project.name)
        }

        project.copy {
            from '.templates/sitebuilder'
            into "sitebuilder"
            include 'sites.groovy'
            expand(projectName: project.name)
        }

        ["coffee", "images"].each {
            def coffeGitIgnore = new File("$project.projectDir/public/$it/.gitignore")
            if (!coffeGitIgnore.exists()) {
                coffeGitIgnore.createNewFile()
            }
        }

        project.viking.portletName = project.name
    }

}
