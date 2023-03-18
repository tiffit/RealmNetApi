package net.tiffit.realmnetapi.api.event;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class EventHandler {

    private HashMap<Class<?>, List<Consumer<?>>> eventListeners = new HashMap<>();
    private List<Consumer<?>> EMPTY = new ArrayList<>(0);

    public void clearListeners(){
        eventListeners.clear();
    }

    public <T> void addListener(Class<T> event, Consumer<T> listener){
        eventListeners.compute(event, (aClass, consumers) -> {
            if(consumers == null){
                consumers = new LinkedList<>();
            }
            consumers.add(listener);
            return consumers;
        });
    }

    @SneakyThrows
    public <T> void executeEvent(Object event){
        for (Consumer<?> consumer : eventListeners.getOrDefault(event.getClass(), EMPTY)) {
            Method method = consumer.getClass().getDeclaredMethod("accept", Object.class);
            method.setAccessible(true);
            method.invoke(consumer, event);
        }
    }

}
