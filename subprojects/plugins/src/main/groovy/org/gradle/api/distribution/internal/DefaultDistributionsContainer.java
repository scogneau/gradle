/*
 * Copyright 2009 the original author or authors.
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
package org.gradle.api.distribution.internal;

import groovy.lang.Closure;
import org.gradle.api.file.CopySpec;
import org.gradle.api.internal.AbstractNamedDomainObjectContainer;
import org.gradle.api.distribution.Distribution;
import org.gradle.api.distribution.DistributionsContainer;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.file.copy.CopySpecImpl;
import org.gradle.internal.reflect.Instantiator;

/**
 * Default implementation for {@link org.gradle.api.distribution.DistributionsContainer}
 * @author scogneau
 */
public class DefaultDistributionsContainer extends AbstractNamedDomainObjectContainer<Distribution> implements DistributionsContainer {

    private final FileResolver fileResolver;

    protected DefaultDistributionsContainer(Class<Distribution> type, Instantiator instantiator, FileResolver fileResolver) {
        super(type, instantiator);
        this.fileResolver = fileResolver;
    }

    protected Distribution doCreate(String name) {
        Distribution distribution = new DefaultDistribution(name);
        CopySpec copySpec = new CopySpecImpl(fileResolver);
        distribution.setContents((CopySpecImpl) copySpec.from("src/"+name+"/dist"));
        return distribution;
    }

    public Distribution add(String name) {
        return doCreate(name);
    }

    public Distribution add(String name, Closure configurationClosure) {
        Distribution distribution = create(name, configurationClosure);
        CopySpec copySpec = new CopySpecImpl(fileResolver);
        distribution.setContents((CopySpecImpl) copySpec.from("src/"+name+"/dist"));
        return distribution;
    }

}
