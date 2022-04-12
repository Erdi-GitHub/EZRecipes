package com.erdi.ezrecipes.api;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.erdi.ezrecipes.api.event.RecipeEvent;

/**
 * Class used to dispatch events and register event listeners
 * @author Erdi__
 */
final class RecipeEventHandler {
	private static final Method CONSUMER_ACCEPT;
	
	static {
		Method accept = null;
		for(Method method : Consumer.class.getMethods()) {
			if(method.getName().equals("accept")) {
				accept = method;
				break;
			}
		}
		
		CONSUMER_ACCEPT = accept;
	}
	
	private Map<Class<? extends RecipeEvent>, ArrayList<Consumer<? extends RecipeEvent>>> listenerMap = new HashMap<>();
	
	<T extends RecipeEvent> void listen(Class<T> type, Consumer<T> listener) {
		if(listenerMap.containsKey(type)) {
			listenerMap.get(type).add(listener);
			return;
		}
		
		ArrayList<Consumer<? extends RecipeEvent>> listeners = new ArrayList<>();
		listeners.add(listener);
		
		listenerMap.put(type, listeners);
	}
	
	<T extends RecipeEvent> void dispatch(Class<T> type, T event) {
		if(!listenerMap.containsKey(type))
			return;

		listenerMap.get(type).forEach(consumer -> {
			try {
				CONSUMER_ACCEPT.invoke(consumer, event);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
	}
}
