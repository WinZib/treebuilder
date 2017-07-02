package org.egzi.treebuilder.samples.text;

import com.google.common.util.concurrent.MoreExecutors;
import junit.framework.TestCase;
import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.Tree;
import org.egzi.treebuilder.TreeBuilderFactory;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.visitors.AsyncWalker;
import org.egzi.treebuilder.visitors.Visitor;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Егор on 03.01.2017.
 */
public class TextTreeTests {
    private void startTest(final String data, final ExecutorService executorService, final String expectedString) {
        Forest<Integer, String> forest = TreeBuilderFactory.<Integer, String>newBuilder().
                useTreeNodeLocator(new TextTreeNodeLocator(data)).buildTrees();

        AsyncWalker<Integer, String> walker = new AsyncWalker<>();

        final StringBuilder result = new StringBuilder("");

        walker.setVisitor(new Visitor<Integer, String>() {
            @Override
            public <R> R doVisit(TreeNode<Integer, String> node) {
                result.append(node.get());
                return null;
            }
        });
        try {
            walker.setExecutorService(executorService);
            for (Tree<Integer, String> tree : forest.getTrees())
                walker.walk(tree.getRoot());
        } finally {
            try {
                executorService.awaitTermination(10L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.shutdownNow();
        }
        TestCase.assertEquals(expectedString, result.toString());
    }

    @Test
    public void simpleTest() {
        startTest("1 A\r\n2 1 B\r\n3 2 C\r\n4 3 D\r\n", Executors.newSingleThreadExecutor(), "ABCD");
    }


    @Test
    public void simpleSameThread() {
        startTest("1 A\r\n2 1 B\r\n3 2 C\r\n4 3 D\r\n", MoreExecutors.sameThreadExecutor(), "ABCD");
    }

    @Test
    public void multiThreadTest() {
        startTest("1 A\r\n2 1 B\r\n3 1 C\r\n4 3 D\r\n", Executors.newFixedThreadPool(2, Executors.defaultThreadFactory()), "ACDB");
    }
}
