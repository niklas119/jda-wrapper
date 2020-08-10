package com.hawolt.pattern.observer;

import com.hawolt.interaction.Command;
import com.hawolt.interaction.PublicWrapper;
import com.hawolt.interaction.base.Commands;
import com.hawolt.pattern.Observer;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class CommandObserver implements Observer<GenericEvent> {

    public static long BOT_ID;

    public static List<Command> commands = new ArrayList<>();

    public CommandObserver() {
        commands.add(new Commands());
    }

    @Override
    public void dispatch(GenericEvent event) {
        try {
            if (event instanceof GuildMessageReceivedEvent) {
                handlePublicMessage((GuildMessageReceivedEvent) event);
            } else if (event instanceof PrivateMessageReceivedEvent) {

            } else if (event instanceof GenericMessageReactionEvent) {

            }
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    private void handlePublicMessage(GuildMessageReceivedEvent event) {
        if (event.getMember().getUser().isBot()) return;
        PublicWrapper wrapper = new PublicWrapper(event);
        if (wrapper.isMention()) wrapper.shift();
        if (wrapper.getArguments().length == 0 || wrapper.getPointer().length() == 0) return;
        String tmp = wrapper.isMention() ? wrapper.getPointer() : wrapper.getPointer().substring(1);
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            if (command.getName().equals(tmp)) {
                command.respondTo(wrapper, wrapper.getIndex());
            }
        }

    }
/*
    public CommandObserver(String base) {
        commands.add(new About());
        commands.add(new Reload());
        commands.add(new Commands());
        Reflections reflections = new Reflections(base);
        Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);
        Logger.debug("Found {} commands", commands.size());
        for (Class<? extends Command> command : commands) {
            if (Modifier.isAbstract(command.getModifiers())) continue;
            Logger.debug("Attempting to add {} to command cache", command.getCanonicalName());
            try {
                CommandObserver.commands.add(command.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                Logger.error(e);
            }
        }
    }



    public abstract void handleException(GenericEvent event, Throwable e);

    private void handleGuildMessage(GenericEvent event) {
        MessageWrapper wrapper = MessageWrapper.wrap(event);
        if (wrapper.isBot()) return;
        String prefix = getPrefix(wrapper.getGuildID());
        boolean mention = wrapper.isMentioned(), match = isCommand(wrapper, wrapper.isDM(), prefix);
        if (((mention && !match) || (!mention && match)) && !wrapper.isDM()) onWrongMention(wrapper);
        if ((mention && wrapper.getArguments().length == 1) || (!mention && !match)) {
            onMessage(wrapper);
            return;
        }
        if (wrapper.getMessage().getContentRaw().isEmpty()) return;
        String key = mention ? wrapper.getArguments()[1] : wrapper.getArguments()[0].substring(prefix.length());
        if (mention) wrapper.shift();
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            if (command.isPrivate() && !wrapper.isDM()) continue;
            boolean allowed = wrapper.isDM() || command.getCommandLevel().ordinal() <= resolve(wrapper.getUserID(), wrapper.getMember().getRoles()).ordinal();
            if (!command.isPrivate() && !allowed) continue;
            if (command.getName().equals(key)) {
                process(command, wrapper);
            } else if (!wrapper.isDM()) {
                List<String> aliases = command.getAliases(wrapper.getMember().getGuild().getIdLong());
                for (int j = 0; j < aliases.size(); j++) {
                    String alias = aliases.get(j);
                    if (alias.equals(key)) {
                        process(command, wrapper);
                    }
                }
            }
        }
    }

    protected abstract void onMessage(MessageWrapper wrapper);

    protected abstract void onWrongMention(MessageWrapper wrapper);

    public abstract void reactionReceived(Message message, GenericMessageReactionEvent event);

    private void handleReaction(GenericEvent event) {
        GenericMessageReactionEvent reaction = (GenericMessageReactionEvent) event;
        reaction.getChannel().retrieveMessageById(reaction.getMessageIdLong()).queue(message -> {
            if (message.getAuthor().getIdLong() == BOT_ID) reactionReceived(message, reaction);
        }, exception -> handleException(event, exception));
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    private void process(Command command, MessageWrapper wrapper) {
        long user = wrapper.getUser().getIdLong();
        Logger.debug("Command {} executed by {}", command.getName(), user);
        boolean length = wrapper.getArguments().length > 1;
        if (length && wrapper.getArguments()[1].equals("help")) {
            wrapper.getChannel().sendMessage(Embedder.embed(command.getHelp())).queue();
        } else if (length && !wrapper.isDM() && wrapper.getArguments()[1].equals("info")) {
            wrapper.getChannel().sendMessage(command.getInfo(wrapper.getGuildID(), getPrefix(wrapper.getGuildID()))).queue();
        } else if (length && !wrapper.isDM() && wrapper.getArguments()[1].equals("usage")) {
            wrapper.getChannel().sendMessage(command.getUsage(getPrefix(wrapper.getGuildID()))).queue();
        } else {
            MessageEmbed embed = command.getResponse(wrapper);
            if (embed != null) wrapper.getChannel().sendMessage(embed).queue();
        }
    }

    private boolean isCommand(MessageWrapper wrapper, boolean dm, String prefix) {
        return !dm && wrapper.getArguments()[0].startsWith(prefix);
    }*/

  /*  public abstract String getPrefix(long guild);

    public abstract CommandLevel resolve(long user, List<Role> roles);*/
}
