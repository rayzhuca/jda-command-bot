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
     * @param jda is the {@link JDA} obj to be set
     */
    public static void setJDA(JDA jda) {
        BotConfiguration.jda = jda;
    }

    /**
     * @return the {@link JDA} set via {@link #setJDA}
     */
    public static JDA getJDA() {
        ErrorMessages.requireNonNullReturn(jda, "getJDA");
        return jda;
    }

    public static String getBotPrefix() {
        return botPrefix;
    }

    public static void setBotPrefix(String botPrefix) {
        if (botPrefix == null) {
            throw new IllegalArgumentException("\"botPrefix\" cannot be null.");
        }
        BotConfiguration.botPrefix = botPrefix;
    }

    public static Color getSuccessColor() {
        return successColor;
    }

    public static void setSuccessColor(Color successColor) {
        if (successColor == null) {
            throw new IllegalArgumentException("\"successColor\" cannot be null.");
        }
        BotConfiguration.successColor = successColor;
    }

    public static Color getErrorColor() {
        return errorColor;
    }

    public static void setErrorColor(Color errorColor) {
        if (errorColor == null) {
            throw new IllegalArgumentException("\"errorColor\" cannot be null.");
        }
        BotConfiguration.errorColor = errorColor;
    }

    /**
     * @apiNote prmopt color is also used for the
     *          {@link com.github.raybipse.framework.CommandGroup.Help Help} command
     */
    public static Color getPromptColor() {
        return promptColor;
    }

    /**
     * @apiNote prmopt color is also used for the
     *          {@link com.github.raybipse.framework.CommandGroup.Help Help} command
     */
    public static void setPromptColor(Color promptColor) {
        if (promptColor == null) {
            throw new IllegalArgumentException("\"promptColor\" cannot be null.");
        }
        BotConfiguration.promptColor = promptColor;
    }
}