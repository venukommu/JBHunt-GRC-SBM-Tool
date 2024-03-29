/*
 * Copyright 2021 - 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.sbm.project.resource;

import org.springframework.sbm.project.resource.filter.GenericTypeListFilter;
import org.springframework.sbm.project.resource.filter.ProjectResourceFinder;
import org.springframework.sbm.project.resource.filter.ResourceFilterException;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenericTypeFilter<T> implements ProjectResourceFinder<Optional<T>> {

    @Getter
    private final Class<T> type;

    public GenericTypeFilter(Class<T> type) {
        this.type = type;
    }

    @Override
    public Optional<T> apply(ProjectResourceSet projectResourceSet) {
        List<T> collect = projectResourceSet.stream()
                .filter(pr -> type.isAssignableFrom(pr.getClass()))
                .map(type::cast)
                .collect(Collectors.toList());
        if (collect.size() > 1) {
            throw new ResourceFilterException(String.format("Found more than one resource of type '%s'. Use %s instead.", type.getClass(), GenericTypeListFilter.class));
        }
        return collect.isEmpty() ? Optional.empty() : Optional.of(collect.get(0));
    }
}
