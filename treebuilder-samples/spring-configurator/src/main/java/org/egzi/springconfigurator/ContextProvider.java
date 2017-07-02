package org.egzi.springconfigurator;

import org.springframework.context.ApplicationContext;

/**
 * Created by egorz on 5/1/2017.
 */
public interface ContextProvider {
    ApplicationContext provide();

    ApplicationContext provideOrWait();

    boolean isInitialized();
}
