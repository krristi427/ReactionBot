package com.example.reactionbot.services;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

public class MessageService {

    private MessageService() {}
    private static final MessageService instance = new MessageService();
    public static MessageService getInstance() {
        return instance;
    }

    public MessageEmbed sendGeneralMessage(String authorName, String title, String description, String footer) {

        return new EmbedBuilder()
                .setAuthor(authorName)
                .setColor(Color.magenta)
                .setTitle(title)
                .addBlankField(false)
                .setDescription(description)
                .setFooter(footer)
                .build();
    }

    public MessageEmbed sendPollMessage(String authorName, String title, String description, String footer) {

        return new EmbedBuilder()
                .setAuthor(authorName)
                .setColor(new Color(255, 164, 36))
                .setTitle(title)
                .addBlankField(false)
                .setDescription(description)
                .setFooter(footer)
                .build();
    }

    public MessageEmbed sendGeneralMessageWithImage(String authorName, String title, String description, String footer, String imageUrl) {

        return new EmbedBuilder()
                .setAuthor(authorName)
                .setColor(Color.magenta)
                .setTitle(title)
                .addBlankField(false)
                .setDescription(description)
                .setFooter(footer)
                .setImage(imageUrl)
                .build();
    }

    public MessageEmbed sendGeneralMessageWithThumbnail(String authorName, String title, String description, String footer, String thumbnailUrl) {

        return new EmbedBuilder()
                .setAuthor(authorName)
                .setColor(Color.magenta)
                .setTitle(title)
                .addBlankField(false)
                .setDescription(description)
                .setFooter(footer)
                .setThumbnail(thumbnailUrl)
                .build();
    }

    public MessageEmbed sendGeneralMessageWithTimestamp(String authorName, String title, String description, String footer) {

        return new EmbedBuilder()
                .setAuthor(authorName)
                .setColor(Color.magenta)
                .setTitle(title)
                .addBlankField(false)
                .setDescription(description)
                .setFooter(footer)
                .setTimestamp(Instant.now())
                .build();
    }
}
