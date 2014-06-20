package org.vikingportlets.gradle.plugin.utils

/**
 * User: mardo
 * Date: 12/4/13
 * Time: 10:58 AM
 */
class ConfUtils {

	static getPortletPluginPaths (project) {
		def projectConfig = getProjectConfig(project)

		projectConfig?.plugins?.portlets?.collect { String pluginPath ->
			if (pluginPath.startsWith("/")) {
				return pluginPath
			} else {
				return "$System.env.VIKING_HOME/modules/$pluginPath"
			}
		} ?: []
	}

	static getProjectConfig(project) {
		def confFile = new File("${project.projectDir}/conf/portlet.conf")
		if (confFile.exists()) {
			return new ConfigSlurper().parse(confFile.toURI().toURL())	
		}
	}
}
