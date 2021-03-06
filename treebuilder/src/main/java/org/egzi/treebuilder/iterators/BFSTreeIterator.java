package org.egzi.treebuilder.iterators;

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
        queue.addAll(next.getChildren());
        return next;
    }
}
