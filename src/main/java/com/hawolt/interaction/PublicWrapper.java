package com.hawolt.interaction;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.Iterator;

public class PublicWrapper extends AbstractWrapper {
    private Member member;

    private long guild;

    public PublicWrapper(GuildMessageReceivedEvent event) {
        this.channel = event.getChannel();
        this.guild = event.getGuild().getIdLong();
        this.arguments = event.getMessage().getContentRaw().split(" ");
        this.mention = isMentioned(arguments[0]);
        this.sender = event.getAuthor();
        this.member = event.getMember();
    }

    public long getGuild() {
        return guild;
    }

    public Member getMember() {
        return member;
    }
}
