package com.github.raybipse;

import javax.security.auth.login.LoginException;

import com.github.raybipse.commands.test.Test;
import com.github.raybipse.framework.InvalidReturnTypeException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 * The beginning point of the application.
 */
public class App {

    private static JDA jda;

    private App() {
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        if (System.getenv("BOT_TOKEN") == null) {
            throw new LoginException("BOT_TOKEN environment variable must be set for the bot token.");
        }

        JDABuilder jdaBuilder = new JDABuilder(System.getenv("BOT_TOKEN"));
        jda = jdaBuilder.build().awaitReady();

        try {
            // Instantiate your commands and command groups here
            // Do not instantiate commands before jda.awaitReady() is ran
            Test.getInstance();
        } catch (InvalidReturnTypeException irte) {
            jda.shutdown();
            irte.printStackTrace();
            System.exit(1);
        }
    }

    public static JDA getJDA() {
        return jda;
    }
}
