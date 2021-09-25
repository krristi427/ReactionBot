package com.example.reactionbot.adapters;

import com.example.reactionbot.dataObject.ReactionEvent;
import com.example.reactionbot.services.MessageService;
import com.example.reactionbot.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
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

        if (!event.getName().equals("arr")) return;

        //TODO refactor the roleToEmoji code in a new emoji based-reaction event for polls
        LinkedHashMap<String, String> roleToEmoji = new LinkedHashMap<>(8, 0.75f);

        List<OptionMapping> title = event.getOptionsByName("title");
        String messageTitle = title.get(0).getAsString();

        List<OptionMapping> text = event.getOptionsByName("text");
        String messageText = text.get(0).getAsString();

        List<Role> availableRoles = event.getGuild().getRoles();

        SelectionMenu.Builder builder = SelectionMenu.create("menu")
                .setPlaceholder("Choose your role")
                .setRequiredRange(0, availableRoles.size());
        availableRoles.forEach(role ->
                builder.addOption(role.getName(), role.getName()));
        SelectionMenu menu = builder.build();

        List<OptionMapping> footer = event.getOptionsByName("footer");

        //if the footer-option is empty: send a custom message, else whatever the footer has
        String footerMessage = footer.isEmpty() ? "Made with ❤️ by your server-team": footer.get(0).getAsString();

        Member member = event.getMember();
        String authorName = member == null ? "Somebody": member.getEffectiveName();

        event.replyEmbeds(
                MessageService.getInstance().sendGeneralMessage(authorName + " asked for this", messageTitle, messageText, footerMessage))
                .setEphemeral(true)
                .addActionRow(menu)
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

    @Override
    public void onSelectionMenu(@NotNull SelectionMenuEvent event) {

        Guild guild = event.getGuild();
        User user = event.getUser();
        String userID = user.getId();

        event.getInteraction().getSelectedOptions().forEach(selectOption -> {

            List<Role> rolesByName = guild.getRolesByName(selectOption.getValue(), true);
            if (rolesByName.isEmpty()) {
                log.error("Role not found in Guild. The case will be ignored, " +
                        "but please look out for spelling errors or availability of the given role");
                return;
            }

            guild.addRoleToMember(userID, rolesByName.get(0)).queue();
        });

        user.openPrivateChannel().queue(
                privateChannel -> privateChannel.sendMessageEmbeds(MessageService.getInstance().sendGeneralMessage(
                        user.getName(),
                        "Role Added!",
                        "Well, you see you reacted to a message and i couldn't help myself but send you a message to congratulate you on getting the new role :D",
                        "Made with ❤️ by your server-team")).queue());

        //since discord needs an acknowledgement of the interaction, but i can not use the one above as one :(
        event.deferEdit().queue();
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
