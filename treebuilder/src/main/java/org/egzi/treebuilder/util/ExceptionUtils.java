package org.egzi.treebuilder.util;

/**
 * Created by egorz on 6/6/2017.
 */
public class ExceptionUtils {
    public static <T extends Throwable> void rethrow(Throwable t) throws T{
        throw (T)t;
    }
}
