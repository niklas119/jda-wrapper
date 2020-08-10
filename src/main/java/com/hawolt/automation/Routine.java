package com.hawolt.automation;

import net.dv8tion.jda.api.events.GenericEvent;


public interface Routine<T extends GenericEvent> {

    void apply(T event);

}
