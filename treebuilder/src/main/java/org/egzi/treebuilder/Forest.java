package org.egzi.treebuilder;

import java.util.*;

/**
 * Container for trees
 * @param <K> key type
 * @param <V> value type
 */
public class Forest<K, V> {
    private Map<K, TreeNode<K,V>> treeRegister = new HashMap<K, TreeNode<K, V>>();

    private Map<K, Tree<K, V>> trees = new HashMap<K, Tree<K, V>>();

    private Map<K, Set<TreeNode<K,V>>> pendingNodes = new HashMap<K, Set<TreeNode<K, V>>>();

    Forest() {
    }

    /**
     * Add node to the forest entity.
     * Node with <b>null</b> as a parent node will be treated as a root node for the new tree
     * If there's no node with <b>parentID</b> key in forest than current one will be add to pending list
     * Otherwise if node with ID equals parentId from treeNode than current node will be added to this tree and pending
     * nodes with parentId equal to getId of current node will be added too
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

            TreeNode<K, V> parentNode = treeRegister.get(treeNode.getParentId());

            if (parentNode == null) {
                addPendingParent(treeNode);
            } else {
                connectNodes(parentNode, treeNode);
            }
        }

        treeRegister.put(treeNode.getId(), treeNode);

        refreshPending(treeNode);
    }

    /**
     * Add node to pending nodes list
     * @param treeNode pending node
     */
    private void addPendingParent(TreeNode<K, V> treeNode) {
        Set<TreeNode<K, V>> pendings = pendingNodes.get(treeNode.getParentId());

        if (pendings == null) {
            pendings = new HashSet<TreeNode<K, V>>();
            pendingNodes.put(treeNode.getParentId(), pendings);
        }

        pendings.add(treeNode);
    }

    /**
     * Get all available trees
     * @return available trees
     */
    public Collection<Tree<K, V>> getTrees() {
        return trees.values();
    }

    /**
     * Add tree to forest
     * @param rootNode new tree
     */
    private void addTree(final TreeNode<K ,V> rootNode) {
        trees.put(rootNode.getId(), new Tree<K, V>(rootNode));
    }

    /**
     * Refresh nodes after adding newNode
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
     * @param parent
     * @param child
     */
    private void connectNodes(TreeNode<K, V> parent, TreeNode<K, V> child) {
        parent.addChildNode(child);
        child.setParent(parent);
    }

    /**
     * Get set of unadded nodes
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
}
