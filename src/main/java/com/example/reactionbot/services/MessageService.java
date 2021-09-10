package com.example.reactionbot.services;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

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

    public static MessageEmbed sendGeneralMessageWithImage(String authorName, String title, String description, String footer, String imageUrl) {

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

    public static MessageEmbed sendGeneralMessageWithThumbnail(String authorName, String title, String description, String footer, String thumbnailUrl) {

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

    public static MessageEmbed sendGeneralMessageWithTimestamp(String authorName, String title, String description, String footer) {

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
