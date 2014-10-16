package org.vikingportlets.gradle.plugin.utils

/**
 * User: mardo
 * Date: 10/15/14
 * Time: 3:25 PM
 */
class LanguageUtils {

	static getLanguageIds(project) {
		def languageFiles = new File(project.projectDir, 'i18n').listFiles()
		languageFiles.findAll{
			it.name != "Language.properties" && !it.name.startsWith(".")
		}.collect {
			it.name - "Language_" - ".properties"
		}
	}
}
