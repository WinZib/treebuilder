package org.egzi.treebuilder.iterators;

import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.TreeNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Created by egorz on 11/29/2017.
 */
public class BFSTreeIterator<K, V> implements Iterator<TreeNode<K, V>> {
    private Queue<TreeNode<K, V>> queue = new LinkedList<>();

    public BFSTreeIterator(Forest<K, V> forest) {
        //store roots in queue
        forest.getTrees().forEach((e) -> queue.add(e.getRoot()));
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public TreeNode<K, V> next() {
        if(!hasNext())
            throw new NoSuchElementException();
        //removes from front of queue
        TreeNode<K, V> next = queue.remove();
        queue.addAll(next.getChilds());
        return next;
    }
}
