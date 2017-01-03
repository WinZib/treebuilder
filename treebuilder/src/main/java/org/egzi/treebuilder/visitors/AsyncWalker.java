package org.egzi.treebuilder.visitors;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.egzi.treebuilder.TreeNode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Егор on 27.09.2016.
 */
public class AsyncWalker<K,V>  {
    private ListeningExecutorService executorService;

    private Visitor<K,V> visitor;

    private OnErrorListener onErrorListener;

    public AsyncWalker() {

    }

    public void setExecutorService(final ExecutorService executorService) {
        this.executorService = MoreExecutors.listeningDecorator(executorService);
    }

    public void setExecutorService(final ListeningExecutorService listeningExecutorService) {
        this.executorService = listeningExecutorService;
    }

    public void setForkJoinPool(final ForkJoinPool forkJoinPool) {
        setExecutorService(forkJoinPool);
    }

    public void setVisitor(final Visitor<K,V> visitor) {
        this.visitor = visitor;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void walk(final TreeNode<K,V> treeNode) {
        TreeNodeTask<K, V> task = constructVisitTask(treeNode);
        Futures.addCallback(executorService.submit(task), new FutureCallback<Object>() {
            public void onSuccess(Object o) {
                for (TreeNode<K,V> childTreeNode : treeNode.getChilds()) {
                    walk(childTreeNode);
                }
            }
            public void onFailure(Throwable throwable) {
                if (onErrorListener != null)
                    onErrorListener.report(treeNode);
            }
        });
    }

    private TreeNodeTask<K,V> constructVisitTask(TreeNode<K,V> treeNode) {
        return new TreeNodeTask<K, V>(treeNode, visitor);
    }


}
