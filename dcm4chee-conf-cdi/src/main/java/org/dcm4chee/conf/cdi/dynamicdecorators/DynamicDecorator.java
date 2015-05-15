package org.dcm4chee.conf.cdi.dynamicdecorators;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface DynamicDecorator {
	@Nonbinding
	double priority() default 1.0;
}
