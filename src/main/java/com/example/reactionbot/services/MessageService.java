package com.example.reactionbot.services;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class MessageService {

    public static MessageEmbed sendGeneralMessage(String authorName, String title, String description, String footer) {

        return new EmbedBuilder()
                .setAuthor(authorName)
                .setColor(Color.magenta)
                .setTitle(title)
                .addBlankField(false)
                .setDescription(description)
                .setFooter(footer)
                .build();
    }
}
