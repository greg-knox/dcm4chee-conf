package org.dcm4chee.conf.cdi.dynamicdecorators;

import java.util.Collection;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDecoratorDecorator<T> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DynamicDecoratorDecorator.class);

	 @Inject
	 @ConfiguredDynamicDecorators
	 Instance<Collection<DelegatingServiceImpl<T>>> dynamicDecorators;
	
    public final T wrapWithDynamicDecorators(T delegate) {
        // Wrap delegate in enabled decorators
        // TODO: lookup if a deco is enabled in the config
        // TODO: allow to specify order in the config
        // TODO: cache theService per service type - it is ApplicationScoped

    	DelegatingServiceImpl<T> theService = new DelegatingServiceImpl<T>();
    	theService.setOrig(delegate);

    	for (Collection<DelegatingServiceImpl<T>> collectionDynamicDecoratorsForType : dynamicDecorators) {
	        for (DelegatingServiceImpl<T> dynamicDecorator : collectionDynamicDecoratorsForType) {
	            LOG.info("Iterating over {}", dynamicDecorator.getClass());
	        	dynamicDecorator.setDelegate(theService);
	            dynamicDecorator.setOrig(dynamicDecorator.getTypeObject());
	            theService = dynamicDecorator;
	        }
    	}

        return theService.getOrig();
    }
}
