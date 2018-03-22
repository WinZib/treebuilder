package org.egzi.treebuilder.textnode;

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
