package org.vikingportlets.gradle.plugin.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
class ListPortlets extends DefaultTask {

    @TaskAction
    def list() {
        new File('viking/controllers').listFiles()
                .findAll { f -> f.name.endsWith("Portlet.groovy") }
                .collect { it.toString() }
                .each { println it - "viking/controllers/" - ".groovy" }
    }
}
