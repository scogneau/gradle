/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.nativecode.base;

import org.gradle.api.Incubating;

/**
 * A Native Binary that is a physical representation of a Library component.
 */
@Incubating
public interface LibraryBinary extends NativeBinary {
    Library getComponent();

    /**
     * Converts this binary into a {@link NativeDependencySet}, for consumption in some other binary.
     */
    NativeDependencySet getAsNativeDependencySet();
}