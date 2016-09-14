package org.egzi.treebuilder;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import static junit.framework.TestCase.*;

/**
 * Created by Егор on 14.09.2016.
 */
public class ForestTest {
    private class IntegerTreeNode extends AbstractTreeNode<Integer, Integer> {
        private Integer id;

        private Integer parentId;

        IntegerTreeNode(Integer parentId, Integer id) {
            this.id = id;
            this.parentId = parentId;
        }

        public Integer getParentId() {
            return parentId;
        }

        public Integer getId() {
            return id;
        }

        public Integer getObject() {
            return id;
        }
    }

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

}
