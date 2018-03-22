package org.egzi.treebuilder.textnode;

/*
 * #%L
 * treebuilder
 * %%
 * Copyright (C) 2018 WinZib (winzib@yandex.ru)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import junit.framework.TestCase;
import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.Tree;
import org.egzi.treebuilder.TreeBuilderFactory;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.visitors.AsyncWalker;
import org.egzi.treebuilder.visitors.Visitor;
import org.junit.Test;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

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

        forest.getTrees().stream().map(tree -> walker.walk(tree.getRoot())).forEach(ForkJoinTask::join);

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
        startTest("1 A\r\n2 1 B\r\n3 2 C\r\n4 3 D\r\n", new ForkJoinPool(2), "ABCD");
    }
}
