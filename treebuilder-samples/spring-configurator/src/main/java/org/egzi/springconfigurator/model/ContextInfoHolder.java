package org.egzi.springconfigurator;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "context-configurations", namespace = "https://github.com/WinZib/treebuilder/schema.xsd")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextInfoHolder {
    @XmlElement(name = "context-configuration", namespace = "https://github.com/WinZib/treebuilder/schema.xsd")
    List<ContextInfo> contextInfoList = new ArrayList<>();

    public ContextInfoHolder() {
    }

    public List<ContextInfo> getContextInfoList() {
        return contextInfoList;
    }
}
