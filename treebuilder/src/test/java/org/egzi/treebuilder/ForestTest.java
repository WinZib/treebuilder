package org.egzi.treebuilder;

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


import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by Егор on 14.09.2016.
 */
public class ForestTest {
    @Test
    public void testNullID() {
        Forest<Integer, Integer> forest = new Forest<Integer, Integer>();
        forest.addTreeNode(new IntegerTreeNode(null, 1));
        forest.addTreeNode(new IntegerTreeNode(1, 2));
        forest.addTreeNode(new IntegerTreeNode(2, 3));
        forest.addTreeNode(new IntegerTreeNode(4, 5));
        forest.addTreeNode(new IntegerTreeNode(3, 4));
        assertTrue(forest.getUnfoundNodeID().size() == 0);
    }

    @Test
    public void testUnfoundNodeIDs() {
        Forest<Integer, Integer> forest = new Forest<Integer, Integer>();
        forest.addTreeNode(new IntegerTreeNode(null, 1));
        forest.addTreeNode(new IntegerTreeNode(1, 2));
        forest.addTreeNode(new IntegerTreeNode(2, 3));
        forest.addTreeNode(new IntegerTreeNode(4, 5));
        Set<Integer> unfound = forest.getUnfoundNodeID();
        assertTrue(unfound.size() == 1);
        assertTrue(unfound.contains(4));
    }

    @Test
    public void testSeveralTree() {
        Forest<Integer, Integer> forest = new Forest<Integer, Integer>();
        forest.addTreeNode(new IntegerTreeNode(null, 1));
        forest.addTreeNode(new IntegerTreeNode(1, 2));
        forest.addTreeNode(new IntegerTreeNode(2, 3));
        forest.addTreeNode(new IntegerTreeNode(4, 5));
        forest.addTreeNode(new IntegerTreeNode(null, 4));
        Collection<Tree<Integer, Integer>> trees = forest.getTrees();
        assertTrue(trees.size() == 2);
    }


    @Test
    public void testNodeStream() throws Exception {
        Forest<Integer, Integer> forest = new Forest<Integer, Integer>();
        forest.addTreeNode(new IntegerTreeNode(null, 1));
        forest.addTreeNode(new IntegerTreeNode(1, 2));
        forest.addTreeNode(new IntegerTreeNode(2, 3));
        forest.addTreeNode(new IntegerTreeNode(4, 5));
        forest.addTreeNode(new IntegerTreeNode(null, 4));
        long actualCount = forest.getTrees().stream().flatMap(Tree::treeNodeStream).count();
        Assert.assertEquals(5, actualCount);
    }

    @Test(expected = IllegalStateException.class)
    public void testThatMarkVisitedTwiceThrowIllegalStateException() throws Exception {
        final IntegerTreeNode integerTreeNode = new IntegerTreeNode(1, 2);
        integerTreeNode.markVisited();
        integerTreeNode.markVisited();
    }

    @Test
    public void testThatNodeSignalToWaitingOnVisit() throws Exception {
        final IntegerTreeNode integerTreeNode = new IntegerTreeNode(1, 2);
        Executors.newSingleThreadScheduledExecutor().schedule(integerTreeNode::markVisited, 500, TimeUnit.MILLISECONDS);
        integerTreeNode.awaitVisited(2, TimeUnit.SECONDS);
    }

    private class IntegerTreeNode extends AbstractTreeNode<Integer, Integer> {
        IntegerTreeNode(Integer parentId, Integer id) {
            super(id, id, parentId);
        }
    }

}
