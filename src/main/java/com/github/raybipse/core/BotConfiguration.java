package com.github.raybipse.core;

import java.awt.Color;

import com.github.raybipse.internal.ErrorMessages;

import net.dv8tion.jda.api.JDA;

/**
 * BotConfiguration is responsible for configuring settings and variables of the
 * bot.
 * 
 * The JDA object must be set via this class for
 * {@link com.github.raybipse.components.Command Command}, or must pass the JDA
 * object directly to {@link com.github.raybipse.components.Command Command}
 * initalization.
 * 
 * @author RayBipse
 */
public class BotConfiguration {
    private static String botPrefix = "&";
    private static Color errorColor = new Color(237, 92, 90);
    private static Color successColor = new Color(93, 217, 107);
    private static Color promptColor = new Color(97, 189, 255);

    private static JDA jda;

    private BotConfiguration() {
    }

    /**
     * This JDA object is used for {@link com.github.raybipse.components.Command
     * Command} to register themselves as an
     * {@link net.dv8tion.jda.api.hooks.ListenerAdapter ListenerAdapter}.
     * 
     * @param jda the {@link JDA} obj to be set
     */
    public static void setJDA(JDA jda) {
        BotConfiguration.jda = jda;
    }

    /**
     * This JDA object is used for {@link com.github.raybipse.components.Command
     * Command} to register themselves as an
     * {@link net.dv8tion.jda.api.hooks.ListenerAdapter ListenerAdapter}.
     * 
     * @return the {@link JDA} set via {@link #setJDA}
     */
    public static JDA getJDA() {
        ErrorMessages.requireNonNullReturn(jda, "getJDA");
        return jda;
    }

    /**
     * If the bot prefix is "/", then a command may be invoked by a user typing:
     * [box prefix][command group prefix] [command prefix]
     * 
     * @return the box prefix
     */
    public static String getBotPrefix() {
        return botPrefix;
    }

    /**
     * If the bot prefix is "/", then a command may be invoked by a user typing:
     * [box prefix][command group prefix] [command prefix]
     * 
     * @param botPrefix the bot's prefix used to invoke to bot
     */
    public static void setBotPrefix(String botPrefix) {
        if (botPrefix == null) {
            throw new IllegalArgumentException("\"botPrefix\" cannot be null.");
        }
        BotConfiguration.botPrefix = botPrefix;
    }

    /**
     * Success color may be used for {@link net.dv8tion.jda.api.EmbedBuilder
     * EmbedBuilder} made by default commands.
     * 
     * @return the success color
     */
    public static Color getSuccessColor() {
        return successColor;
    }

    /**
     * Success color may be used for {@link net.dv8tion.jda.api.EmbedBuilder
     * EmbedBuilder} made by default commands.
     * 
     * @param successColor the success color to be set
     */
    public static void setSuccessColor(Color successColor) {
        if (successColor == null) {
            throw new IllegalArgumentException("\"successColor\" cannot be null.");
        }
        BotConfiguration.successColor = successColor;
    }

    /**
     * Error color may be used for {@link net.dv8tion.jda.api.EmbedBuilder
     * EmbedBuilder} made by default commands.
     * 
     * @return the error color
     */
    public static Color getErrorColor() {
        return errorColor;
    }

    /**
     * Error color may be used for {@link net.dv8tion.jda.api.EmbedBuilder
     * EmbedBuilder} made by default commands.
     * 
     * @param errorColor the error color to be set
     */
    public static void setErrorColor(Color errorColor) {
        if (errorColor == null) {
            throw new IllegalArgumentException("\"errorColor\" cannot be null.");
        }
        BotConfiguration.errorColor = errorColor;
    }

    /**
     * Prompt color may be used for {@link net.dv8tion.jda.api.EmbedBuilder
     * EmbedBuilder} made by default commands.
     * 
     * Prompt color is also used for the
     * {@link com.github.raybipse.components.CommandGroup.Help Help} command
     * 
     * @return the prompt color
     */
    public static Color getPromptColor() {
        return promptColor;
    }

    /**
     * Prompt color may be used for {@link net.dv8tion.jda.api.EmbedBuilder
     * EmbedBuilder} made by default commands.
     * 
     * Prompt color is also used for the
     * {@link com.github.raybipse.components.CommandGroup.Help Help} command
     * 
     * @param promptColor the prompt color to be set
     */
    public static void setPromptColor(Color promptColor) {
        if (promptColor == null) {
            throw new IllegalArgumentException("\"promptColor\" cannot be null.");
        }
        BotConfiguration.promptColor = promptColor;
    }
}