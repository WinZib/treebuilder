package org.egzi.springconfigurator;

import org.egzi.springconfigurator.context1.MyConfiguration;
import org.egzi.springconfigurator.context2.ParentConfiguration;
import org.egzi.springconfigurator.model.ConfigDefinition;
import org.egzi.springconfigurator.model.ConfigDefinitionContainer;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static junit.framework.TestCase.*;
/**
 * Created by egorz on 6/5/2017.
 */
public class JaxbTest {
    @Test
    public void testUnmarshalling() throws Exception {
        ConfigDefinitionContainer holder = (ConfigDefinitionContainer)ContextDefinitionLocator.jaxbContext.createUnmarshaller().unmarshal(getClass().getResourceAsStream("test.springconfigurator.xml"));
        assertEquals(1, holder.getContextDefinitions().size());
        ConfigDefinition contextInfo = holder.getContextDefinitions().get(0);
        assertEquals(MyConfiguration.class, contextInfo.getConfigurationClass());
        assertEquals(ParentConfiguration.class, contextInfo.getParentConfigurationClass());
        assertEquals(true, contextInfo.isLazyInit());
        assertEquals(AnnotationConfigApplicationContext.class, contextInfo.getApplicationContextClass());
    }
}
