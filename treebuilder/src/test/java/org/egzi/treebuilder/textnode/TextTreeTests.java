package org.egzi.treebuilder.textnode;

import junit.framework.TestCase;
import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.Tree;
import org.egzi.treebuilder.TreeBuilderFactory;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.visitors.AsyncWalker;
import org.egzi.treebuilder.visitors.Visitor;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;

/**
 * Created by Егор on 03.01.2017.
 */
public class TextTreeTests {
    private void startTest(final String data, final ForkJoinPool executorService, final String expectedString) {
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
        for (Tree<Integer, String> tree : forest.getTrees())
            walker.walk(tree.getRoot());

        TestCase.assertEquals(expectedString, result.toString());
    }

    @Test
    public void simpleTest() {
        startTest("1 A\r\n2 1 B\r\n3 2 C\r\n4 3 D\r\n", new ForkJoinPool(1), "ABCD");
    }


    @Test
    public void simpleSameThread() {
        startTest("1 A\r\n2 1 B\r\n3 2 C\r\n4 3 D\r\n", new ForkJoinPool(1), "ABCD");
    }

    @Test
    public void multiThreadTest() {
        startTest("1 A\r\n2 1 B\r\n3 1 C\r\n4 3 D\r\n", new ForkJoinPool(2), "ABCD");
    }
}
