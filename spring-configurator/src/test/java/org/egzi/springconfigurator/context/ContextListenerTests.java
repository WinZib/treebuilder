package org.egzi.springconfigurator.context;

import org.egzi.springconfigurator.ContextManager;
import org.egzi.springconfigurator.context.listener.TestContextListener;
import org.egzi.springconfigurator.context.locator.context1.MyConfiguration_1;
import org.egzi.springconfigurator.context.manager.added.AddedContext;
import org.egzi.springconfigurator.context.manager.failed.FailedContext;
import org.egzi.springconfigurator.context.manager.parent.ParentContext;
import org.egzi.springconfigurator.context.manager.cycl.CyclicBean;
import org.egzi.springconfigurator.context.manager.cycl.CyclicBeanConfiguration;
import org.egzi.springconfigurator.context.manager.standalone.StandaloneContext;
import org.egzi.springconfigurator.locator.ConfigDefinition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.egzi.springconfigurator.ContextsConfiguration.configuration;
import static org.egzi.springconfigurator.util.TestUtils.refreshStaticStateOfContextManager;
import static org.egzi.springconfigurator.util.TestUtils.startAndPrint;
import static org.junit.Assert.assertTrue;

public class ContextListenerTests {


    @Before
    public void init() throws Exception {
        refreshStaticStateOfContextManager();
    }

    @Test
    public void testThatWeCanApplyListenerBeforeContextStarting() throws Exception {

        ContextManager.configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        final TestContextListener testListener = new TestContextListener();
        ContextManager.getInstance().addListener(StandaloneContext.class, testListener);
        startAndPrint();
        assertTrue(testListener.onStart);
        assertTrue(testListener.onRefresh);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatYouCantApplyListenerToAbsentNode() throws Exception {
        ContextManager.configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        final TestContextListener testListener = new TestContextListener();
        ContextManager.getInstance().addListener(MyConfiguration_1.class, testListener);
    }


    @Test
    public void testThatYouCanApplyListenerToExternalConfigurationAfterStart() throws Exception {
        ContextManager.configure(configuration()
                .enableContextFileLookup("META-INF/manager/context.yaml")
                .disableContextFileLookup("META-INF/context/context.properties")
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        ContextManager.getInstance().start();

        ContextManager.getInstance().add(
                new ConfigDefinition(AddedContext.class,
                        ParentContext.class,
                        false,
                        AnnotationConfigApplicationContext.class
                ));

        final TestContextListener testListener = new TestContextListener();
        ContextManager.getInstance().addListener(AddedContext.class, testListener);
        assertTrue((testListener.onRefresh));
    }

    @Test
    public void testThatOnErrorMethodWillBeExecutedIfContextFailed() throws Exception {
        ContextManager.configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        final TestContextListener testListener = new TestContextListener();
        ContextManager.getInstance().start();
        ContextManager.getInstance().add(
                new ConfigDefinition(FailedContext.class,
                        null,
                        false,
                        AnnotationConfigApplicationContext.class
                ));
        ContextManager.getInstance().addListener(FailedContext.class, testListener);
        assertTrue(testListener.onError);

    }

    @Test
    public void testThatWeCanAddListenerToContextWhichWasAddedButDidNotRefreshYet() throws Exception {
        ContextManager.configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        ContextManager.getInstance().start();
        ContextManager.getInstance().add(
                new ConfigDefinition(CyclicBeanConfiguration.class,
                        null,
                        false,
                        AnnotationConfigApplicationContext.class
                ));
        final CyclicBean bean = ContextManager.getInstance().provideApplicationContext(CyclicBeanConfiguration.class)
                .getBean(CyclicBean.class);
        assertTrue(bean.getContextListener().onRefresh);
    }
}
