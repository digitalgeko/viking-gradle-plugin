package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.vikingportlets.gradle.plugin.utils.ConfUtils
import org.vikingportlets.gradle.plugin.utils.PortletUtils

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
class GenerateConfig extends DefaultTask {

    @TaskAction
    def generate() {

        if (new File("${project.projectDir}/conf/portlet.conf").exists()) {
            project.copy {
                from '.templates/conf'
                into "$project.buildDir/conf"
                include 'portlet.xml'
                include 'liferay-display.xml'
                include 'liferay-portlet.xml'
                include 'liferay-hook.xml'
                expand(
                        portlets: PortletUtils.getPortlets(project),
                        projectName: project.name,
                        javascripts: ["$project.buildDir/compiled_coffee/js", "$project.projectDir/public/js"].collect {
                            new File(it).listFiles().findAll { f -> f.name.endsWith(".js") }.collect { it.name.toString() }
                        }.flatten(),
                        conf: ConfUtils.getProjectConfig(project),
                )
            }
        }
    }
}
