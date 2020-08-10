package com.hawolt.pattern.impl;

import com.hawolt.automation.Routine;
import com.hawolt.pattern.Observable;
import com.hawolt.pattern.Observer;
import com.hawolt.pattern.observer.CommandObserver;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.pmw.tinylog.Logger;

import javax.annotation.Nonnull;
import java.util.*;

public class ObservableAdapter extends ListenerAdapter implements Observable<GenericEvent> {

    private List<Observer<GenericEvent>> list = new ArrayList<>();

    public void addObserver(Observer<GenericEvent> observer) {
        list.add(observer);
    }

    public void removeObserver(Observer<GenericEvent> observer) {
        list.remove(observer);
    }

    public void notifyObserver(GenericEvent event) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).dispatch(event);
        }
    }

    private Map<Class<? extends GenericEvent>, Routine<? extends GenericEvent>> routines = new HashMap<>();

    public void addRoutine(Class<? extends GenericEvent> event, Routine<? extends GenericEvent> routine) {
        routines.put(event, routine);
    }

    @SuppressWarnings("unchecked")
    private <T> T uncheckedCast(Object type) {
        return (T) type;
    }

    @Override
    public void onGenericEvent(@Nonnull GenericEvent event) {
        Optional.ofNullable(routines.get(event.getClass())).ifPresent(routine -> routine.apply(uncheckedCast(event)));
        notifyObserver(event);
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        CommandObserver.BOT_ID = event.getJDA().getSelfUser().getIdLong();
        Logger.debug("Started {} at {}", CommandObserver.BOT_ID, new Date());
    }
}
