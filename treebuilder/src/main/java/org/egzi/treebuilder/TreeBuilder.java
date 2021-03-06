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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Sample tree builder
 *
 * @param <K> type for node ID
 * @param <V> type for node values
 */
public class TreeBuilder<K, V> {

    private List<TreeNodeLocator<K, V>> treeNodeLocators = new ArrayList<>();

    private boolean failIfUnmappedElementsFound = true;

    TreeBuilder() {

    }


    /**
     * Set TableLocator to discover new nodes
     *
     * @param treeNodeLocator source locator
     * @return current treebuilder
     */
    public TreeBuilder<K, V> useTreeNodeLocator(TreeNodeLocator<K, V> treeNodeLocator) {
        Objects.requireNonNull(treeNodeLocator, "Locator can't be null please specify correct one");
        treeNodeLocators.add(treeNodeLocator);
        return this;
    }


    public TreeBuilder<K, V> useTreeNodeLocators(Collection<? extends TreeNodeLocator< K, V>> treeNodeLocator) {
        Objects.requireNonNull(treeNodeLocator, "Locator can't be null please specify correct one");
        treeNodeLocators.addAll(treeNodeLocator);
        return this;
    }

    /**
     * Set <b>true</b> and treebuilder will raise an {@see TreeConstructException} exception if after tree construction
     * unadded node will be find
     *
     * @param failIfUnmappedElementsFound
     * @return
     */
    public TreeBuilder<K, V> setFailIfUnmappedElementsFound(boolean failIfUnmappedElementsFound) {
        this.failIfUnmappedElementsFound = failIfUnmappedElementsFound;
        return this;
    }

    /**
     * Build tree with passed parameters
     *
     * @return {@see Forest} entity
     */
    public Forest<K, V> buildTrees() {
        Forest<K, V> forest = new Forest<K, V>();
        for (TreeNodeLocator<K, V> treeNodeLocator : treeNodeLocators) {
            addLocatorsNodes(forest, treeNodeLocator);
        }

        checkElementMapping(forest);

        return forest;
    }

    private void addLocatorsNodes(Forest<K, V> forest, TreeNodeLocator<K, V> treeNodeLocator) {
        while (treeNodeLocator.hasMore()) {
            TreeNode<K, V> treeNode = treeNodeLocator.getNextTreeNode();
            forest.addTreeNode(treeNode);
        }
    }

    private void checkElementMapping(Forest<K, V> forest) {
        forest.clearRegister();
        forest.markBuilded();
        if (failIfUnmappedElementsFound && forest.getUnfoundNodeID().size() > 0)
            throw new TreeConstructException("Didn't find " + forest.getUnfoundNodeID() + " elements. Tree can't be constructed");
    }
}
