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


import org.egzi.treebuilder.TreeNode;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Predicate;

public class AsyncWalker<K, V> extends AbstractWalker<K, V> {

    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    private Predicate<TreeNode<K, V>> filterRule = ((Predicate<TreeNode<K, V>>) TreeNode::isVisited).negate();

    public AsyncWalker() {
    }

    public void setForkJoinPool(final ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
    }

    public ForkJoinTask<Void> walk(final TreeNode<K, V> treeNode) {
        if (treeNode.isVisited()) return null;
        return forkJoinPool.submit(constructVisitTask(treeNode));
    }


    private TreeNodeTask<K, V> constructVisitTask(TreeNode<K, V> treeNode) {
        return new TreeNodeTask<>(treeNode, getVisitor(), filterRule);
    }

    public AsyncWalker applyFilterRuleForChildrenNodes(Predicate<? super TreeNode<K, V>> filterRule) {
        this.filterRule.and(filterRule);
        return this;
    }


}




