package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.TreeNode;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class AsyncWalker<K, V> extends AbstractWalker<K, V> {
    private ForkJoinPool forkJoinPool;

    private OnErrorListener onErrorListener;

    public AsyncWalker() {
    }

    public void setForkJoinPool(final ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void walk(final TreeNode<K, V> treeNode) {
        forkJoinPool.execute(constructVisitTask(treeNode));
    }

    public void await(long time, TimeUnit timeUnit) {
        forkJoinPool.awaitQuiescence(time, timeUnit);
    }

    private TreeNodeTask<K, V> constructVisitTask(TreeNode<K, V> treeNode) {
        return new TreeNodeTask<>(treeNode, getVisitor());
    }
}
