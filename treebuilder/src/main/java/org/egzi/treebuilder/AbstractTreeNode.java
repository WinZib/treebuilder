package org.egzi.treebuilder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Егор on 14.09.2016.
 */
public abstract class AbstractTreeNode<K, V> implements TreeNode<K, V> {
    private Collection<TreeNode<K, V>> childs = new ArrayList<TreeNode<K, V>>();

    public Collection<TreeNode<K, V>> getChilds() {
        return childs;
    }

    public void addChildNode(TreeNode<K, V> node) {
        childs.add(node);
    }
}
