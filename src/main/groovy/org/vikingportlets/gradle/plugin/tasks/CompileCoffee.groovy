package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
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
		def isWindows = System.properties['os.name'].toLowerCase().contains('windows')
		["$project.projectDir/viking/views", "$project.projectDir/public/coffee"].each {
			if (isWindows) {
				exe(['cmd','/c','coffee', '--bare', '-o', "$project.buildDir/compiled_coffee/js", '-c', it])
			} else {
				exe(['coffee', '--bare', '-o', "$project.buildDir/compiled_coffee/js", '-c', it])
			}
		}
        exe(['coffee', '--bare', '-o', "$project.buildDir/compiled_coffee", '-c', "$project.projectDir/public"])
    }

    def exe(cmd) {
        Process command
        command = cmd.execute()
        command.waitFor()
        def errOutput = command.err.text
        if (errOutput) {
            logger.error(errOutput)
            throw new GradleException("Coffee script compilation failed")
        }
    }
}
