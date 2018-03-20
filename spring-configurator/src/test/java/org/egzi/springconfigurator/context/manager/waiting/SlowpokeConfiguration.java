package org.egzi.springconfigurator.context.manager.waiting;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration()
@ComponentScan(basePackageClasses = {SlowpokeBean.class})
public class SlowpokeConfiguration {
}
