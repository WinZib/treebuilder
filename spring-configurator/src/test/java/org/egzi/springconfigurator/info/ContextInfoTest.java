package org.egzi.springconfigurator.info;

import org.egzi.springconfigurator.context.manager.standalone.StandaloneContext;
import org.egzi.springconfigurator.exceptions.InvalidContextStatusException;
import org.egzi.springconfigurator.visitor.ContextTreeNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ContextInfoTest {


    private ContextTreeNode treeNode;

    @Before
    public void setUp() throws Exception {
        treeNode = ContextTreeNode.builder()
                .applicationConfigClass(StandaloneContext.class)
                .build();
    }

    @Test
    public void testNotStared() throws Exception {
        ContextInfo contextInfo = new ContextInfo(treeNode);
        Assert.assertEquals(ContextStatus.NOT_STARTED, contextInfo.status());
    }

    @Test
    public void testStopped() throws Exception {
        treeNode.onStarting();
        treeNode.onStop();
        ContextInfo contextInfo = new ContextInfo(treeNode);
        Assert.assertEquals(ContextStatus.STOPPED, contextInfo.status());
    }

    @Test
    public void testFailed() throws Exception {
        treeNode.onStarting();
        treeNode.onError(new Exception());

        ContextInfo contextInfo = new ContextInfo(treeNode);
        Assert.assertEquals(ContextStatus.FAILED, contextInfo.status());
    }

    @Test
    public void testStarted() throws Exception {
        treeNode.onStarting();
        treeNode.onRefresh();
        treeNode.onFinish();
        ContextInfo contextInfo = new ContextInfo(treeNode);
        Assert.assertEquals(ContextStatus.STARTED, contextInfo.status());
    }

    @Test(expected = InvalidContextStatusException.class)
    public void testThatYouCantRefreshNotStartedTreeNode() throws Exception {
        treeNode.onRefresh();
    }

    @Test(expected =InvalidContextStatusException.class)
    public void testThatYouCantFinishNotStartedNode() throws Exception {
        treeNode.onFinish();
    }

    @Test(expected =InvalidContextStatusException.class)
    public void testThatYouCantSetErrorNotStartedNode() throws Exception {
        treeNode.onError(new Exception());
    }

    @Test(expected =InvalidContextStatusException.class)
    public void testThatYouCantSetStartingStatusAlreadyStartedNode() throws Exception {
        treeNode.onStarting();
        treeNode.onStarting();
    }

    @Test(expected =InvalidContextStatusException.class )
    public void testThatYouCantSetErrorTwice() throws Exception {
        treeNode.onStarting();
        treeNode.onError(new Exception());
        treeNode.onError(new Exception());

    }

    @Test(expected =InvalidContextStatusException.class )
    public void testThatYouCantSetRefreshTwice() throws Exception {
        treeNode.onStarting();
        treeNode.onRefresh();
        treeNode.onRefresh();
    }

    @Test(expected =InvalidContextStatusException.class )
    public void testThatYouCantRefreshFailedContext() throws Exception {
        treeNode.onStarting();
        treeNode.onError(new Exception());
        treeNode.onRefresh();
    }

    @Test(expected =InvalidContextStatusException.class )
    public void testThatYouCantFailedRefreshContext() throws Exception {
        treeNode.onStarting();
        treeNode.onRefresh();
        treeNode.onError(new Exception());
    }

    @Test(expected =InvalidContextStatusException.class )
    public void testThatYouCantStartFailedContext() throws Exception {
        treeNode.onStarting();
        treeNode.onError(new Exception());
        treeNode.onStarting();
    }

    @Test(expected =InvalidContextStatusException.class )
    public void testThatYouCantStartStartedContext() throws Exception {
        treeNode.onStarting();
        treeNode.onRefresh();
        treeNode.onStarting();

    }
}
