package com.example.reactionbot.adapters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class MessageAdapter extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getMessage().getContentRaw().equalsIgnoreCase("%help")) {
            MessageEmbed messageEmbed = sendMessageWithEmbeds(event.getAuthor().getName()).build();
            event.getChannel().sendMessage(messageEmbed).queue();
        }
    }

    private EmbedBuilder sendMessageWithEmbeds(String authorName) {

        return new EmbedBuilder().setTitle("Helpful Menu af")
                .setDescription("There is no great mastery to this bot...it's a util for the server-team ;)")
                .setColor(Color.magenta)
                .setAuthor(authorName + " asked for this");
    }
}
