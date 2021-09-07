package com.example.reactionbot.adapters;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ReactionDeleteAdapter extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {

        if (event.getReaction().getReactionEmote().getEmoji().equals("\uD83D\uDC4E")) {
            String messageId = event.getMessageId();
            event.getChannel()
                    .deleteMessageById(messageId)
                    .queue(); // ALWAYS queue everything you do
        }
    }
}
