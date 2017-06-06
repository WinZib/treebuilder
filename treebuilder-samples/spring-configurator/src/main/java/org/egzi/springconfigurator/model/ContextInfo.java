package org.egzi.springconfigurator;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class ContextInfo {
    @XmlElement(name = "context-class", namespace = "https://github.com/WinZib/treebuilder/schema.xsd")
    private Class contextCloass;
    @XmlElement(name = "parent-context-class")
    private Class parentContextClass;
    @XmlAttribute(name = "lazy-init")
    private boolean lazyInit = false;
    @XmlAttribute(name = "application-context-class")
    private Class<? extends AnnotationConfigApplicationContext> applicationContextClass = AnnotationConfigApplicationContext.class;

    public Class getContextClass() {
        return contextCloass;
    }

    @XmlTransient
    public Class getParentContextClass() {
        return parentContextClass;
    }

    @XmlTransient
    public boolean isLazyInit() {
        return lazyInit;
    }

    @XmlTransient
    public Class<? extends AnnotationConfigApplicationContext> getApplicationContextClass() {
        return applicationContextClass;
    }
}

