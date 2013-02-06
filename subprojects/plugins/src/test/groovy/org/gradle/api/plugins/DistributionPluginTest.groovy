/*
 * Copyright 2012 the original author or authors.
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
import org.gradle.api.distribution.plugins.DistributionPlugin
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.distribution.Distribution
import org.gradle.api.distribution.DistributionsContainer
import org.gradle.internal.reflect.Instantiator;
import org.gradle.util.HelperUtil
import spock.lang.Specification;

class DistributionPluginTest extends Specification{
	private final Project project = HelperUtil.createRootProject();
	private final DistributionPlugin plugin = new DistributionPlugin(project.services.get(Instantiator));
	
	def "adds convention object with default values"() {
		when:
		plugin.apply(project)

		then:
		project.extensions.getByType(DistributionsContainer.class) != null

	}

	def "adds distZip task to project"() {
		when:
		plugin.apply(project)

		then:
		def task = project.tasks[DistributionPlugin.TASK_DIST_ZIP_NAME]
		task instanceof Zip
		task.archiveName == "${project.distributions[DistributionPlugin.MAIN_DISTRIBUTION_NAME].baseName}.zip"
	}


	public void "distribution name is configurable"() {
		when:
		plugin.apply(project)
		project.distributions[DistributionPlugin.MAIN_DISTRIBUTION_NAME].baseName = "SuperApp";

		then:
		def distZipTask = project.tasks[DistributionPlugin.TASK_DIST_ZIP_NAME]
		distZipTask.archiveName == "SuperApp.zip"
	}

    public void "default distribution baseName is project name "() {
        when:
        plugin.apply(project)

        then:
        Distribution mainDistribution = project.distributions[DistributionPlugin.MAIN_DISTRIBUTION_NAME]
        mainDistribution.name == DistributionPlugin.MAIN_DISTRIBUTION_NAME;
        mainDistribution.baseName == project.name;

    }


}
