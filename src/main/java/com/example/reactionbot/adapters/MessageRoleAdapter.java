package com.example.reactionbot.adapters;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MessageRoleAdapter extends ListenerAdapter {

    //TODO time the poll so that one could read stats on how it went

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        List<OptionMapping> title = event.getOptionsByName("title");
        String messageTitle = title.get(0).getAsString();

        List<OptionMapping> text = event.getOptionsByName("text");
        StringBuilder messageText = new StringBuilder(text.get(0).getAsString());

        List<OptionMapping> mappings = event.getOptionsByName("map");

        //to match the format of role: unicode
        //TODO refactor this in it's own method
        String rolesWithEmoji = mappings.get(0).getAsString();

        Pattern pattern = Pattern.compile("[\\w]+:\\s[\\u0000-\\uFFFF]");
        Matcher matcher = pattern.matcher(rolesWithEmoji);

        List<String> roleAndEmoji = new ArrayList<>();

        while (matcher.find()) {
            roleAndEmoji.add(matcher.group());
        }

        for (String s : roleAndEmoji) {

            String[] split = s.split(": ");
            messageText.append("\n").append(split[0]).append(" -> ").append(split[1]);
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
