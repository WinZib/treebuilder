package org.egzi.springconfigurator.context.manager.cycl;

import org.egzi.springconfigurator.ContextManager;
import org.egzi.springconfigurator.context.listener.TestContextListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CyclicBean {

    private TestContextListener listener = new TestContextListener();

    @PostConstruct
    public void init() {
        ContextManager.getInstance().addListener(CyclicBeanConfiguration.class, listener);
    }

    public TestContextListener getContextListener() {
        return listener;
    }


}
