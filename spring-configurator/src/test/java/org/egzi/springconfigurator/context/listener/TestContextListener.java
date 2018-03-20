package org.egzi.springconfigurator.context.listener;

import org.egzi.springconfigurator.ContextListener;

public class TestContextListener implements ContextListener {

    public volatile boolean onStart = false;

    public volatile boolean onError = false;

    public volatile boolean onRefresh = false;

    @Override
    public void onStart() {
        onStart = true;
    }

    @Override
    public void onError() {
        onError = true;
    }

    @Override
    public void onRefresh() {
        onRefresh = true;
    }
}
