package org.vikingportlets.gradle.plugin

import org.gradle.api.*
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.WarPlugin
import org.gradle.api.tasks.bundling.Zip
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.vikingportlets.gradle.plugin.tasks.*
import org.vikingportlets.gradle.plugin.utils.ConfUtils


class Viking implements Plugin<Project> {

    public static final String VIKING_PROJECT_TASK_NAME = "new"
    public static final String VIKING_ADD_PORTLET_TASK_NAME = "add"
    public static final String LIST_PORTLETS_TASK_NAME = "list-portlets"
    public static final String COMPILE_COFFEE_TASK_NAME = "compile-coffee"
    public static final String GENERATE_CONFIGURATION_TASK_NAME = "generate-config"
    public static final String PROCESS_FILES_TASK_NAME = "process-files"
    public static final String ADD_FRIENDLY_URLS = "add-friendlyURLs"
    public static final String DEPLOY_TASK_NAME = "deploy"
    public static final String COPY_ALL_DEPENDENCIES_TASK_NAME = "copy-all-dependencies"
	public static final String BUILD_SITE_TASK_NAME = "build-site"


    void apply(Project project) {

        project.extensions.create("viking", VikingExtension)
        project.plugins.apply(JavaPlugin.class)
        project.plugins.apply(GroovyPlugin.class)
        project.plugins.apply(IdeaPlugin.class)
        project.plugins.apply(WarPlugin.class)

        project.buildscript.configurations.create('compile')
        project.buildscript.configurations.create('providedCompile')

        project.configurations { compile }

        project.repositories {
            mavenCentral()
            maven { url "http://morphia.googlecode.com/svn/mavenrepo" }
            maven { url "http://repo.gradle.org/gradle/repo" }
            maven { url "http://107.170.56.196:8081/artifactory/libs-release-local" }
        }
        def liferayVersion = project.hasProperty("liferayVersion") ? project.liferayVersion : "6.1.1"
        
        project.dependencies {
            compile 'org.vikingportlets:viking-framework:0.2.21'
            compile 'org.codehaus.groovy:groovy-all:2.1.3'
            providedCompile group: 'com.liferay.portal', name: 'portal-service', version: liferayVersion
            providedCompile group: 'com.liferay.portal', name: 'util-bridges', version: liferayVersion
            providedCompile group: 'com.liferay.portal', name: 'util-taglib', version: liferayVersion
            providedCompile group: 'com.liferay.portal', name: 'util-java', version: liferayVersion
            providedCompile group: 'javax.portlet', name: 'portlet-api', version: '2.0'
            providedCompile group: 'javax.servlet', name: 'servlet-api', version: '2.4'
            providedCompile group: 'javax.servlet.jsp', name: 'jsp-api', version: '2.0'
            compile group: 'org.hibernate', name: 'hibernate-validator', version: '5.0.1.Final'
            compile group: 'com.google.code.morphia', name: 'morphia', version: '0.99'

            compile project.fileTree (dir: 'lib', includes: ['*.jar'])
            providedCompile project.fileTree(dir: 'provided_lib', includes: ['*.jar'])
        }

        project.task('version') << {
            println project.viking.portal
            println project.viking.portletName
        }

		project.task(type: Zip, "sitebuilder-create-zip") {
			archiveName "sitebuilder.zip"
			from "sitebuilder"
		}


        def portletPluginPaths = ConfUtils.getPortletPluginPaths(project)
        
        project.sourceSets {
            main {
                groovy {
                    srcDirs = ['viking'] + portletPluginPaths
                    exclude "viking/views"
                }
                resources {
                    srcDirs = ['conf', 'resources']
                }
            }
        }

        project.war {
            dependsOn 'process-files'
            dependsOn 'generate-config'
            dependsOn 'add-friendlyURLs'
            
            from 'public'
            exclude 'coffee'
            from "$project.buildDir/compiled_coffee"

            webInf {
                from "$project.buildDir/urlmappings"
                into "classes/urlmappings"
            }

            webInf {
                from "i18n"
                into "classes/content"
            }

            webInf {
                from "$project.buildDir/conf"
            }

            webInf {
                from "$project.buildDir/$ProcessFiles.PROCESSED_VIEWS_FOLDER"
                into "views"
            }

            for ( portletPath in portletPluginPaths ) {
                webInf {
                    from "$portletPath/views"
                    into "views"
                }
            }			

            webXml = project.file('.templates/web.xml')
        }

        if (project.hasProperty("env") && project.env.toLowerCase() == "prod") {
            project.war.rootSpec.exclude "dev.conf"
        }

        def newProject = project.tasks.create(VIKING_PROJECT_TASK_NAME, CreateProject.class)
        newProject.description = "Creates a new viking project."
        newProject.dependsOn('version')

        def addPortlet = project.tasks.create(VIKING_ADD_PORTLET_TASK_NAME, AddPortlet.class)
        addPortlet.description = "Add a new portlet to the project."

        def listPortlets = project.tasks.create(LIST_PORTLETS_TASK_NAME, ListPortlets.class)
        listPortlets.description = "Lists existing portlets."

        def complieCoffee = project.tasks.create(COMPILE_COFFEE_TASK_NAME, CompileCoffee.class)
        complieCoffee.description = "Compiles CoffeeScript files to Javascript.  Requires Coffeescript to be installed and present in the PATH."

        def generateConfig = project.tasks.create(GENERATE_CONFIGURATION_TASK_NAME, GenerateConfig.class)
        generateConfig.description = "Generates config files required by Liferay from viking configuration."
        generateConfig.dependsOn('compile-coffee')

        def processFiles = project.tasks.create(PROCESS_FILES_TASK_NAME, ProcessFiles.class)
        processFiles.description = "Process files to replace viking prefixes and suffixes, like __ for portletId."

        def addFrienldyUrls = project.tasks.create(ADD_FRIENDLY_URLS, AddFriendlyURLs.class)
        addFrienldyUrls.description = "Adds friendly URL mappings for all the portlets."

        def deploy = project.tasks.create(DEPLOY_TASK_NAME, Deploy.class)
        deploy.description = "Deploys war to local environment using the defined deployDir."
        deploy.dependsOn('war')
 
        def copyAllDependencies = project.tasks.create(COPY_ALL_DEPENDENCIES_TASK_NAME, CopyAllDependencies.class)
		copyAllDependencies.description = "Copy all project dependencies into the project's build folder."

		def buildSite = project.tasks.create(BUILD_SITE_TASK_NAME, BuildSite.class)
		buildSite.dependsOn("sitebuilder-create-zip")
		buildSite.description = "Builds a site based on sitebuilder/sites.groovy contents"

	}
}










