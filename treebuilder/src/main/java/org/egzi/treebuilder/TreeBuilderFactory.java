package org.egzi.treebuilder;

/**
 * Factory class for TreeBuild
 */
public class TreeBuilderFactory {
    /**
     * Create TreeBuild instance
     * @param <K> type for key in treebuilder
     * @param <V> type for value in treebuilder
     * @return instance
     */
    public static <K, V> TreeBuilder<K,V> newBuilder() {
        return new TreeBuilder<K, V>();
    }
}
