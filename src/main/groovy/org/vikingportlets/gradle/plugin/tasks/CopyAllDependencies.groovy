package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
class CopyAllDependencies extends DefaultTask {

    @TaskAction
    def exec() {
        project.copy {
            into "$project.buildDir/output/lib"
			from project.configurations.runtime
        }
    }

}
