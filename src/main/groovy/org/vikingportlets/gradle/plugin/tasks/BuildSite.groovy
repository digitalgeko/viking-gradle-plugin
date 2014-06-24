package org.vikingportlets.gradle.plugin.tasks

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: juanitoramonster
 * Date: 7/12/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
class BuildSite extends DefaultTask {


    @TaskAction
    def build() {
		def siteBuilderConfFile = new File("$project.projectDir/conf/sitebuilder.conf")
		def sitebuilderConf = new ConfigSlurper().parse(siteBuilderConfFile.toURI().toURL())
        def zipFile = new File("$project.buildDir/distributions/sitebuilder.zip")
		def http = new HTTPBuilder( sitebuilderConf.host )

		http.headers['Authorization'] = 'Basic ' + "$sitebuilderConf.user:$sitebuilderConf.pass".toString().bytes.encodeBase64().toString()
		http.request(Method.POST) { req ->
			uri.path = '/c/portal/build-site'
			requestContentType = 'multipart/form-data'
			req.entity = MultipartEntityBuilder.create().addBinaryBody("zipFile", zipFile).build()

			response.success = { resp, text ->
				println "resp.status:$resp.status"
			}
		}

	}

}
