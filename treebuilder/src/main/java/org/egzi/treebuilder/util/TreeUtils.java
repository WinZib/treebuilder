package org.egzi.treebuilder.util;

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
