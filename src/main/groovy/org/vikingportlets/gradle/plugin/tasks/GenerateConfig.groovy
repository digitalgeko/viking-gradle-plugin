package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.vikingportlets.gradle.plugin.utils.ConfUtils
import org.vikingportlets.gradle.plugin.utils.LanguageUtils
import org.vikingportlets.gradle.plugin.model.Portlet

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
class GenerateConfig extends DefaultTask {

	def collectFiles (extension, directories) {
		directories.collect {
			new File(it).listFiles().findAll { f -> f.name.endsWith(extension) }.collect { it.name.toString() }
		}.flatten()
	}

    @TaskAction
    def generate() {

        if (new File("${project.projectDir}/conf/portlet.conf").exists()) {

			def portlets = Portlet.getPortletPaths(project)

			def portletMaps = portlets.collect { new Portlet(it) }

			def javascripts = collectFiles(".js", ["$project.buildDir/compiled_coffee/js", "$project.projectDir/public/js"])

			def portletJavascripts = portletMaps.collectEntries {
				[
						(it.portletName): collectFiles(".js", [
								"$project.buildDir/compiled_coffee/js/${it.portletName}Portlet",
								"$project.projectDir/viking/views/${it.portletName}Portlet"
						])
				]
			}

            project.copy {
                from '.templates/conf'
                into "$project.buildDir/conf"
                include 'portlet.xml'
                include 'liferay-display.xml'
                include 'liferay-portlet.xml'
                include 'liferay-hook.xml'
                expand(
                        portlets: portlets,
						portletMaps: portletMaps,
                        projectName: project.name,
                        javascripts: javascripts,
						portletJavascripts: portletJavascripts,
                        conf: ConfUtils.getProjectConfig(project),
						languageIds: LanguageUtils.getLanguageIds(project),
                )
            }
        }
    }
}
