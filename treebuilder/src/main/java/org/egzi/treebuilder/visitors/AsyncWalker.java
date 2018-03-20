package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Predicate;

public class AsyncWalker<K, V> extends AbstractWalker<K, V> {

    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    private Predicate<TreeNode<K, V>> filterRule = ((Predicate<TreeNode<K, V>>) TreeNode::isVisited).negate();

    public AsyncWalker() {
    }

    public void setForkJoinPool(final ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
    }

    public ForkJoinTask<Void> walk(final TreeNode<K, V> treeNode) {
        if (treeNode.isVisited()) return null;
        return forkJoinPool.submit(constructVisitTask(treeNode));
    }


    private TreeNodeTask<K, V> constructVisitTask(TreeNode<K, V> treeNode) {
        return new TreeNodeTask<>(treeNode, getVisitor(), filterRule);
    }

    public AsyncWalker applyFilterRuleForChildrenNodes(Predicate<? super TreeNode<K, V>> filterRule) {
        this.filterRule.and(filterRule);
        return this;
    }


}




