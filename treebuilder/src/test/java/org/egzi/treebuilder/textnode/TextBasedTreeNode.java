package org.egzi.treebuilder.textnode;

import org.egzi.treebuilder.AbstractTreeNode;

/**
 * Created by Егор on 20.11.2016.
 */
public class TextBasedTreeNode extends AbstractTreeNode<Integer, String> {
    public TextBasedTreeNode(String id, String data) {
        this(Integer.valueOf(id), data);
    }

    public TextBasedTreeNode(String id, String parentId, String data) {
        this(Integer.valueOf(id), Integer.valueOf(parentId), data);
    }

    public TextBasedTreeNode(Integer id, String data) {
        this(id, (Integer) null, data);
    }

    public TextBasedTreeNode(Integer id, Integer parentId, String data) {
        super(id, data, parentId);
    }
}
