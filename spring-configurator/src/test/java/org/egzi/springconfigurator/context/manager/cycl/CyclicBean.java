package org.egzi.springconfigurator.context.manager.cycl;

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
