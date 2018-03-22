package org.egzi.springconfigurator.util;

/*
 * #%L
 * spring-configurator
 * %%
 * Copyright (C) 2018 WinZib (winzib@yandex.ru)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import lombok.SneakyThrows;
import org.egzi.springconfigurator.ContextManager;
import org.egzi.springconfigurator.info.ContextStatus;
import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.util.TreeUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;

public abstract class TestUtils {

    private static final Class[] ADD_URL_PARAMETERS = new Class[]{URL.class};


    private TestUtils() {
        throw new AssertionError();
    }


    public static void assertNodeCount(Forest<Class, ApplicationContext> forest, int expectedCound) {

        assertEquals(expectedCound, forest.nodeStream()
                .count());
    }


    @SneakyThrows
    public static void refreshStaticStateOfContextManager() {
        Field instance = ContextManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        ReflectionUtils.setField(instance, null, null);
        Field created = ContextManager.class.getDeclaredField("created");
        created.setAccessible(true);
        ReflectionUtils.setField(created, false, false);
        Field started = ContextManager.class.getDeclaredField("started");
        started.setAccessible(true);
        ReflectionUtils.setField(started, false, new AtomicBoolean());


    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static void addJarToCurrentClasspath(URL url) {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;
        Method method = sysclass.getDeclaredMethod("addURL", ADD_URL_PARAMETERS);
        method.setAccessible(true);
        method.invoke(sysloader, url);
    }

    public static void removeJarFromClassPath(URL url) throws Exception {
        URLClassLoader urlClassLoader = (URLClassLoader)
                ClassLoader.getSystemClassLoader();
        Class<?> urlClass = URLClassLoader.class;
        Field ucpField = urlClass.getDeclaredField("ucp");
        ucpField.setAccessible(true);
        URLClassPath ucp = (URLClassPath) ucpField.get(urlClassLoader);
        Class<?> ucpClass = URLClassPath.class;
        Field urlsField = ucpClass.getDeclaredField("urls");
        urlsField.setAccessible(true);
        Stack urls = (Stack) urlsField.get(ucp);
        urls.remove(url);
    }


    public static void startAndPrint() throws IOException {
        ContextManager.getInstance().start(1, TimeUnit.MINUTES);
        printContextTreeToStdOut();
        assertStarted();
    }

    public static void assertStarted() {
        ContextManager.getInstance().contextTreeInfo().contextInfoNodes().forEach(contextInfo
                -> assertEquals(ContextStatus.STARTED, contextInfo.status()));
    }

    public static void printContextTreeToStdOut() throws IOException {
        TreeUtils.print(ContextManager.getInstance().forest(), new PrintWriter(System.out));
    }


}
