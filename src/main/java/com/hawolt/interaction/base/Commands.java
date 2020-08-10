package com.hawolt.interaction.base;

import com.hawolt.Instance;
import com.hawolt.interaction.AbstractCommand;
import com.hawolt.interaction.AbstractWrapper;
import com.hawolt.interaction.Command;
import com.hawolt.pattern.observer.CommandObserver;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.time.Instant;
import java.util.stream.Collectors;

public class Commands extends AbstractCommand {
    public Commands() {
        super(null);
    }

    @Override
    public String getName() {
        return "commands";
    }

    @Override
    public MessageAction getResponse(String previous, String current, AbstractWrapper wrapper) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Available commands");
        builder.setDescription(String.join("\n", CommandObserver.commands.stream().map(Command::getName).collect(Collectors.toSet())));
        builder.setTimestamp(Instant.now());
        return wrapper.getChannel().sendMessage(builder.build());
    }
}
