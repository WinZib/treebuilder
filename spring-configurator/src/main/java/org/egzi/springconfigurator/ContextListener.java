package org.egzi.springconfigurator;

/**
 * Listener that invokes on context event.
 */
public interface ContextListener {


    default void onStart() {
    }


    default void onError() {
    }

    void onRefresh();
}
