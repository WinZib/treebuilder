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


import org.egzi.treebuilder.AbstractTreeNode;

/**
 * Created by Егор on 20.11.2016.
 */
public class TextBasedTreeNode extends AbstractTreeNode<Integer, String> {
    public TextBasedTreeNode(String id, String data) {
        this(Integer.valueOf(id), data);
    }

    public TextBasedTreeNode(String id, String parentId, String data) {
        this(Integer.valueOf(id), Integer.valueOf(parentId), data);
    }

    public TextBasedTreeNode(Integer id, String data) {
        this(id, (Integer) null, data);
    }

    public TextBasedTreeNode(Integer id, Integer parentId, String data) {
        super(id, data, parentId);
    }
}
