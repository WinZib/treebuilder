package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * It represents implementation of {@link RecursiveAction} for {@link java.util.concurrent.ForkJoinPool}
 * Logic of this action is performed by {@link Visitor}
 *
 * @param <K>
 * @param <V>
 */
public class TreeNodeTask<K, V> extends RecursiveAction {

    private final TreeNode<K, V> treeNode;
    private final Visitor<K, V> visitor;
    private final Predicate<? super TreeNode<K, V>> taskFilterRule;


    public TreeNodeTask(TreeNode<K, V> treeNode, final Visitor<K, V> visitor,
                        Predicate<? super TreeNode<K, V>> taskFilterRule) {
        this.treeNode = treeNode;
        this.visitor = visitor;
        this.taskFilterRule = taskFilterRule;
    }

    public void compute() {
        visitor.doVisit(treeNode);
        invokeAll(
                treeNode.getChildren().
                        stream().
                        filter(taskFilterRule).
                map(t -> (new TreeNodeTask<>(t, visitor, taskFilterRule))).
                collect(Collectors.toCollection(ArrayList::new))
        ).forEach(ForkJoinTask::join);
    }

}




