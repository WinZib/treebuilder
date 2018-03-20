package org.egzi.springconfigurator.context;

import org.egzi.springconfigurator.ContextsConfiguration;
import org.egzi.springconfigurator.context.locator.context2.ParentConfiguration;
import org.egzi.springconfigurator.context.manager.added.AddedContext;
import org.egzi.springconfigurator.context.manager.children.ChildConfig1;
import org.egzi.springconfigurator.context.manager.children.ChildConfig2;
import org.egzi.springconfigurator.context.manager.children.ChildConfig3;
import org.egzi.springconfigurator.context.manager.children.superchild.SuperChild1;
import org.egzi.springconfigurator.context.manager.children.superchild.SuperChild2;
import org.egzi.springconfigurator.context.manager.children.superchild.SuperChild3;
import org.egzi.springconfigurator.context.manager.destroy.ParentDestroyConfig1;
import org.egzi.springconfigurator.context.manager.destroy.ParentDestroyConfig2;
import org.egzi.springconfigurator.context.manager.destroy.children.ChildDestroyConfig1;
import org.egzi.springconfigurator.context.manager.destroy.children.ChildDestroyConfig2;
import org.egzi.springconfigurator.context.manager.failed.FailedContext;
import org.egzi.springconfigurator.context.manager.failed.child.FaildChildConfiguration;
import org.egzi.springconfigurator.context.manager.parent.ParentContext;
import org.egzi.springconfigurator.context.manager.profile.ProfileConfiguration;
import org.egzi.springconfigurator.context.manager.standalone.StandaloneContext;
import org.egzi.springconfigurator.context.manager.waiting.SlowpokeConfiguration;
import org.egzi.springconfigurator.exceptions.InvalidContextStatusException;
import org.egzi.springconfigurator.exceptions.ParentContextFailedException;
import org.egzi.springconfigurator.info.ContextInfo;
import org.egzi.springconfigurator.locator.ConfigDefinition;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.*;
import static org.egzi.springconfigurator.ContextManager.configure;
import static org.egzi.springconfigurator.ContextManager.getInstance;
import static org.egzi.springconfigurator.ContextsConfiguration.configuration;
import static org.egzi.springconfigurator.info.ContextStatus.FAILED;
import static org.egzi.springconfigurator.info.ContextStatus.STARTED;
import static org.egzi.springconfigurator.util.TestUtils.*;
import static org.junit.Assert.*;

/**
 * Created by egorz and mash  on 6/4/2017.
 */


public class ContextManagerTests {

    @Before
    public void init() throws Exception {
        refreshStaticStateOfContextManager();
    }

