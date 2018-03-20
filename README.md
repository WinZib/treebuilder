# Treebuilder
Simple implementation of parallel tree processing over [Fork Join Pool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html) (FJP) 
# Spring configurator
Treebuilder used for parralel context starting. 
Configurator lookup configuration classes, build context tree and starting that tree in parralel useing Treebuilder.
# How do Spring Configurator
**1. Crete configuration file**

If you want to use spring configurator, you should create configuration files describing your context hierarchy.

There are two types of configuration files:
- property configuration files
- yaml configuration files

Properties file structure:
``` properties
application-context-class=org.springframework.context.annotation.AnnotationConfigApplicationContext
config-class=com.egzi.spring.MyConfiguration
parent-config-class=com.egzi.spring.MyParentConfiguration
lazy-init=false
```
- application-context-class - class for application contex. Optional parameter

- config-class - your configuration class of current context

- parent-config - parent configuration class for current context

- lazy-init - is your context is lazy init

Spring configurator lookups properties files from `META-INF/context.properties` by default

Yaml file structure:
``` yaml
contexts:
  - lazy-init: false
    application-context-class: org.springframework.context.annotation.AnnotationConfigApplicationContext
    config-class: com.egzi.spring.MyParentConfiguration

  - lazy-init: false
    application-context-class: org.springframework.context.annotation.AnnotationConfigApplicationContext
    config-class: com.egzi.spring.MyConfiguration
    parent-config-class: com.egzi.spring.MyParentConfiguration
```
Yaml is configurated the same way, as properties file, but yaml file lookup shoul be enabled in ContextManager, because it disabled by default
``` java
        ContextManager.configure(ContextsConfiguration
                .configuration()
                .enableContextFileLookup("META-INF/manager/context.yaml"));
```
**2.Configure the context manager**

Context Manager is main class for working with spring context hierarchy. It should be configured before working with it. You can't configure context manager after `getInstance` was invoked.

![#f03c15](https://placehold.it/15/f03c15/000000?text=+) Incorrect
``` java
ContextManager.getInstance();
ContextManager.configure(ContextsConfiguration
                .configuration()
                .enableContextFileLookup("META-INF/manager/context.yaml"));
```
![#c5f015](https://placehold.it/15/c5f015/000000?text=+) Correct
``` java
ContextManager.configure(ContextsConfiguration
                .configuration()
                .enableContextFileLookup("META-INF/manager/context.yaml"));
ContextManager.getInstance();
```
You can specify in ContextsConfiguration:
- spring profiles from your enviroment
- enable/disable file lookup
- add new files files for lookup

Example:
``` java
        ContextManager.configure(ContextsConfiguration
                .configuration()
                .enableContextFileLookup("META-INF/manager/context.yaml")
                .disableContextFileLookup("META-INF/context.properties")
                .addProfie("Profile1")
                .addProfiels(Arrays.asList("Profile2","Profile3")));
```

**2.Start context manager**
You can start context manager in sync or async mode.
- In sync mode we pass timeout in start method and wait specified timeout. If timeout expired, TimeoutException would be thrown.
``` java
ContextManager.getInstance().start(3,TimeUnit.MINUTES)
```
- In async mode we start context in backround and return Map<RootContextClass,ForkJoinTask<Void>>
``` java
        final Map<Class, ForkJoinTask<Void>> contextsFuture = ContextManager.getInstance().start();
        contextsFuture.get(MyConfiguration_1.class).get(1,SECONDS)
```
  
**2.Add context to configured tree on the fly**

You can configure ContextManager by java API after configuration file lookup before and after start.
- If context manager have't been started,  we just add `not started` context node to `not started` tree. After that we start context manager and our external contexts will be started by FJP.
``` java
ContextManager.getInstance().add(
                new ConfigDefinition(AddedContext.class,
                        ParentContext.class,
                        false,
                        AnnotationConfigApplicationContext.class
                )); // add extenal contextr
ContextManager.getInstance().start(); // start external context and lookuped context                
```
- If context manager is aready started, we do the following steps:
  - Waiting parent context starting for specified timeout
  - When parent context have been started, we start external context
  - Add `started` external context in tree

``` java
ContextManager.getInstance().start(); // start context manager in async mode
ContextManager.getInstance().add(
                new ConfigDefinition(AddedContext.class,
                        ParentContext.class,
                        false,
                        AnnotationConfigApplicationContext.class
                )); // wait ParentContext for infinite timeout(no timeout value was passed), 
                //start AddedContext, and add it in tree
```
