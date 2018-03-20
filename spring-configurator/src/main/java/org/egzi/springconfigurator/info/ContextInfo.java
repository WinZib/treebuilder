package org.egzi.springconfigurator.info;

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
