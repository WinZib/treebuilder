package org.egzi.treebuilder.visitors;

/*
 * #%L
 * treebuilder
 * %%
 * Copyright (C) 2018 WinZib (winzib@yandex.ru)
 * %%
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
 * #L%
 */


import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by egorz on 6/6/2017.
 */
public abstract class AbstractWalker<K, V> implements Walker<K, V> {

    private Visitor<K, V> visitor;

    public Map<K, ForkJoinTask<Void>> walk(Forest<K, V> forest) {
        return forest.getTrees()
                .stream()
                .map(Tree::getRoot)
                .collect(HashMap::new, (m, v)->m.put(v.getId(), walk(v)), HashMap::putAll);
    }
    public Visitor<K, V> getVisitor() {
        return visitor;
    }

    @Override
    public void setVisitor(Visitor<K, V> visitor) {
        this.visitor = visitor;
    }
}
