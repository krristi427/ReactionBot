package com.example.reactionbot.adapters;

import com.example.reactionbot.dataObject.ReactionEvent;
import com.example.reactionbot.services.MessageService;
import com.example.reactionbot.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class ReactionsRoleAdapter extends ListenerAdapter {

    private List<ReactionEvent> ongoingEvents;
    StorageService storageService = StorageService.getInstance();

    public ReactionsRoleAdapter() {
        ongoingEvents = fillOngoingEvents();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        LinkedHashMap<String, String> roleToEmoji = new LinkedHashMap<>(8, 0.75f);

        List<OptionMapping> title = event.getOptionsByName("title");
        String messageTitle = title.get(0).getAsString();

        List<OptionMapping> text = event.getOptionsByName("text");
        StringBuilder messageText = new StringBuilder(text.get(0).getAsString());

        List<OptionMapping> mappings = event.getOptionsByName("map");
        String[] rolesWithEmoji = mappings.get(0).getAsString().split(" ");

        // In this for-loop, the map is filled with the role-emoji pairs AND
        // the messageText is updated to contain the mappings in a representable manner
        for (int i = 0; i < rolesWithEmoji.length; i+=2) { //+=2 bc you don't need to check the emojis

            //drop the last character (the ":") in the string
            rolesWithEmoji[i] = rolesWithEmoji[i].substring(0, rolesWithEmoji[i].length() - 1);

            //TODO add checks to prevent users from using the same emoji twice within an Event
            roleToEmoji.put(rolesWithEmoji[i], rolesWithEmoji[i + 1]);
            messageText.append("\n").append(rolesWithEmoji[i]).append(" --> ").append(rolesWithEmoji[i + 1]);
        }

        List<OptionMapping> footer = event.getOptionsByName("footer");

        //if the footer-option is empty: send a custom message, else whatever the footer has
        String footerMessage = footer.isEmpty() ? "Made with ❤️ by your server-team": footer.get(0).getAsString();

        Member member = event.getMember();
        String authorName = member == null ? "Somebody": member.getEffectiveName();

        event.replyEmbeds(
                MessageService.getInstance().sendGeneralMessage(authorName + " asked for this", messageTitle, messageText.toString(), footerMessage))
                .queue(interactionHook -> interactionHook.retrieveOriginal() // all this bullshit for an ID
                        .queue(message -> {
                            ongoingEvents.add(new ReactionEvent(message.getId(), roleToEmoji));
                            updateOngoingEvents();
                        }));
    }

    private void updateOngoingEvents() {

        storageService.writeToFile(ongoingEvents);
    }

    private List<ReactionEvent> fillOngoingEvents() {
        return storageService.readFromFile();
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

        Optional<ReactionEvent> correspondingEvent = ongoingEvents.stream()
                .filter(reactionEvent -> event.getMessageId().equals(reactionEvent.getId()))
                .findFirst();

        if (correspondingEvent.isEmpty()) {
            log.warn("It seems the Event the user tried to react to, is unavailable");
            return;
        }

        LinkedHashMap<String, String> currentMapping = correspondingEvent.get().getRoleToEmojiMapping();

        String emoji = event.getReaction().getReactionEmote().getEmoji();
        String role = "";

        //could i have built the map differently in order to call a simple key.getValue()? Yes
        // why didn't i do it? I just really like streams :D
        Optional<String> roleOptional = findKeyForValue(currentMapping, emoji);

        if (roleOptional.isEmpty()) {
            log.warn("No Role was found for that emoji ( " + emoji + " ) in the provided map");
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

    private Optional<String> findKeyForValue(LinkedHashMap<String, String> roleToEmoji, String emoji) {

        return roleToEmoji.entrySet()
                .stream()
                .filter(key -> emoji.equals(
                        key.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
    }
}
