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
class CompileCoffee extends DefaultTask {

    @TaskAction
    def compile() {
        def isWindows = System.properties['os.name'].toLowerCase().contains('windows')

        project.exec {
            if (isWindows)
                commandLine 'cmd','/c','coffee', '--bare', '-o', "$project.buildDir/compiled_coffee/js", '-c', "$project.projectDir/public/coffee"
            else
                commandLine 'coffee', '--bare', '-o', "$project.buildDir/compiled_coffee/js", '-c', "$project.projectDir/public/coffee"
        }
    }
}
