package com.example.reactionbot.dataObject;

import lombok.Getter;

import java.util.LinkedHashMap;

public class ReactionEvent {

    @Getter
    String id;

    @Getter
    LinkedHashMap<String, String> roleToEmojiMapping;

    public ReactionEvent(String id, LinkedHashMap<String, String> roleToEmojiMapping) {
        this.id = id;
        this.roleToEmojiMapping = roleToEmojiMapping;
    }
}
