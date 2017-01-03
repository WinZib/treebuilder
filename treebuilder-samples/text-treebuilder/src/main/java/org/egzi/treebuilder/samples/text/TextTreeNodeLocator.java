package org.egzi.treebuilder.samples.text;

import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.TreeNodeLocator;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

/**
 * Parse Tree structure from text like:
 * <b>key</b>*space*<b>parent_key</b>*space*<b>value</b>
 * Example:
 * 1 A
 * 2 1 B
 * 3 1 C
 * 4 2 D
 *
 */
public class TextTreeNodeLocator implements TreeNodeLocator<Integer, String>{
    private Scanner scanner;

    public TextTreeNodeLocator(String data) {
        this(new StringReader(data));
    }

    public TextTreeNodeLocator(Reader reader) {
        this(new Scanner(reader));
    }

    public TextTreeNodeLocator(Readable readable) {
        this(new Scanner(readable));
    }

    public TextTreeNodeLocator(InputStream is) {
        this(new Scanner(is));
    }

    public TextTreeNodeLocator(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public TreeNode<Integer, String> getNextTreeNode() {
        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine();
                String[] lineParts = line.split("\\W");
                if (lineParts.length == 3)
                    return new TextBasedTreeNode(lineParts[0], lineParts[1], lineParts[2]);
                else if (lineParts.length == 2)
                    return new TextBasedTreeNode(lineParts[0], lineParts[1]);
                else throw new IllegalAccessException("Need to pass just 2 or 3 arguments");
            } catch (Exception e) {
                //TODO yes-yes just print exception in stderr (i'm a bad boy)
                e.printStackTrace();
            }
        }
        //will be mean that scanner has no any line
        return null;
    }

    @Override
    public boolean hasMore() {
        return scanner.hasNextLine();
    }
}
