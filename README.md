# Treebuilder
Simple implementation of parallel tree processing over [Fork Join Pool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html) 
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

Yaml file structure:

``` yaml

```

