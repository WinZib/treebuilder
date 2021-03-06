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

/**
 * Listener react on error when {@see Visitor} doVisit fails on some treenode
 */
public interface OnErrorListener {
    /**
     * Execute logic on <b>treeNode</b> during iteration
     * @param treeNode current {@see TreeNode}
     * @param <K> type of tree id
     * @param <V> type of tree value
     */
    <K,V> void report(TreeNode<K,V> treeNode);
}
