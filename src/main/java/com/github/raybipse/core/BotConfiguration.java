package com.github.raybipse.core;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.raybipse.components.Command;
import com.github.raybipse.components.CommandGroup;
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

    private static Set<Command> allCommands = new HashSet<Command>();
    private static Set<CommandGroup> allCommandGroups = new HashSet<CommandGroup>();

    /**
     * BotConfiguration cannot be instantized.
     */
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
     * The bot prefix is necessary to be appeneded on front of a message for a
     * command to be invoked.
     * 
     * @return the box prefix
     */
    public static String getBotPrefix() {
        return botPrefix;
    }

    /**
     * The bot prefix is necessary to be appeneded on front of a message for a
     * command to be invoked.
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

    /**
     * Gets all standalone commands as well as commands in {@link CommandGroup}.
     * 
     * @return a {@link Set} of all {@link Command}
     */
    public static Set<Command> getAllCommands() {
        return allCommands;
    }

    /**
     * This method should be only called by jda-command-bot. All commands
     * automatically register themselves via this method upon instantization.
     * 
     * @param command the command to be added to the list of commands
     */
    public static void addCommand(Command command) {
        allCommands.add(command);
    }

    /**
     * When called, the method gets the set returned by {@link #getAllCommands()}
     * and filters out {@link Command}s that returns null in
     * {@link Command#getParent()}.
     * 
     * @return a {@link Set} of all {@link Command}s that do not have a parent
     */
    public static Set<Command> getAllStandaloneCommands() {
        return allCommands.stream().filter((v) -> v.getParent() == null).collect(Collectors.toSet());
    }

    /**
     * @return all the {@link CommandGroup}s
     */
    public static Set<CommandGroup> getAllCommandGroups() {
        return allCommandGroups;
    }

    /**
     * This method should be only called by jda-command-bot. All commands groups
     * automatically register themselves via this method upon instantization.
     * 
     * @param commandGroup the command group to be added to the list of all command
     *                     groups
     */
    public static void addCommandGroup(CommandGroup commandGroup) {
        allCommandGroups.add(commandGroup);
    }
}