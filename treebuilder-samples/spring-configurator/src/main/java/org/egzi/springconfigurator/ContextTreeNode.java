package org.egzi.springconfigurator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.egzi.treebuilder.AbstractTreeNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

@ToString
@Builder()
public class ContextTreeNode extends AbstractTreeNode<Class, ApplicationContext> {
    @Getter
    @Setter
    private ApplicationContext context;
    @Getter
    @Setter
    private Date scheduleDate;
    @Getter
    @Setter
    private Date startDate;
    @Getter
    @Setter
    private Date finishDate;
    @Getter
    @Setter
    private Date stopDate;
    @Getter
    @Setter
    private Throwable error;

    @Getter
    private Class applicationConfigClass;
    @Getter
    private Class parentApplicationConfigClass;
    @Getter
    private Class<? extends AnnotationConfigApplicationContext> contextClass;
    @Getter
    private boolean lazyInit;

    public ContextTreeNode(ApplicationContext context, Date scheduleDate, Date startDate, Date finishDate, Date stopDate, Throwable error, Class applicationConfigClass, Class parentApplicationConfigClass, Class<? extends AnnotationConfigApplicationContext> contextClass, boolean lazyInit) {
        this.context = context;
        this.scheduleDate = scheduleDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.stopDate = stopDate;
        this.error = error;
        this.applicationConfigClass = applicationConfigClass;
        this.parentApplicationConfigClass = parentApplicationConfigClass;
        this.contextClass = contextClass;
        this.lazyInit = lazyInit;
    }

    public ApplicationContext get() {
        return context;
    }

    public Class getId() {
        return applicationConfigClass;
    }

    public Class getParentId() {
        return parentApplicationConfigClass;
    }

}
