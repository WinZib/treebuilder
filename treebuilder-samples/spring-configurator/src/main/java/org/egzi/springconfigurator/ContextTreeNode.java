package org.egzi.springconfigurator;

import org.egzi.treebuilder.AbstractTreeNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class ContextTreeNode extends AbstractTreeNode<Class, ApplicationContext> {
    private Date scheduleDate;

    private Date startDate;

    private Date finishDate;

    private Date stopDate;

    private Throwable error;

    private Class applicationConfigClass;

    private Class parentApplicationConfigClass;

    private Class<? extends AnnotationConfigApplicationContext> contextClass;

    private boolean lazyInit;

    public ContextTreeNode(Class applicationConfigClass, Class parentApplicationConfigClass, Class<? extends AnnotationConfigApplicationContext> contextClass, boolean lazyInit) {
        super(applicationConfigClass, parentApplicationConfigClass);
        this.applicationConfigClass = applicationConfigClass;
        this.parentApplicationConfigClass = parentApplicationConfigClass;
        this.contextClass = contextClass;
        this.lazyInit = lazyInit;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Class getApplicationConfigClass() {
        return applicationConfigClass;
    }

    public void setApplicationConfigClass(Class applicationConfigClass) {
        this.applicationConfigClass = applicationConfigClass;
    }

    public Class getParentApplicationConfigClass() {
        return parentApplicationConfigClass;
    }

    public void setParentApplicationConfigClass(Class parentApplicationConfigClass) {
        this.parentApplicationConfigClass = parentApplicationConfigClass;
    }

    public Class<? extends AnnotationConfigApplicationContext> getContextClass() {
        return contextClass;
    }

    public void setContextClass(Class<? extends AnnotationConfigApplicationContext> contextClass) {
        this.contextClass = contextClass;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public String toString() {
        return "ContextTreeNode{" +
                "scheduleDate=" + scheduleDate +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", stopDate=" + stopDate +
                ", error=" + error +
                ", applicationConfigClass=" + applicationConfigClass +
                ", parentApplicationConfigClass=" + parentApplicationConfigClass +
                ", contextClass=" + contextClass +
                ", lazyInit=" + lazyInit +
                '}';
    }
}
