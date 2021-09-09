package com.example.reactionbot.adapters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class MessageRoleAdapter extends ListenerAdapter {

    private LinkedHashMap<String, String> roleToEmoji = new LinkedHashMap<>(8, 0.75f);

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        List<OptionMapping> title = event.getOptionsByName("title");
        String messageTitle = title.get(0).getAsString();

        List<OptionMapping> text = event.getOptionsByName("text");
        StringBuilder messageText = new StringBuilder(text.get(0).getAsString());

        List<OptionMapping> mappings = event.getOptionsByName("map");
        String[] rolesWithEmoji = mappings.get(0).getAsString().split(" ");

        //+=2 bc you don't need to check the emojis
        for (int i = 0; i < rolesWithEmoji.length; i+=2) {

            //drop the last character (the ":") in the string
            rolesWithEmoji[i] = rolesWithEmoji[i].substring(0, rolesWithEmoji[i].length() - 1);
            roleToEmoji.put(rolesWithEmoji[i], rolesWithEmoji[i + 1]);
            messageText.append("\n").append(rolesWithEmoji[i]).append(" -> ").append(rolesWithEmoji[i + 1]);
        }

        event.replyEmbeds(
                createMessageWithEmbeds(messageTitle, messageText.toString(), "Somebody")
                        .build()).queue();

    }

    private EmbedBuilder createMessageWithEmbeds(String title, String description, String authorName) {

        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.magenta)
                .setAuthor(authorName + " asked for this");
    }
}
