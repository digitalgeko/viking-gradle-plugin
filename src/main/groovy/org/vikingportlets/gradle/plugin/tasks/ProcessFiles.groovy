package org.vikingportlets.gradle.plugin.tasks

import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
class ProcessFiles extends DefaultTask {
    static final String PROCESSED_VIEWS_FOLDER = "processed-files/viking/views"
    @TaskAction
    def generate() {
        project.copy {
            from "viking/views"
            into "$project.buildDir/$PROCESSED_VIEWS_FOLDER"
        }
        new File("$project.buildDir/$PROCESSED_VIEWS_FOLDER").eachFileRecurse(FileType.FILES) { file ->
            if (file.name.endsWith(".ftl")) {
                file.text = file.text.replace("__", "\${h.portletId}")
            }
        }
    }
}
