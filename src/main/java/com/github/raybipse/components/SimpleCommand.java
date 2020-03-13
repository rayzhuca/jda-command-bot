package com.github.raybipse.components;

import static com.github.raybipse.internal.ErrorMessages.requireNonNullParam;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.raybipse.internal.Nullable;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * SimpleCommand is a short way of defining a {@link Command} without making a
 * new class.
 */
public class SimpleCommand extends Command {

    private List<GenericEventEntry<?>> eventListeners = new ArrayList<>();

    /**
     * @param name        the name of the command
     * @param prefix      the prefix of the command
     * @param description the description of the command
     * @param examples    the examples of the command
     * @param syntax      the syntax of the command
     * @param parent      the parent of the command
     */
    public SimpleCommand(String name, String prefix, String description, List<String> examples, String syntax,
            CommandGroup parent) {
        super(name, prefix);
        setDescription(description);
        setExamples(examples);
        setSyntax(syntax);
        setParent(parent);
    }

    /**
     * @param consumer the consumer to be called when a {@link MessageReceivedEvent}
     *                 is called
     * @param filters  the filters that acts as prerequisites for the consumer to be
     *                 called
     * 
     * @return {@code this}
     */
    public SimpleCommand withMessageReceivedEvent(Consumer<MessageReceivedEvent> consumer,
            @Nullable List<Function<? super MessageReceivedEvent, Boolean>> filters) {
        eventListeners.add(new GenericEventEntry<MessageReceivedEvent>(MessageReceivedEvent.class, consumer, filters));
        return this;
    }

    /**
     * @param consumer the consumer to be called when a {@link MessageReceivedEvent}
     *                 is called
     * 
     * @return {@code this}
     */
    public SimpleCommand withMessageReceivedEvent(Consumer<MessageReceivedEvent> consumer) {
        eventListeners.add(new GenericEventEntry<MessageReceivedEvent>(MessageReceivedEvent.class, consumer, null));
        return this;
    }

    /**
     * @param entry the entry to be added to the list of listeners
     * 
     * @return {@code this}
     */
    public SimpleCommand withEvent(GenericEventEntry<?> entry) {
        requireNonNullParam(entry, "entry");
        eventListeners.add(entry);
        return this;
    }

    /**
     * 
     * @param <T>        the type of the class to be listened to
     * @param eventClass the class to be listened to
     * @param consumer   the consumer to be called when
     * @param filters    the filters that acts as prerequisites for the consumer to
     *                   be called
     * 
     * @return {@code this}
     */
    public <T extends GenericEvent> SimpleCommand withEvent(Class<T> eventClass, Consumer<T> consumer,
            @Nullable List<? extends Function<? super T, Boolean>> filters) {
        return withEvent(new GenericEventEntry<T>(eventClass, consumer, filters));
    }

    @Override
    public void onGenericEvent(GenericEvent event) {
        eventListeners.forEach(v -> {
            v.notify(event);
        });
    }

    /**
     * An entry of the list of listeners that will be notified when a event is
     * called.
     * 
     * @param <T> the type of the event to be listened to
     */
    public static class GenericEventEntry<T extends GenericEvent> {
        private Class<T> eventClass;
        private Consumer<? super T> consumer;
        private List<? extends Function<? super T, Boolean>> filters;

        /**
         * @param eventClass the class to be listened to
         * @param consumer   the consumer to be called when
         * @param filters    the filters that acts as prerequisites for the consumer to
         *                   be called
         */
        public GenericEventEntry(Class<T> eventClass, Consumer<? super T> consumer,
                @Nullable List<? extends Function<? super T, Boolean>> filters) {
            requireNonNullParam(eventClass, "eventClass");
            requireNonNullParam(consumer, "consumer");

            this.setEventClass(eventClass);
            this.setConsumer(consumer);
            this.setFilters(filters);
        }

        /**
         * Calls the consumer if the {@code inputEvent} is the event to be listened to
         * and all the filters return {@code true}.
         * 
         * @param inputEvent the event of the event
         * 
         * @return if the consumer is called
         */
        @SuppressWarnings("unchecked")
        public boolean notify(GenericEvent inputEvent) {
            if (!inputEvent.getClass().isAssignableFrom(eventClass))
                return false;
            if (filters != null && !filters.stream().allMatch(f -> f.apply((T) inputEvent)))
                return false;
            invoke((T) inputEvent);
            return true;
        }

        /**
         * Calls the consumer with the parameter {@code event}.
         * 
         * @param event the event of the event
         */
        public void invoke(T event) {
            consumer.accept(event);
        }

        /**
         * @return the class of the event to be listened to
         */
        public Class<T> getEventClass() {
            return eventClass;
        }

        /**
         * @param eventClass the class of the event to be listened to
         */
        public void setEventClass(Class<T> eventClass) {
            requireNonNullParam(eventClass, "eventClass");
            this.eventClass = eventClass;
        }

        /**
         * @return the consumer
         */
        public Consumer<? super T> getConsumer() {
            return consumer;
        }

        /**
         * @param consumer the consumer
         */
        public void setConsumer(Consumer<? super T> consumer) {
            if (consumer == null) {
                consumer = (event) -> {
                };
            }
            this.consumer = consumer;
        }

        /**
         * @return the list of filters
         */
        public List<? extends Function<? super T, Boolean>> getFilters() {
            return filters;
        }

        /**
         * @param filters the list of filters
         */
        public void setFilters(List<? extends Function<? super T, Boolean>> filters) {
            if (filters == null) {
                filters = new ArrayList<Function<T, Boolean>>();
            }
            this.filters = filters;
        }
    }
}