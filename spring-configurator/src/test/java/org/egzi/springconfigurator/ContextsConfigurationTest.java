package org.egzi.springconfigurator;

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


import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ContextsConfigurationTest {

    // prevent some global refactoring fuck up in idea.
    @Test
    public void testThatDefaultContextManagerConfigurationHaveValidContextNameAndProfilesSize() throws Exception {
        ContextsConfiguration contextsConfiguration = ContextsConfiguration.configuration();
        contextsConfiguration.locatorsConfigs().forEach(config ->
                assertEquals("META-INF/context.properties", config.fileName()));
        assertEquals(0, contextsConfiguration.contextProfiles().size());

    }

}
