package org.egzi.treebuilder.visitors;

import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by egorz on 6/6/2017.
 */
public abstract class AbstractWalker<K, V> implements Walker<K, V> {

    private Visitor<K, V> visitor;

    public Map<K, ForkJoinTask<Void>> walk(Forest<K, V> forest) {
        return forest.getTrees()
                .stream()
                .map(Tree::getRoot)
                .collect(HashMap::new, (m, v)->m.put(v.getId(), walk(v)), HashMap::putAll);
    }
    public Visitor<K, V> getVisitor() {
        return visitor;
    }

    @Override
    public void setVisitor(Visitor<K, V> visitor) {
        this.visitor = visitor;
    }
}
