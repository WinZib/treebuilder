package org.egzi.treebuilder;

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


/**
 * Abstraction to find next TreeNode
 * @param <K> key type in a tree
 * @param <V> value type in a tree
 */
public interface TreeNodeLocator<K, V> {
    /**
     * Return next TreeNode instance from some source
     * @return {@see TreeNode} instance
     */
    TreeNode<K, V> getNextTreeNode();

    /**
     * Identifies presence of another TreeNode instances
     * @return <b>true</b> if there's another tree instances
     *         <b>false</b> otherwise
     */
    boolean hasMore();
}
