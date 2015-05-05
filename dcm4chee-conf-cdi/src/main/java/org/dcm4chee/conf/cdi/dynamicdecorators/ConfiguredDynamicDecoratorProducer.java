package org.dcm4chee.conf.cdi.dynamicdecorators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.dcm4chee.archive.store.DelegatingStoreService;
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
	Instance<DelegatingStoreService> dynamicStoreDecorators;
	
	@Produces
	@ConfiguredDynamicDecorators
	public Collection<DelegatingStoreService> getConfiguredDynamicDecorators() {
    	Map<Double, DelegatingStoreService> orderedDecorators = new TreeMap<Double, DelegatingStoreService>();
    	
    	for (DelegatingStoreService dynamicDecorator : dynamicStoreDecorators) {
    		//need to be careful - if the dynamicDecorator object has ApplicationScoped scope, then we need to go through a weld proxy class to get the annotation
    		//we would do this by doing:
    		//Class<?> clazz = dynamicDecorator.getClass().getSuperclass();
    		
    		Class<? extends DelegatingStoreService> clazz = dynamicDecorator.getClass();
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
