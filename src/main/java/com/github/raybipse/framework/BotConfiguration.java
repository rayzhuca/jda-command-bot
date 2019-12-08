package com.github.raybipse.framework;

import java.awt.Color;

/**
 * Configuration for the application. All properties in this method should be
 * used for the entire application. Methods and properties should be static by
 * default.
 * 
 * @author RayBipse
 */
public class BotConfiguration {
    private static String botPrefix = "&";
    private static Color errorColor = new Color(237, 92, 90);
    private static Color successColor = new Color(93, 217, 107);
    private static Color promptColor = new Color(97, 189, 255);

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