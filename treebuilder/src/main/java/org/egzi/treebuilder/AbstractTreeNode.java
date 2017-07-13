package org.egzi.treebuilder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Егор on 14.09.2016.
 */
public abstract class AbstractTreeNode<K, V> implements TreeNode<K, V> {

    private K id;

    private V value;

    private K parentId;

    private TreeNode<K, V> parent;

    private Collection<TreeNode<K, V>> childs = new ArrayList<TreeNode<K, V>>();

    public AbstractTreeNode(K id, K parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public AbstractTreeNode(K id, V value, K parentId) {
        this(id, parentId);
        this.value = value;
    }

    public Collection<TreeNode<K, V>> getChilds() {
        return childs;
    }

    public void addChildNode(TreeNode<K, V> node) {
        childs.add(node);
    }

    @Override
    public K getParentId() {
        return parentId;
    }

    @Override
    public TreeNode<K, V> getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode<K, V> parent) {
        this.parent = parent;
    }

    @Override
    public K getId() {
        return id;
    }

    @Override
    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;
    }
}
