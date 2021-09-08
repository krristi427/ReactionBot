package com.example.reactionbot.adapters;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ReactionRoleAdapter extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {

        String emoji = event.getReaction().getReactionEmote().getEmoji();
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
