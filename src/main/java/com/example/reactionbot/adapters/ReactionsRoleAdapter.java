package com.example.reactionbot.adapters;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ReactionsRoleAdapter extends ListenerAdapter {

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

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        handleReaction(event, true);
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        handleReaction(event, false);
    }

    //since both extend the same class, OO saves the day
    private void handleReaction(GenericMessageReactionEvent event, boolean isReactionAdded) {

        String emoji = event.getReaction().getReactionEmote().getEmoji();
        String role = "";

        //could i have built the map differently in order to call a simple key.getValue()? Yes
        // why didn't i do it? I just really like streams :D
        Optional<String> roleOptional = findKeyForValue(roleToEmoji, emoji);

        if (roleOptional.isEmpty()) {
            log.error("No Role was found for that emoji in the provided map");
            return;
        }

        role = roleOptional.get();

        Guild guild = event.getGuild();

        List<Role> rolesByName = guild.getRolesByName(role, true);
        if (rolesByName.isEmpty()) {
            log.error("Role not found in Guild. The case will be ignored, " +
                    "but please look out for spelling errors or availability of the given role");
            return;
        }

        if (isReactionAdded) {
            guild.addRoleToMember(event.getUserId(), rolesByName.get(0)).queue();
        } else {
            guild.removeRoleFromMember(event.getUserId(), rolesByName.get(0)).queue();
        }

    }

    private EmbedBuilder createMessageWithEmbeds(String title, String description, String authorName) {

        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.magenta)
                .setAuthor(authorName + " asked for this");
    }

    private Optional<String> findKeyForValue(LinkedHashMap<String, String> roleToEmoji, String emoji) {

        return roleToEmoji.entrySet()
                .stream()
                .filter(key -> emoji.equals(
                        key.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
