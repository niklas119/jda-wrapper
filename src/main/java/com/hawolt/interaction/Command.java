package com.hawolt.interaction;

import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.Iterator;

public interface Command {
    String getName();

    CommandLevel getLevel();

    void reload(String previous);

    String getConfig();

    MessageAction getResponse(String previous, String current, AbstractWrapper wrapper);

    void respondTo(AbstractWrapper wrapper, int index);
}
