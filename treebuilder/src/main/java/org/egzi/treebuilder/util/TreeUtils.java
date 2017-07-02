package org.egzi.treebuilder.util;

import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.visitors.DFSWalker;
import org.egzi.treebuilder.visitors.Visitor;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by egorz on 6/6/2017.
 */
public class TreeUtils {
    public static <K, V> void print(Forest<K, V> forest, final Writer writer) throws IOException {
        DFSWalker<K, V> dsf = new DFSWalker<>();
        dsf.setVisitor(new Visitor<K, V>() {
            @Override
            public <R> R doVisit(TreeNode<K, V> node) {
                try {
                    writer.write("Node:" + node + "\r\n");
                } catch (IOException io) {
                    io.printStackTrace();
                }
                return null;
            }
        });
        dsf.walk(forest);
        writer.flush();
    }
}
