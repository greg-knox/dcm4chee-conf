package org.dcm4chee.conf.cdi.dynamicdecorators;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.dcm4che3.conf.core.api.ConfigurationException;
import org.dcm4che3.conf.core.storage.SingleJsonFileConfigurationStorage;
import org.dcm4che3.net.Device;
import org.dcm4chee.archive.conf.ArchiveDeviceExtension;
import org.dcm4chee.archive.query.QueryService;
import org.dcm4chee.archive.store.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ConfiguredDynamicDecoratorProducer {
	private static final Logger LOG = LoggerFactory.getLogger(ConfiguredDynamicDecoratorProducer.class);
	
	@Inject
	Device device;
	
	@Inject
	@ConfiguredDynamicDecorators
	Map<String, Double> dynamicDecoratorConfiguration;
	
	@Inject 
	@DynamicDecorator
	Instance<DelegatingServiceImpl<StoreService>> dynamicStoreDecorators;
	
	@Inject 
	@DynamicDecorator
	Instance<DelegatingServiceImpl<QueryService>> dynamicQueryDecorators;
	
	private Map<Double, DelegatingServiceImpl<StoreService>> storeDecorators = null;
	private Map<Double, DelegatingServiceImpl<QueryService>> queryDecorators = null;
	
	private List<String> disabledDecorators = null;

	//TODO: change synchronization from method level
	// each object can only have one synchronized method invoked at a time, so getConfiguredStoreServiceDynamicDecorators will block getConfiguredQueryServiceDynamicDecorator
	@Produces
	@ConfiguredDynamicDecorators
	public synchronized Collection<DelegatingServiceImpl<StoreService>> getConfiguredStoreServiceDynamicDecorators() {
		if (storeDecorators == null) {
			storeDecorators = getServiceDecorators(dynamicStoreDecorators, StoreService.class.getName());
			LOG.info("Created store decorators: {}", storeDecorators);
		}
		LOG.trace("Retrieved {}.", storeDecorators);
		return storeDecorators.values();
	}
	
	@Produces
	@ConfiguredDynamicDecorators
	public synchronized Collection<DelegatingServiceImpl<QueryService>> getConfiguredQueryServiceDynamicDecorators() {
		if (queryDecorators == null) {
			queryDecorators = getServiceDecorators(dynamicQueryDecorators, QueryService.class.getName());
			LOG.info("Creating query decorators: {}", queryDecorators);
		}
		LOG.trace("Retrieved query decorators {}.", queryDecorators);
		return queryDecorators.values();
		
	}
	
	//TODO: how to detect changes to this property, and re-generate the service decorators after?
	private synchronized List<String> getDisabledDecorators() {
		if (disabledDecorators == null) {
			ArchiveDeviceExtension devExt = device.getDeviceExtension(ArchiveDeviceExtension.class);
			if (devExt != null) {
				disabledDecorators = Arrays.asList(devExt.getDisabledDecorators());
			}
		}
		LOG.debug("Returning disabled decorators: {}", disabledDecorators);
		return disabledDecorators;
	}

	private <T> Map<Double, DelegatingServiceImpl<T>> getServiceDecorators(Instance<DelegatingServiceImpl<T>> dynamicDecoratorsForService, String clazz) {
		LOG.debug("Creating decorators for {}.", clazz);
		
		Map<Double, DelegatingServiceImpl<T>> orderedDynamicDecorators = new ConcurrentSkipListMap<Double, DelegatingServiceImpl<T>>();
		for (DelegatingServiceImpl<T> dynamicDecorator : dynamicDecoratorsForService) {
			
			//need to be careful - if the dynamicDecorator object has ApplicationScoped scope, then we need to go through a weld proxy class to get the annotation
			//we would do this by doing:
			//Class<?> clazz = dynamicDecorator.getClass().getSuperclass();
			Class<?> decoratorClazz = dynamicDecorator.getClass();
			if (isDecoratorEnabled(decoratorClazz)) {
				Double priority = (Double) dynamicDecoratorConfiguration.get(decoratorClazz.getName());
				orderedDynamicDecorators.put(priority, dynamicDecorator);
				LOG.debug("Configuring the decorator {} with priority {}.", decoratorClazz, priority);
			}
		}
		//TODO: Make the collection inside the map immutable ?
		return orderedDynamicDecorators;
	}
	
	private boolean isDecoratorEnabled(Class<?> decoratorClazz) {
		boolean enabled = false;
		if (!dynamicDecoratorConfiguration.containsKey(decoratorClazz.getName())) {
			LOG.debug("Not configuring the decorator {} because it is not in the configuration.", decoratorClazz);
		} else if (getDisabledDecorators().contains(decoratorClazz.getName())) {
			LOG.debug("Not configuring the decorator {} because it is disabled.", decoratorClazz);
		} else {
			enabled = true;
		}
		return enabled;
	}
	
}
