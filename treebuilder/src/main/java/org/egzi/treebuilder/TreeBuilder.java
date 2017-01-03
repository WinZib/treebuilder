package org.egzi.treebuilder;

/**
 * Created by Егор on 14.09.2016.
 */
public class TreeBuilder<K, V> {

    private TreeNodeLocator<K, V> treeNodeLocator;

    private boolean failIfUnmappedElementsFound = true;

    TreeBuilder() {

    }



    public TreeBuilder<K, V> useTreeNodeLocator(TreeNodeLocator<K, V> treeNodeLocator) {
        this.treeNodeLocator = treeNodeLocator;
        return this;
    }

    public TreeBuilder<K, V> setFailIfUnmappedElementsFound(boolean failIfUnmappedElementsFound) {
        this.failIfUnmappedElementsFound = failIfUnmappedElementsFound;
        return this;
    }

    public Forest buildTrees() {
        Forest<K, V> forest = new Forest<K, V>();

        while (treeNodeLocator.hasMore()) {
            TreeNode<K, V> treeNode = treeNodeLocator.getNextTreeNode();
            forest.addTreeNode(treeNode);
        }

        forest.clearRegister();

        if (failIfUnmappedElementsFound && forest.getUnfoundNodeID().size() > 0)
            throw new TreeConstructException("Didn't find " + forest.getUnfoundNodeID() + " elements. Tree can't be constructed");

        return forest;
    }
}
