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
 * Class described visiting of Tree node
 * @param <K> type of TreeNode id
 * @param <V> type of TreeNode value
 */
public interface Visitor<K, V> {
    /**
     * Do some action in node <b>node</b>
     * @param node current node
     * @param <R> type of Result
     * @return result of processing
     */
    <R> R doVisit(TreeNode<K,V> node);

}