    @Test
    public void testLoadingContextWithoutHierarchy() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        startAndPrint();
        assertNodeCount(getInstance().forest(), 1);

    }


    @Test
    public void testLoadingContextWithHierarchyAndYamlLookup() throws Exception {
        configure(configuration().enableContextFileLookup("META-INF/manager/context.yaml"));
        startAndPrint();
        assertNodeCount(getInstance().forest(), 2);

    }

    @Test
    public void testLoadingContextWithHugeHierarchyAndYamlLookup() throws Exception {
        configure(configuration().enableContextFileLookup("META-INF/manager/bigyaml.yaml"));
        startAndPrint();
        assertNodeCount(getInstance().forest(), 7);

    }

    @Test
    public void testContextUpWithCombinationLookup() throws Exception {
        configure(configuration().enableContextFileLookup("META-INF/manager/context.yaml")
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        startAndPrint();
        assertNodeCount(getInstance().forest(), 3);

    }

    @Test
    public void testContextUpWithExternalContextAddBeforeStart() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/context.yaml")
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        getInstance().add(
                new ConfigDefinition(AddedContext.class,
                        ParentContext.class,
                        false,
                        AnnotationConfigApplicationContext.class
                ));
        startAndPrint();
        assertNodeCount(getInstance().forest(), 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatAddExternalContextWithAbsentParentThrowException() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/context.yaml")
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        getInstance().add(
                new ConfigDefinition(AddedContext.class,
                        ParentConfiguration.class,
                        false,
                        AnnotationConfigApplicationContext.class
                ));
        startAndPrint();
    }

    @Test
    public void testThatWeCanAddExternalContextAfterStart() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/context.yaml")
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        getInstance().start(10, MINUTES);

        getInstance().add(
                new ConfigDefinition(AddedContext.class,
                        ParentContext.class,
                        false,
                        AnnotationConfigApplicationContext.class
                ));
        printContextTreeToStdOut();
        assertNodeCount(getInstance().forest(), 4);


    }

    @Test(expected = IllegalStateException.class)
    public void testThatContextCantStartedTwice() throws Exception {
        getInstance().start(10, MINUTES);
        getInstance().start(10, MINUTES);
    }

    @Test(expected = IllegalStateException.class)
    public void testThatContextCantBeConfiguredAfterInit() throws Exception {
        getInstance();
        configure(ContextsConfiguration.configuration());
    }


    @Test
    public void testThatWeCanGetApplicationContextIfContextWasUp() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        startAndPrint();
        final ApplicationContext context =
                getInstance().provideApplicationContext(StandaloneContext.class);
        assertNotNull(context);
        assertNotNull(context.getBean(StandaloneContext.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatProvideMethodThrowExceptionIfThereAreNoContextInTree() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        startAndPrint();
        getInstance().provideApplicationContext(ParentConfiguration.class);
    }

    @Test(expected = InvalidContextStatusException.class)
    public void testThatProvideMethodThrowExceptionIfContextWasNotStarted() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        getInstance().provideApplicationContext(StandaloneContext.class);
    }

    @Test
    public void testThatWeCanWaitContextUp() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        Executors.newSingleThreadScheduledExecutor().schedule(() -> getInstance().start(),
                500, MILLISECONDS);
        getInstance().provideApplicationContextOrWait(StandaloneContext.class, 7, SECONDS);
    }

    @Test(expected = TimeoutException.class)
    public void testThatIfContextWastUpForSpecificTimeExceptionWillThrown() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        getInstance();
        getInstance().provideApplicationContextOrWait(StandaloneContext.class, 1, MILLISECONDS);
    }


    @Test
    public void testThatFailedContextNotToThrowExceptionAndContextTreeNodeHaveFailedStatus() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        getInstance().add(
                new ConfigDefinition(FailedContext.class,
                        null,
                        false,
                        AnnotationConfigApplicationContext.class
                )).start(10, MINUTES);

        assertEquals(FAILED, getInstance().contextTreeInfo().contextStatus(FailedContext.class));
        assertEquals(STARTED, getInstance().contextTreeInfo().contextStatus(StandaloneContext.class));
    }

    @Test
    public void testThatWeCanAddProfileToContextLookup() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties")
                .addProfie("correct_profile"));
        getInstance().add(
                new ConfigDefinition(ProfileConfiguration.class,
                        null,
                        false,
                        AnnotationConfigApplicationContext.class
                ));
        startAndPrint();
        final List<String> beanNames = asList(getInstance().provideApplicationContext(ProfileConfiguration.class)
                .getBeanDefinitionNames());
        assertTrue(beanNames.contains("correctBean"));
        assertFalse(beanNames.contains("incorrectBean"));
    }


    @Test
    public void testThatWeCanAddHugeHierarchyAfterServerStart() throws Exception {
        configure(configuration().enableContextFileLookup("META-INF/manager/context.yaml"));
        startAndPrint();
        getInstance()
                .add(new ConfigDefinition(ChildConfig1.class,
                        ParentContext.class,
                        false,
                        AnnotationConfigApplicationContext.class))

                .add(new ConfigDefinition(ChildConfig2.class,
                        ParentContext.class,
                        false,
                        AnnotationConfigApplicationContext.class))

                .add(new ConfigDefinition(ChildConfig3.class,
                        ParentContext.class,
                        false,
                        AnnotationConfigApplicationContext.class))

                .add(new ConfigDefinition(SuperChild1.class,
                        ChildConfig1.class,
                        false,
                        AnnotationConfigApplicationContext.class))

                .add(new ConfigDefinition(SuperChild2.class,
                        ChildConfig2.class,
                        false,
                        AnnotationConfigApplicationContext.class))

                .add(new ConfigDefinition(SuperChild3.class,
                        ChildConfig3.class,
                        false,
                        AnnotationConfigApplicationContext.class));
        assertNodeCount(getInstance().forest(), 8);
        assertStarted();
    }

    @Test
    public void testHierarchyTestLookupWithFailedContext() throws Exception {
        configure(configuration().enableContextFileLookup("META-INF/manager/failed_context.yaml"));
        getInstance().start(10, MINUTES);
        final ContextInfo childContextInfo = getInstance().contextTreeInfo().contextInfo(FaildChildConfiguration.class);
        assertEquals(FAILED, childContextInfo.status());

        assertEquals(ParentContextFailedException.class, childContextInfo.error().getClass());
    }

    @Test
    public void testThatWeDotTryToStartExternalContextIfParentIsFailed() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        getInstance().add(
                new ConfigDefinition(FailedContext.class,
                        null,
                        false,
                        AnnotationConfigApplicationContext.class
                )).start(10, MINUTES);

        getInstance().add(
                new ConfigDefinition(FaildChildConfiguration.class,
                        FailedContext.class,
                        false,
                        AnnotationConfigApplicationContext.class
                ));

        final ContextInfo childContextInfo = getInstance().contextTreeInfo().contextInfo(FaildChildConfiguration.class);
        assertEquals(FAILED, childContextInfo.status());

        assertEquals(ParentContextFailedException.class, childContextInfo.error().getClass());
    }


    @Test
    public void testThatWeCanDestroyContextForest() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/destroy.yaml"));
        startAndPrint();
        getInstance().shutDownTree();
        assertTrue(ParentDestroyConfig1.destroyed);
        assertTrue(ParentDestroyConfig2.destroyed);
        assertTrue(ChildDestroyConfig1.destroyed);
        assertTrue(ChildDestroyConfig2.destroyed);
    }


    @Test
    public void testThatWeCanShutdownTreeSubList() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/destroy.yaml"));
        startAndPrint();
        getInstance().shutdownContext(ParentDestroyConfig1.class);

        //check that child was destroyed
        assertTrue(ParentDestroyConfig1.destroyed);
        assertTrue(ChildDestroyConfig1.destroyed);

        //check that other context node is alive
        assertFalse(ParentDestroyConfig2.destroyed);
        assertFalse(ChildDestroyConfig2.destroyed);
    }

    @Test(expected = TimeoutException.class)
    public void testThatWeCanWaitParentContextForSpecificTimeoutAndFailedChildContextOnTimeoutException() throws Exception {
        configure(configuration()
                .enableContextFileLookup("META-INF/manager/standalone_context.properties"));
        getInstance().add(
                new ConfigDefinition(SlowpokeConfiguration.class,
                        null,
                        false,
                        AnnotationConfigApplicationContext.class
                ));
        final Future<Map<Class, ForkJoinTask<Void>>> startContextTask = Executors.newSingleThreadExecutor().submit(
                () -> getInstance().start());
        startContextTask.get();
        getInstance().add(
                new ConfigDefinition(AddedContext.class,
                        SlowpokeConfiguration.class,
                        false,
                        AnnotationConfigApplicationContext.class
                ), 1, MILLISECONDS);

        assertEquals(FAILED
                , getInstance().contextTreeInfo().contextInfo(AddedContext.class).status());
    }

}

