package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.internal.tasks.CommandLineOption
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
class AddPortlet extends DefaultTask {

    def portletName

    @TaskAction
    def add() {
        if (!portletName) {
            portletName = project.viking.portletName
        }
        println "Adding new portlet: ${portletName}"

        // Create controller
        project.copy {
            from '.templates/classes/controllers'
            into "viking/controllers"
            include 'Portlet.groovy'
            rename { String fileName ->
                portletName.capitalize() + fileName
            }
            expand(portletName: portletName)
        }

        // Create views directory and add default views
        new File("views/${portletName.capitalize()}Portlet").mkdir()
        project.copy {
            from '.templates/views/Portlet'
            into "viking/views/${portletName.capitalize()}Portlet"
            include '*'
            expand(portletName: portletName)
        }

    }

    @CommandLineOption(options = "portletName", description = "Sets the name for a new portlet.")
    void setPortletName(String portletName) {
        this.portletName = portletName
    }

}
