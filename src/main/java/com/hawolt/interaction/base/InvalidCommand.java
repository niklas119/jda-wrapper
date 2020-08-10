package com.hawolt.interaction.base;

import com.hawolt.interaction.AbstractCommand;
import com.hawolt.interaction.AbstractWrapper;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class InvalidCommand extends AbstractCommand {
    public InvalidCommand() {
        super(null);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public MessageAction getResponse(String previous, String current, AbstractWrapper wrapper) {
        String leading = String.format("%s is not a valid command", current, previous);
        String trailing = String.format("for %s", previous);
        return wrapper.getChannel().sendMessage(previous == null ? leading : String.join(" ", leading, trailing));
    }
}
