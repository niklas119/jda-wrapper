package com.hawolt.interaction;

import com.hawolt.pattern.observer.CommandObserver;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public abstract class AbstractWrapper {

    protected int index;

    protected TextChannel channel;
    protected String[] arguments;
    protected boolean mention;
    protected User sender;

    public String[] getArguments() {
        return arguments;
    }

    public boolean isMention() {
        return mention;
    }

    public void shift() {
        arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
    }

    public String getPointer() {
        return arguments[index];
    }

    public int getIndex() {
        return index;
    }

    public int incrementIndex() {
        return index += 1;
    }

    public boolean canIncrement() {
        return index < arguments.length - 1;
    }

    public boolean canDecrement() {
        return index > 0;
    }

    public int decrementIndex() {
        return index -= 1;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public User getSender() {
        return sender;
    }

    public long getSenderID() {
        return sender.getIdLong();
    }

    protected boolean isMentioned(String o) {
        return o.equals("<@" + CommandObserver.BOT_ID + ">") || o.equals("<@!" + CommandObserver.BOT_ID + ">");
    }


}
