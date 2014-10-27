package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.DefaultExecAction

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
		project.exec {
			def isWindows = System.properties['os.name'].toLowerCase().contains('windows')
			["$project.projectDir/viking/views", "$project.projectDir/public/coffee"].each {
				DefaultExecAction command
				if (isWindows) {
					command = commandLine 'cmd','/c','coffee', '--bare', '-o', "$project.buildDir/compiled_coffee/js", '-c', it
				} else {
					command = commandLine 'coffee', '--bare', '-o', "$project.buildDir/compiled_coffee/js", '-c', it
				}
				command.execute().assertNormalExitValue()
			}
		}
    }
}
