package com.example.reactionbot.services;

import com.example.reactionbot.dataObject.ReactionEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Slf4j
public class StorageService {

    /**
     * Used for reading from and writing to the backup json.
     * The name starting with BOOT-INF is the actual path inside the container where the json is located
     * and the other one is the local one. I recommend using the one from src for development purposes only
     * AND SWITCHING BACK AFTER ANY CHANGE
     */

    //private static final String FILE_NAME = "src/main/resources/ongoingRoleDistributions.json";
    private static final String FILE_NAME = "BOOT-INF/classes/ongoingRoleDistributions.json";
    private static final Path FILE_NAME_PATH = Paths.get(FILE_NAME);

    private List<ReactionEvent> ongoingReactionEvents;
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    //implement the singleton pattern
    private StorageService() {

        if (Files.exists(FILE_NAME_PATH)) {
            ongoingReactionEvents = readFromFile();
        }
    }
    private static final StorageService instance = new StorageService();
    public static StorageService getInstance() {
        return instance;
    }

    public List<ReactionEvent> readFromFile() {

        Type type = new TypeToken<List<ReactionEvent>>() {
        }.getType();

        try {
            ongoingReactionEvents = gson.fromJson(Files.readString(FILE_NAME_PATH), type);
        } catch (IOException e) {
            log.error("Something went wrong while reading from the file!");
            e.printStackTrace();
        }

        if (ongoingReactionEvents == null) {
            ongoingReactionEvents = Collections.emptyList();
        }

        return ongoingReactionEvents;
    }

    public void writeToFile(List<ReactionEvent> events) {

        String json = gson.toJson(events);
        try {

            // this overrides the entire content of the json!
            // but since this json is always kept up to date and read directly after a crash
            // it is fine
            Files.writeString(FILE_NAME_PATH, json);
        } catch (IOException e) {
            log.error("Something went wrong while writing to file!");
            e.printStackTrace();
        }
    }

}
