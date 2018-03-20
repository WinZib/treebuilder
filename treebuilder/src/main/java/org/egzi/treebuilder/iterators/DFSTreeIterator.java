package org.egzi.treebuilder.iterators;

import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.TreeNode;

import java.util.*;

/**
 * Created by egorz on 11/29/2017.
 */
public class DFSTreeIterator<K, V> implements Iterator<TreeNode<K, V>> {
    private Stack<TreeNode<K, V>> stack = new Stack<>();

    public DFSTreeIterator(Forest<K, V> forest) {
        //store roots in stack
        forest.getTrees().forEach((e) -> stack.push(e.getRoot()));
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public TreeNode<K, V> next() {
        if(!hasNext())
            throw new NoSuchElementException();
        //removes from front of stack
        TreeNode<K, V> next = stack.pop();
        stack.addAll(next.getChildren());
        return next;
    }
}
