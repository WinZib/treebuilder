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

import java.util.ArrayList;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * It represents implementation of {@link RecursiveAction} for {@link java.util.concurrent.ForkJoinPool}
 * Logic of this action is performed by {@link Visitor}
 *
 * @param <K>
 * @param <V>
 */
public class TreeNodeTask<K, V> extends RecursiveAction {

    private final TreeNode<K, V> treeNode;
    private final Visitor<K, V> visitor;
    private final Predicate<? super TreeNode<K, V>> taskFilterRule;


    public TreeNodeTask(TreeNode<K, V> treeNode, final Visitor<K, V> visitor,
                        Predicate<? super TreeNode<K, V>> taskFilterRule) {
        this.treeNode = treeNode;
        this.visitor = visitor;
        this.taskFilterRule = taskFilterRule;
    }

    public void compute() {
        visitor.doVisit(treeNode);
        invokeAll(
                treeNode.getChildren().
                        stream().
                        filter(taskFilterRule).
                map(t -> (new TreeNodeTask<>(t, visitor, taskFilterRule))).
                collect(Collectors.toCollection(ArrayList::new))
        ).forEach(ForkJoinTask::join);
    }

}




