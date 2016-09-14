package org.egzi.treebuilder;

import java.util.*;

/**
 * Created by Егор on 14.09.2016.
 */
public class Forest<K, V> {
    private Map<K, TreeNode<K,V>> treeRegister = new HashMap<K, TreeNode<K, V>>();

    private Map<K, Tree<K, V>> trees = new HashMap<K, Tree<K, V>>();

    private Map<K, Set<TreeNode<K,V>>> pendingNodes = new HashMap<K, Set<TreeNode<K, V>>>();

    Forest() {
    }

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
                addPedningParent(treeNode);
            } else {
                parentNode.addChildNode(treeNode);
            }
        }

        treeRegister.put(treeNode.getId(), treeNode);

        refreshPending(treeNode);
    }

    private void addPedningParent(TreeNode<K, V> treeNode) {
        Set<TreeNode<K, V>> pendings = pendingNodes.get(treeNode.getParentId());

        if (pendings == null) {
            pendings = new HashSet<TreeNode<K, V>>();
            pendingNodes.put(treeNode.getParentId(), pendings);
        }

        pendings.add(treeNode);
    }

    public Collection<Tree<K, V>> getTrees() {
        return trees.values();
    }

    private void addTree(final TreeNode<K ,V> rootNode) {
        trees.put(rootNode.getId(), new Tree<K, V>(rootNode));
    }

    private void refreshPending(TreeNode<K, V> newNode) {
        Set<TreeNode<K, V>> pended = pendingNodes.get(newNode.getId());

        if (pended == null) {
            //nothing to update
            return;
        }

        for (TreeNode<K, V> node : pended)
            newNode.addChildNode(node);

        pendingNodes.remove(newNode.getId());
    }

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
