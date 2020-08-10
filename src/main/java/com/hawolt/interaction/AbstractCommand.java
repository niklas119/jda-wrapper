package com.hawolt.interaction;

import com.hawolt.Instance;
import com.hawolt.interaction.base.InvalidCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractCommand extends HashMap<String, AbstractCommand> implements Command {

    private static final InvalidCommand INVALID_COMMAND = new InvalidCommand();

    private CommandLevel level;
    private String help, usage;
    private boolean secret;

    protected AbstractCommand parent;

    public AbstractCommand(AbstractCommand parent) {
        this(parent, false);
    }

    private AbstractCommand(AbstractCommand parent, boolean isHelp) {
        this.parent = parent;
        reload(parent == null ? null : parent.getConfig());
        if (isHelp) return;
        put("help", new AbstractCommand(this, true) {
            @Override
            public String getName() {
                return "help";
            }

            @Override
            public MessageAction getResponse(String previous, String current, AbstractWrapper wrapper) {
                return wrapper.getChannel().sendMessage(help);
            }
        });
        put("info", new AbstractCommand(this, true) {
            @Override
            public String getName() {
                return "info";
            }

            @Override
            public MessageAction getResponse(String previous, String current, AbstractWrapper wrapper) {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setAuthor(help);
                builder.setTitle(usage.replace("{@}", "."));
                StringBuilder description = new StringBuilder();
                Set<String> set = AbstractCommand.this.keySet();
                if (!set.isEmpty()) description.append("Available sub-commands:\r");
                for (String sub : set) {
                    description.append("-").append(sub).append("\n");
                }
                if (description.length() > 0) description.setLength(description.length() - 1);
                builder.setDescription(description.toString());
                return wrapper.getChannel().sendMessage(builder.build());
            }
        });
    }

    @Override
    public void reload(String previous) {
        String current = (previous == null ? getName() : String.join(".", previous, getName())).toLowerCase();
        level = CommandLevel.findByName(Instance.getConfig().getOrDefault(String.join(".", "commands", current, "level"), "DEFAULT"));
        help = Instance.getConfig().getOrDefault(String.join(".", "commands", current, "help"), "No help provided.");
        usage = Instance.getConfig().getOrDefault(String.join(".", "commands", current, "usage"), "No help provided.");
        secret = Boolean.parseBoolean(Instance.getConfig().getOrDefault(String.join(".", "commands", current, "usage"), "false"));
        for (String sub : keySet()) {
            get(sub).reload(current);
        }

    }

    public String getConfig() {
        return (parent == null ? getName() : String.join(".", parent.getName(), getName())).toLowerCase();
    }

    public CommandLevel getLevel() {
        return level;
    }

    public boolean isPrivate() {
        return secret;
    }

    public String getHelp() {
        return help;
    }

    public String getUsage() {
        return usage;
    }

    @Override
    public void respondTo(AbstractWrapper wrapper, int index) {
        if (wrapper.canIncrement()) wrapper.incrementIndex();
        String command = wrapper.getPointer();
        if (containsKey(command)) {
            get(command).respondTo(wrapper, wrapper.getIndex());
        } else {
            handle(getResponse(null, command, wrapper));
        }
    }

    private void handle(MessageAction action) {
        if (action != null) action.queue();
    }

}
