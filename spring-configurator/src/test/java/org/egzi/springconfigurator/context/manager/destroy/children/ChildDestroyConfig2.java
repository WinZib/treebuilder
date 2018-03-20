package org.egzi.springconfigurator.context.manager.destroy.children;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChildDestroyConfig2 implements DisposableBean {

    public static volatile boolean destroyed = false;


    @Override
    public void destroy() throws Exception {
        destroyed = true;
    }

}
