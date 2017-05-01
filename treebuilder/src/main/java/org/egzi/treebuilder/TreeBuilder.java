package org.egzi.treebuilder;

/**
 * Sample tree builder
 * @param <K> type for node ID
 * @param <V> type for node values
 */
public class TreeBuilder<K, V> {

    private TreeNodeLocator<K, V> treeNodeLocator;

    private boolean failIfUnmappedElementsFound = true;

    TreeBuilder() {

    }


    /**
     * Set TableLocator to discover new nodes
     * @param treeNodeLocator source locator
     * @return current treebuilder
     */
    public TreeBuilder<K, V> useTreeNodeLocator(TreeNodeLocator<K, V> treeNodeLocator) {
        this.treeNodeLocator = treeNodeLocator;
        return this;
    }

    /**
     * Set <b>true</b> and treebuilder will raise an {@see TreeConstructException} exception if after tree construction
     * unadded node will be find
     * @param failIfUnmappedElementsFound
     * @return
     */
    public TreeBuilder<K, V> setFailIfUnmappedElementsFound(boolean failIfUnmappedElementsFound) {
        this.failIfUnmappedElementsFound = failIfUnmappedElementsFound;
        return this;
    }

    /**
     * Build tree with passed parameters
     * @return {@see Forest} entity
     */
    public Forest<K, V> buildTrees() {
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
