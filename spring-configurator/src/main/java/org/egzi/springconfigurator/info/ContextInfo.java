package org.egzi.springconfigurator.info;

/*
 * #%L
 * spring-configurator
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


import lombok.ToString;
import org.egzi.springconfigurator.visitor.ContextTreeNode;
import org.egzi.treebuilder.TreeNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * Snapshot of current context status in some moment of time.
 */
@ToString
public class ContextInfo {

    private final ContextTreeNode contextNode;


    public ContextInfo(TreeNode<Class, ApplicationContext> node) {
        ContextTreeNode contextNode = ((ContextTreeNode) node);

        this.contextNode = contextNode.copy();
    }

    public Date startDate() {
        return contextNode.startDate();
    }

    public Date finishDate() {
        return contextNode.finishDate();
    }

    public Date stopDate() {
        return contextNode.stopDate();
    }

    public Throwable error() {
        return contextNode.error();
    }

    public boolean lazyInit() {
        return contextNode.lazyInit();
    }

    public Class applicationConfigClass() {
        return contextNode.applicationConfigClass();
    }

    public Class parentApplicationConfigClass() {
        return contextNode.parentApplicationConfigClass();
    }

    public Class<? extends AnnotationConfigApplicationContext> contextClass() {
        return contextNode.contextClass();
    }

    public List<String> profiles() {
        return contextNode.profiles();
    }

    public ContextStatus status() {
        return contextNode.status();
    }

    public Class context() {
        return contextNode.contextClass();
    }
}
