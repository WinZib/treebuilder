package org.egzi.springconfigurator.context.manager.cycl;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = CyclicBean.class)
public class CyclicBeanConfiguration {
}
