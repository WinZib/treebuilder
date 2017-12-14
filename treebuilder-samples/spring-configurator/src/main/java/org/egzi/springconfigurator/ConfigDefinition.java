package org.egzi.springconfigurator.model;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.xml.bind.annotation.*;


public class ConfigDefinition {
    @XmlElement(name = "config-class")
    private Class configurationClass;
    @XmlElement(name = "parent-config-class")
    private Class parentContextClass;
    @XmlAttribute(name = "lazy-init")
    private boolean lazyInit = false;
    @XmlAttribute(name = "application-context-class")
    private Class<? extends AnnotationConfigApplicationContext> applicationContextClass = AnnotationConfigApplicationContext.class;

    public ConfigDefinition() {
    }

    @XmlTransient
    public Class getConfigurationClass() {
        return configurationClass;
    }

    @XmlTransient
    public Class getParentConfigurationClass() {
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
