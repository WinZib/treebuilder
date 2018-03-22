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
import org.egzi.treebuilder.TreeNode;

import java.util.Map;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by egorz on 6/6/2017.
 */
public interface Walker<K,V> {
    void setVisitor(final Visitor<K, V> visitor);

    ForkJoinTask<Void> walk(TreeNode<K,V> treeNode);

    Map<K, ForkJoinTask<Void>> walk(Forest<K, V> forest);

}
