package org.egzi.treebuilder.samples.text;

import org.egzi.treebuilder.AbstractTreeNode;

/**
 * Created by Егор on 20.11.2016.
 */
public class TextBasedTreeNode extends AbstractTreeNode<Integer, String> {
    private Integer id;
    private Integer parentId;
    private String data;

    public TextBasedTreeNode(String id, String data) {
        this(Integer.valueOf(id), data);
    }

    public TextBasedTreeNode(String id, String parentId, String data) {
        this(Integer.valueOf(id), Integer.valueOf(parentId), data);
    }

    public TextBasedTreeNode(Integer id, String data) {
        this(id, (Integer)null, data);
    }

    public TextBasedTreeNode(Integer id, Integer parentId, String data) {
        this.id = id;
        this.parentId = parentId;
        this.data = data;
    }

    @Override
    public Integer getParentId() {
        return parentId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getObject() {
        return data;
    }
}
