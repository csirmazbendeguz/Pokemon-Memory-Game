package net.csirmazbendeguz.memory_game.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class EventDispatcher {

    private EventHandlers eventHandlers;

    @Inject
    public EventDispatcher(EventHandlers eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    /**
     * Dispatch an event to the registered event listeners.
     *
     * @param event The event to dispatch.
     */
    public void dispatch(EventObject event) {
        Class<? extends EventObject> eventClass = event.getClass();

        if (!eventClass.isAnnotationPresent(Listener.class)) {
            throw new IllegalArgumentException(String.format("Can't dispatch '%s', '%s' annotation is missing.", eventClass.getName(), Listener.class.getName()));
        }

        Class<? extends EventListener> listenerInterface = eventClass.getAnnotation(Listener.class).value();
        List<Method> methods = Arrays.stream(listenerInterface.getMethods())
            .filter(method -> method.getParameterCount() == 1)
            .filter(method -> method.getParameterTypes()[0].equals(eventClass))
            .collect(Collectors.toList());

        if (methods.isEmpty()) {
            throw new IllegalArgumentException(String.format("Can't dispatch '%s', corresponding method in '%s' is missing.", eventClass.getName(), listenerInterface.getName()));
        }

        List<EventListener> listeners = eventHandlers.getAll(listenerInterface);

        for (EventListener listener : listeners) {
            for (Method method : methods) {
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    throw new RuntimeException(String.format("Can't dispatch '%s', failed to invoke '%s' on '%s'.", eventClass.getName(), method, listener), exception);
                }
            }
        }
    }

}
