package com.example.reactionbot;

import com.example.reactionbot.adapters.MessageAdapter;
import com.example.reactionbot.adapters.ReactionsRoleAdapter;
import com.example.reactionbot.adapters.ReactionDeleteAdapter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
public class ReactionBotApplication {

    //TODO consider setting up a github-workflow that deploys the changes to the bot on a push
    //TODO add unit tests!

    @Autowired
    Environment environment;

    @Autowired
    private ReactionsRoleAdapter reactionsRoleAdapter;

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
                    .setActivity(Activity.listening("/arr"))
                    .addEventListeners(reactionsRoleAdapter)
                    .build();

            List<OptionData> options = new ArrayList<>(8);
            options.add(new OptionData(OptionType.STRING, "title", "your poll needs a title").setRequired(true));
            options.add(new OptionData(OptionType.STRING, "text", "meaningful and supportive text").setRequired(true));
            options.add(new OptionData(OptionType.STRING, "map", "provide entries in 'role: :emoji-alias:' manner").setRequired(true));
            options.add(new OptionData(OptionType.STRING, "footer", "a small footer to be remembered by"));

            jda.upsertCommand("arr", "assign role at reaction")
                    .addOptions(options)
                    .queue();

        } catch (LoginException e) {
            log.error("Login failed, your token is probably invalid or not provided", e);
            e.printStackTrace();
        }
        return jda;
    }
}
