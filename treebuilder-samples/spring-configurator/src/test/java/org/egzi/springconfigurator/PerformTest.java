package org.egzi.springconfigurator;

import org.egzi.treebuilder.util.TreeUtils;
import org.junit.Test;

import java.io.PrintWriter;

/**
 * Created by egorz on 6/4/2017.
 */
public class PerformTest {
    @Test
    public void testLoading() throws Exception{
        TreeUtils.print(ContextManager.getInstance().getForest(), new PrintWriter(System.out));
    }
}
