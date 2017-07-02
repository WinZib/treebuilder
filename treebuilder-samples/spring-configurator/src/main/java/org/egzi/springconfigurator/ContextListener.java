package org.egzi.springconfigurator;

import org.springframework.context.ApplicationContext;

/**
 * Created by egorz on 5/1/2017.
 */
public interface ContextListener {
    void onRefresh(ContextProvider provider);

    void onError(Throwable e);

    void onShutDown(ApplicationContext context);
}
