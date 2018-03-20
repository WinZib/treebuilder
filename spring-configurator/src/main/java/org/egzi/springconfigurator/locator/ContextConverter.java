package org.egzi.springconfigurator.locator;

/**
 * Converter that convert ConfigDefinition to some other object
 * @param <T>- object type
 */
public interface ContextConverter<T> {


    /**
     * Convert definition to some other object type
     * @param definition -  definition of context
     * @return specified type
     */
    T convert(ConfigDefinition definition);
}
