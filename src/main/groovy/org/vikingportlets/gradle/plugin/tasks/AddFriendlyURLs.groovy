package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.vikingportlets.gradle.plugin.utils.ConfUtils
import org.vikingportlets.gradle.plugin.model.Portlet

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
class AddFriendlyURLs extends DefaultTask {

    @TaskAction
    def generate() {
        Portlet.getPortletPaths(project).each {

	        def portletClassName = it.substring(it.lastIndexOf(File.separator)+1) - '.groovy'
            def dashedName = portletClassName.replaceAll(/(\B[A-Z])/, '-$1').toLowerCase()

            project.copy {
                from '.templates/conf'
                into "$project.buildDir/urlmappings"
                include 'friendly-url-routes.xml'
                rename { String fileName ->
                    "${dashedName}-${fileName}"
                }
                expand(
                    portletClassName: portletClassName,
                    conf: ConfUtils.getProjectConfig(project)
                )
            }
        }
    }
}
