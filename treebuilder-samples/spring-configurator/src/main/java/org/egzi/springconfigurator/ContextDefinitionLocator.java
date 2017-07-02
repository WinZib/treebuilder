package org.egzi.springconfigurator;

import com.google.common.base.Throwables;
import org.egzi.springconfigurator.model.ContextInfo;
import org.egzi.springconfigurator.model.ContextInfoHolder;
import org.egzi.treebuilder.TreeNode;
import org.egzi.treebuilder.TreeNodeLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by egorz on 5/1/2017.
 */
public class ContextDefinitionLocator implements TreeNodeLocator<Class, ApplicationContext> {
    private static final Logger log = LoggerFactory.getLogger(ContextDefinitionLocator.class);

    private static final String RESOURCE_NAME = "META-INF/spring-definition.xml";
    static JAXBContext jaxbContext;

    static {
        try {
            jaxbContext = JAXBContext.newInstance("org.egzi.springconfigurator.model");
        } catch (JAXBException e) {
            e.printStackTrace();
            log.error("Error at initialization of JAXBContext", e);
        }
    }

    Iterator<ContextInfo> contextIterator;
    Enumeration<URL> resourceEnumeration;

    public ContextDefinitionLocator() {
        try {
            resourceEnumeration = getClass().getClassLoader().getResources(RESOURCE_NAME);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public TreeNode<Class, ApplicationContext> getNextTreeNode() {

        if (!(contextIterator != null && contextIterator.hasNext())) {
            parseResource();
        }


        return convert(contextIterator.next());
    }

    private ContextTreeNode convert(ContextInfo contextInfo) {
        return ContextTreeNode.builder()
                .applicationConfigClass(contextInfo.getApplicationContextClass())
                .parentApplicationConfigClass(contextInfo.getParentConfigurationClass())
                .contextClass(contextInfo.getApplicationContextClass())
                .lazyInit(contextInfo.isLazyInit())
                .build();
    }

    private ContextInfo unmarshall(Node node) {
        try {
            return jaxbContext.createUnmarshaller().unmarshal(new DOMSource(node), ContextInfo.class).getValue();
        } catch (JAXBException je) {
            throw new RuntimeException("fail parse context-definition");
        }
    }

    private void parseResource() {
        URL resource = resourceEnumeration.nextElement();
        try {
            ContextInfoHolder contextInfoHolder = (ContextInfoHolder)
                    jaxbContext.createUnmarshaller().unmarshal(new FileInputStream(new File(resource.toURI())));
            contextIterator = contextInfoHolder.getContextInfoList().iterator();
        } catch (Exception e) {
            throw new RuntimeException("fail to parse resource " + resource, e);
        }
    }

    @Override
    public boolean hasMore() {
        return (contextIterator != null && contextIterator.hasNext()) || resourceEnumeration.hasMoreElements();
    }
}
