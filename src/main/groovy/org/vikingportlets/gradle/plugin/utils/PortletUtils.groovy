package org.vikingportlets.gradle.plugin.utils

/**
 * User: mardo
 * Date: 12/4/13
 * Time: 8:56 AM
 */
class PortletUtils {
	static getPortlets (project) {

		def projectPortlets = new File(project.projectDir, 'viking' + File.separator + 'controllers').listFiles()
				.findAll { f -> f.name.endsWith("Portlet.groovy") }
				.collect { it.toString() - (project.projectDir.path+File.separator) }

		def modulePortlets = ConfUtils.getPortletPluginPaths(project).collect {
			new File("$it" + File.separator + "controllers").listFiles()
					.findAll { f -> f.name.endsWith("Portlet.groovy") }
					.collect { it.toString() }
		}.flatten()

		projectPortlets + modulePortlets
	}
}
