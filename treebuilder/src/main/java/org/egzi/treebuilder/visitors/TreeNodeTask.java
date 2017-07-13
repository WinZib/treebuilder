package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * @param <K>
 * @param <V>
 */
public class TreeNodeTask<K, V> extends RecursiveAction {
    private TreeNode<K, V> treeNode;
    private Visitor<K, V> visitor;

    public TreeNodeTask(final TreeNode<K, V> treeNode, final Visitor<K, V> visitor) {
        this.treeNode = treeNode;
        this.visitor = visitor;
    }

    public void compute() {
        visitor.doVisit(treeNode);

        ArrayList<ForkJoinTask> tasks = new ArrayList<>();

        for (TreeNode<K, V> treeNode : treeNode.getChilds()) {
            tasks.add(new TreeNodeTask<K, V>(treeNode, visitor));
        }

        invokeAll(tasks);
    }
}
