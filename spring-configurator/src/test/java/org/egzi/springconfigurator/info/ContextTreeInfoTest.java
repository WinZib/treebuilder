package org.egzi.springconfigurator.info;


import org.egzi.springconfigurator.locator.YamlContextDefinitionLocator;
import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.TreeBuilderFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import sun.misc.Unsafe;

import static java.util.Collections.singletonList;

public class ContextTreeInfoTest {

    @Test(expected = IllegalArgumentException.class)
    public void testThatYouCantGetStatusOfAbsentContext() throws Exception {
      Forest forest = TreeBuilderFactory.<Class, ApplicationContext>newBuilder()
                .useTreeNodeLocators(singletonList(new YamlContextDefinitionLocator("ololoolol.lolololo")))
                .buildTrees();
        ContextTreeInfo contextTreeInfo = new ContextTreeInfo(forest);
        contextTreeInfo.contextStatus(Unsafe.class);
    }
}