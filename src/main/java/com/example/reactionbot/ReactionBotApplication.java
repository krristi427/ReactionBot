package com.example.reactionbot;

import com.example.reactionbot.adapters.MessageAdapter;
import com.example.reactionbot.adapters.ReactionDeleteAdapter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@Slf4j
public class ReactionBotApplication {

    //TODO create a MessagingService that sends messages with embeds

    @Autowired
    Environment environment;

    @Autowired
    private MessageAdapter messageAdapter;

    @Autowired
    private ReactionDeleteAdapter reactionDeleteAdapter;

    public static void main(String[] args) {
        SpringApplication.run(ReactionBotApplication.class, args);
    }

    @Bean
    @ConfigurationProperties(value = "jda")
    public JDA jda() {

        String token = environment.getProperty("TOKEN");
        JDA jda = null;

        try {
            jda = JDABuilder.createLight(token)
                    .setActivity(Activity.listening("%"))
                    .addEventListeners(messageAdapter)
                    .addEventListeners(reactionDeleteAdapter)
                    .build();

            jda.upsertCommand("arr", "assign role at reaction")
                    .addOption(OptionType.STRING, "title", "your poll needs a title")
                    .addOption(OptionType.STRING, "text", "meaningful and supportive text")
                    .addOption(OptionType.STRING, "map", "provide roles in role:unicodeEmoji manner")
                    .queue();

        } catch (LoginException e) {
            log.error("Login failed, your token is probably invalid or not provided", e);
            e.printStackTrace();
        }
        return jda;
    }
}
