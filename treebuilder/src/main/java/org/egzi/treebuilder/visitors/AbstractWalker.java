package org.egzi.treebuilder.visitors;

import lombok.Getter;
import lombok.Setter;
import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.Tree;

/**
 * Created by egorz on 6/6/2017.
 */
public abstract class AbstractWalker<K, V> implements Walker<K, V> {
    @Setter
    @Getter
    private Visitor<K, V> visitor;

    public void walk(Forest<K, V> forest) {
        for (Tree<K, V> tree : forest.getTrees()) {
            walk(tree.getRoot());
        }
    }
}
