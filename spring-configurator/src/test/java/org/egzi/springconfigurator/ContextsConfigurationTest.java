package org.egzi.springconfigurator;

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
