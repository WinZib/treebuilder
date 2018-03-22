package org.egzi.springconfigurator.info;

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



import org.egzi.springconfigurator.locator.YamlContextDefinitionLocator;
import org.egzi.treebuilder.Forest;
import org.egzi.treebuilder.TreeBuilderFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import sun.misc.Unsafe;

import static java.util.Collections.singletonList;

public class ContextTreeInfoTest {

    @Test(expected = IllegalArgumentException.class)
    public void testThatYouCantGetStatusOfAbsentContext() throws Exception {
      Forest forest = TreeBuilderFactory.<Class, ApplicationContext>newBuilder()
                .useTreeNodeLocators(singletonList(new YamlContextDefinitionLocator("ololoolol.lolololo")))
                .buildTrees();
        ContextTreeInfo contextTreeInfo = new ContextTreeInfo(forest);
        contextTreeInfo.contextStatus(Unsafe.class);
    }
}