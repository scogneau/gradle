/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.plugins

import org.gradle.api.Project
import org.gradle.api.tasks.application.CreateStartScripts
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.bundling.Tar
import org.gradle.util.HelperUtil
import org.gradle.util.Matchers
import spock.lang.Specification
import org.gradle.api.tasks.Sync
import org.gradle.api.file.CopySpec

class LibraryPluginTest extends Specification {
    private final Project project = HelperUtil.createRootProject();
    private final LibraryPlugin plugin = new LibraryPlugin();

    def "applies JavaPlugin and adds convention object with default values"() {
        when:
        plugin.apply(project)

        then:
        project.plugins.hasPlugin(JavaPlugin.class)
        project.convention.getPlugin(LibraryPluginConvention.class) != null
        project.distributionName == project.name
        project.distribution instanceof CopySpec
    }

    def "adds distZip task to project"() {
        when:
        plugin.apply(project)

        then:
        def task = project.tasks[DistPlugin.TASK_DIST_ZIP_NAME]
        task instanceof Zip
        task.archiveName == "${project.name}.zip"
    }
	
	def "adds distTar task to project"() {
		when:
		plugin.apply(project)

		then:
		def task = project.tasks[DistPlugin.TASK_DIST_TAR_NAME]
		task instanceof Tar
		task.archiveName == "${project.name}.tar"
	}

    public void "distributionName is configurable"() {
        when:
        plugin.apply(project)
        project.distributionName = "SuperApp";

        then:

        def distZipTask = project.tasks[DistPlugin.TASK_DIST_ZIP_NAME]
        distZipTask.archiveName == "SuperApp.zip"
		
		def distTarTask = project.tasks[DistPlugin.TASK_DIST_TAR_NAME]
		distTarTask.archiveName == "SuperApp.tar"
    }
    

}
