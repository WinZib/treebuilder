package org.egzi.springconfigurator.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
    public ContextInfoHolder createContextInfoHolder() {
        return new ContextInfoHolder();
    }

    public ContextInfo createContextInfo() {
        return new ContextInfo();
    }
}
