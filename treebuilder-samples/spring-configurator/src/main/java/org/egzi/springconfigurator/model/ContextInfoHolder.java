package org.egzi.springconfigurator.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "context-configurations")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextInfoHolder {
    @XmlElement(name = "context-configuration")
    List<ContextInfo> contextInfoList = new ArrayList<>();

    public ContextInfoHolder() {
    }

    public List<ContextInfo> getContextInfoList() {
        return contextInfoList;
    }
}
