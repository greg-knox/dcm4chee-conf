package org.dcm4chee.conf.cdi.dynamicdecorators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.dcm4chee.archive.query.QueryService;
import org.dcm4chee.archive.store.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ConfiguredDynamicDecoratorProducer {
	private static final Logger LOG = LoggerFactory.getLogger(ConfiguredDynamicDecoratorProducer.class);
	
	private static final List<String> disabledDecoratorsClassName = new ArrayList<String>();
	
	static {
		disabledDecoratorsClassName.add("org.dcm4chee.archive.iocm.impl.StoreServiceIOCMDecorator");
	}
	
	@Inject 
	@DynamicDecorator
	Instance<DelegatingServiceImpl<StoreService>> dynamicStoreDecorators;
	
	@Inject 
	@DynamicDecorator
	Instance<DelegatingServiceImpl<QueryService>> dynamicQueryDecorators;
	
	@Inject
	BeanManager beanManager;
	
	@Produces
	@ConfiguredDynamicDecorators
	public Collection<DelegatingServiceImpl<StoreService>> getConfiguredDynamicDecorators() {
		Map<Double, DelegatingServiceImpl<StoreService>> orderedDecorators = new TreeMap<Double, DelegatingServiceImpl<StoreService>>();
    	
    	for (DelegatingServiceImpl<StoreService> dynamicDecorator : dynamicStoreDecorators) {
    		//need to be careful - if the dynamicDecorator object has ApplicationScoped scope, then we need to go through a weld proxy class to get the annotation
    		//we would do this by doing:
    		//Class<?> clazz = dynamicDecorator.getClass().getSuperclass();
    		Class<?> clazz = dynamicDecorator.getClass();
        	if (disabledDecoratorsClassName.contains(clazz.getName())) {
        		LOG.info("Not configuring the decorator {} because it is disabled.", clazz);
        		continue;
        	}
    		orderedDecorators.put(clazz.getAnnotation(DynamicDecorator.class).priority(), dynamicDecorator);
    		LOG.debug("Configuring the decorator {} with priority {}.", clazz, clazz.getAnnotation(DynamicDecorator.class).priority());
    	}
    	return orderedDecorators.values();

	}
	
	@Produces
	@ConfiguredDynamicDecorators
	public Collection<DelegatingServiceImpl<QueryService>> getConfiguredQueryDynamicDecorators() {
		Map<Double, DelegatingServiceImpl<QueryService>> orderedDecorators = new TreeMap<Double, DelegatingServiceImpl<QueryService>>();
    	
    	for (DelegatingServiceImpl<QueryService> dynamicDecorator : dynamicQueryDecorators) {
    		//need to be careful - if the dynamicDecorator object has ApplicationScoped scope, then we need to go through a weld proxy class to get the annotation
    		//we would do this by doing:
    		//Class<?> clazz = dynamicDecorator.getClass().getSuperclass();
    		
    		Class<?> clazz = dynamicDecorator.getClass();
        	if (disabledDecoratorsClassName.contains(clazz.getName())) {
        		LOG.info("Not configuring the decorator {} because it is disabled.", clazz);
        		continue;
        	}
    		orderedDecorators.put(clazz.getAnnotation(DynamicDecorator.class).priority(), dynamicDecorator);
    		LOG.debug("Configuring the decorator {} with priority {}.", clazz, clazz.getAnnotation(DynamicDecorator.class).priority());
    	}
    	return orderedDecorators.values();

	}

}
