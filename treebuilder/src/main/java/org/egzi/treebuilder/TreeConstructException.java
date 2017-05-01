package org.egzi.treebuilder;

/**
 * Exception which can be thrown during constructing tree process
 */
public class TreeConstructException extends RuntimeException {
    public TreeConstructException(String message) {
        super(message);
    }
}
