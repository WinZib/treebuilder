package org.egzi.treebuilder;

/**
 * Created by Егор on 14.09.2016.
 */
public class TreeBuilderFactory {
    public static <K, V> TreeBuilder<K,V> newBuilder() {
        return new TreeBuilder<K, V>();
    }
}
