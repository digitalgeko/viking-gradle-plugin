package org.vikingportlets.gradle.plugin.model

import org.vikingportlets.gradle.plugin.utils.ConfUtils

/**
 * User: mardo
 * Date: 12/4/13
 * Time: 8:56 AM
 */
class Portlet {

	String dashedName

	String portletName

	Portlet (portletPath) {
		def vkCtrlPath = 'viking' + File.separator + 'controllers' + File.separator
		this.dashedName = portletPath.replaceAll(/(\B[A-Z])/, '-$1').toLowerCase() - vkCtrlPath - '.groovy'
		this.portletName = portletPath - vkCtrlPath - 'Portlet.groovy'
	}

	static getPortletPaths(project) {

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
