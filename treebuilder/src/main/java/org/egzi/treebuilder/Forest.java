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


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * Container for trees
 *
 * @param <K> key type
 * @param <V> value type
 */
public class Forest<K, V> {

    private Map<K, TreeNode<K, V>> treeRegister = new HashMap<>();

    // due to opportunity to add already visited node to on the fly.
    private Map<K, Tree<K, V>> trees = new ConcurrentHashMap<>();

    private Map<K, Set<TreeNode<K, V>>> pendingNodes = new HashMap<>();

    private boolean builded = false;

    Forest() {
    }

    /**
     * Add node to the forest entity.
     * Node with <b>null</b> as a parent node will be treated as a root node for the new tree
     * If there's no node with <b>parentID</b> key in forest than current one will be add to pending list
     * Otherwise if node with ID equals parentId from treeNode than current node will be added to this tree and pending
     * nodes with parentId equal to getId of current node will be added too
     *
     * @param treeNode new node
     */
    public void addTreeNode(TreeNode<K, V> treeNode) {
        if (treeNode.getId() == null)
            throw new IllegalArgumentException("id of tree's element can't be null");
        //check on parent node
        if (treeNode.getParentId() == null) {
            //check for duplicate node
            if (trees.containsKey(treeNode.getId()))
                throw new TreeConstructException("Duplicate of node " + treeNode + ". Construction will be terminated");
            addTree(treeNode);
        } else {
            Optional<TreeNode<K, V>> parentNode;
            if (!builded) {
                parentNode = Optional.ofNullable(treeRegister.get(treeNode.getParentId()));
            } else {
                parentNode = nodeStream().filter(node -> node.getId().equals(treeNode.getParentId())).findFirst();
            }
            if (!parentNode.isPresent()) {
                addPendingParent(treeNode);
            } else {
                connectNodes(parentNode.get(), treeNode);
            }
        }

        if (!builded) {
            treeRegister.put(treeNode.getId(), treeNode);
        }

        refreshPending(treeNode);
    }

    /**
     * Add node to pending nodes list
     *
     * @param treeNode pending node
     */
    private void addPendingParent(TreeNode<K, V> treeNode) {
        Set<TreeNode<K, V>> pendings = pendingNodes.computeIfAbsent(treeNode.getParentId(), k -> new HashSet<TreeNode<K, V>>());
        pendings.add(treeNode);
    }

    /**
     * Get all available trees
     *
     * @return available trees
     */
    public Collection<Tree<K, V>> getTrees() {
        return trees.values();
    }

    /**
     * Add tree to forest
     *
     * @param rootNode new tree
     */
    private void addTree(final TreeNode<K, V> rootNode) {
        trees.put(rootNode.getId(), new Tree<>(rootNode));
    }

    /**
     * Refresh nodes after adding newNode
     *
     * @param newNode new Node
     */
    private void refreshPending(TreeNode<K, V> newNode) {
        Set<TreeNode<K, V>> pended = pendingNodes.get(newNode.getId());

        if (pended == null) {
            //nothing to update
            return;
        }

        for (TreeNode<K, V> node : pended) {
            connectNodes(newNode, node);
        }

        pendingNodes.remove(newNode.getId());
    }

    /**
     * Link two nodes by Parent-Child relation
     *
     * @param parent
     * @param child
     */
    private void connectNodes(TreeNode<K, V> parent, TreeNode<K, V> child) {
        parent.addChildNode(child);
        child.setParent(parent);
    }

    /**
     * Get set of unadded nodes
     *
     * @return set of nodes
     */
    public Set<K> getUnfoundNodeID() {
        return pendingNodes.keySet();
    }

    /**
     * clear temp register. should be invoked after build finish
     */
    void clearRegister() {
        treeRegister.clear();
    }

    void markBuilded() {
        builded = true;
    }

    /**
     * Flat map stream of nodes
     *
     * @return node stream
     */
    public Stream<TreeNode<K, V>> nodeStream() {
        return getTrees().stream()
                .flatMap(Tree::treeNodeStream);
    }
}
