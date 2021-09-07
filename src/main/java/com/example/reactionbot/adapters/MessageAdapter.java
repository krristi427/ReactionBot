package com.example.reactionbot.adapters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class MessageAdapter extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }

        String authorName = event.getAuthor().getName();

        String message = event.getMessage().getContentRaw();
        String[] parameters = message.split(" ");

        if (parameters[0].equalsIgnoreCase("%help")) {
            MessageEmbed messageEmbed = sendMessageWithEmbeds("Helpful shit", "helpy stuff", authorName).build();
            event.getChannel().sendMessageEmbeds(messageEmbed).queue();
        }

        if (parameters[0].equalsIgnoreCase("%echo")) {

            String description = Arrays.stream(parameters, 1, parameters.length)
                    .collect(Collectors.joining(" "));

            MessageEmbed messageWithEmbeds = sendMessageWithEmbeds("Echooo", description, authorName).build();
            event.getChannel().sendMessageEmbeds(messageWithEmbeds).queue();
        }
    }

    private EmbedBuilder sendMessageWithEmbeds(String title, String description, String authorName) {

        return new EmbedBuilder().setTitle(title)
                .setDescription(description)
                .setColor(Color.magenta)
                .setAuthor(authorName + " asked for this");
    }
}
