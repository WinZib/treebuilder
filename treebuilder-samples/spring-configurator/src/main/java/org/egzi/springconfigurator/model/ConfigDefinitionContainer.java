package org.egzi.springconfigurator.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "context-configurations")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigDefinitionContainer {
    @XmlElement(name = "context-configuration")
    List<ConfigDefinition> contextInfoList = new ArrayList<>();

    public List<ConfigDefinition> getContextDefinitions() {
        return contextInfoList;
    }
}
