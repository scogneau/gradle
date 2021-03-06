/*
 * Copyright 2014 the original author or authors.
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

package org.gradle.runtime.jvm.internal.plugins;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.runtime.jvm.JvmLibrary;
import org.gradle.runtime.jvm.JvmComponentExtension;

public class DefaultJvmComponentExtension implements JvmComponentExtension {
    private final NamedDomainObjectContainer<JvmLibrary> libraries;

    public DefaultJvmComponentExtension(NamedDomainObjectContainer<JvmLibrary> libraries) {
        this.libraries = libraries;
    }

    public NamedDomainObjectContainer<JvmLibrary> getLibraries() {
        return libraries;
    }

    public void libraries(Action<? super NamedDomainObjectContainer<? super JvmLibrary>> action) {
        action.execute(libraries);
    }
}
