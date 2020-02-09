package com.github.raybipse.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.github.raybipse.core.BotConfiguration;
import com.github.raybipse.internal.ErrorMessages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;

/**
 * An instruction that handles a single operation, and is often the child of a
 * {@link CommandGroup}.
 * 
 * A command must be a singleton. Meaning that it only has one single instance.
 * By convention, this is implemented by having a static variable that will be
 * holding the instance of the class. The constructor will be private to prevent
 * instantization from outside and there would also be a method named
 * {@code getInstance()} that initializes the instance if it is called for the
 * first time and returns it. Here is an example of a singleton class,
 * 
 * <pre>
 * <code>
 * class Class {
 *   private static Class instance;
 *   private Class() {}
 *   public static Class getInstance() {
 *     if (instance == null) instance = new Class();
 *     return instance;
 *   }
 * }
 * </code>
 * </pre>
 * 
 * This must be implemented in the command itself since abstract classes cannot
 * implement this behavior easily.
 * 
 * @see {@link CommandGroup}
 * @author RayBipse
 */
public abstract class Command extends ListenerAdapter {

    private Set<Role> requiredRoles = new HashSet<>();
    private Set<Role> blacklistedRoles = new HashSet<>();
    private Consumer<MessageReceivedEvent> onRolePermissionFail = (event) -> {
            event.getChannel().sendMessage(getEmbedPermissionError(requiredRoles, blacklistedRoles).build()).queue();
            System.out.println("queued");
    };

    protected Command() {
        ErrorMessages.requireNonNullReturn(getName(), "getName");
        ErrorMessages.requireNonNullReturn(getPrefix(), "getPrefix");
        ErrorMessages.requireNonNullReturn(getSyntax(), "getSyntax");

        BotConfiguration.getJDA().addEventListener(this);
    }

    /**
     * @return the name of the command. The name cannot be null.
     */
    protected abstract String getName();

    /**
     * @return the prefix used to invoke the command. The prefix cannot be null.
     */
    protected abstract String getPrefix();

    /**
     * @return the description of the command. Return null if there is none.
     */
    protected abstract String getDescription();

    /**
     * @return an array of examples showcasing how to use the command. The array can
     *         be empty or null.
     * 
     * @apiNote the return value should only contain the example concerned after the
     *          prefix(es). The bot prefix, {@link CommandGroup} prefix, and command
     *          prefix will automatically be added. E.g., a return value of
     *          "[parameters...]" would become "[bot prefix][command group prefix]
     *          [prefix] [parameters...]".
     */
    protected abstract String[] getExamples();

    /**
     * @return the syntax of the command. The syntax cannot be null.
     * 
     * @apiNote the return value should only contain the syntax concerned after the
     *          prefix(es). The bot prefix, {@link CommandGroup} prefix, and command
     *          prefix will automatically be added. E.g., a return value of
     *          "[parameters...]" would become "[bot prefix][command group prefix]
     *          [prefix] [parameters...]".
     */
    protected abstract String getSyntax();

    /**
     * @return the parent command group of the command. Return null if there is
     *         none.
     */
    protected abstract CommandGroup getParent();

    /**
     * @return information of the command
     * 
     * @apiNote this method is used by the parent's default help command. Override
     *          this method and return null if you wish to not show information
     *          about this command.
     */
    protected EmbedBuilder getEmbedInfo() {
        EmbedBuilder builder = new EmbedBuilder().setTitle("Command: \"" + getName() + "\"")
                .setColor(BotConfiguration.getPromptColor());
        if (getDescription() != null) {
            builder.setDescription(getDescription());
        }
        if (getParent() != null) {
            builder.addField("Command group", getParent().getName(), false);
        }
        builder.addField("Prefix", getPrefix(), false);

        if (getParent() != null)
            builder.addField("Syntax", MarkdownUtil.monospace(
                    BotConfiguration.getBotPrefix() + getParent().getPrefix() + " " + getPrefix() + " " + getSyntax()),
                    false);
        else
            builder.addField("Syntax",
                    MarkdownUtil.monospace(BotConfiguration.getBotPrefix() + getPrefix() + " " + getSyntax()), false);

        if (getExamples() != null && getExamples().length != 0) {
            StringBuilder exampleValue = new StringBuilder();
            for (String example : getExamples()) {
                if (getParent() != null) {
                    exampleValue.append(BotConfiguration.getBotPrefix() + getParent().getPrefix() + " " + getPrefix()
                            + " " + example + "\n");
                } else {
                    exampleValue.append(BotConfiguration.getBotPrefix() + getPrefix() + " " + example + "\n");
                }
            }
            builder.addField("Example" + (getExamples().length > 1 ? "s" : ""),
                    MarkdownUtil.monospace(exampleValue.toString()), false);
        }

        return builder;
    }

    /**
     * @return true if message calls the command
     */
    protected boolean getInputValidity(String input) {
        ErrorMessages.requireNonNullParam(input, "input");
        CommandGroup parent = getParent();

        if (parent == null) {
            return input.startsWith(getPrefix(), BotConfiguration.getBotPrefix().length());
        } else {
            return input.startsWith(getPrefix(),
                    BotConfiguration.getBotPrefix().length() + parent.getPrefix().length() + 1);
        }
    }

    /**
     * @param input is the original content of the message sent by the user. Input
     *              must of been already checked for validity with
     *              {@link #getInputValidity()}
     * 
     * @return a string without command group (if it exists) and command prefix
     */
    protected String trimInputBeginning(String input) {
        ErrorMessages.requireNonNullParam(input, "input");
        CommandGroup parent = getParent();
        input = input.substring(BotConfiguration.getBotPrefix().length());

        if (parent != null) {
            input = input.substring(parent.getPrefix().length() + 1); // plus one for the space separating the prefixes
        }

        // To trim the extra space before the argument
        // If there is no argument just return an empty string
        if (input.length() == getPrefix().length()) {
            input = "";
        } else {
            input = input.substring(getPrefix().length() + 1);
        }
        return input;
    }

    /**
     * @param c the character to be checked
     * 
     * @return true if parameter c is ' or "
     */
    private static boolean isCharQuotationMark(char c) {
        return c == '\"' || c == '\'';
    }

    /**
     * Splits a string into an array of arguments to be used via a space. Any spaces
     * inside a double or single quotation mark will not be split, and the quotation
     * mark will be removed. Quotation marks will be treated normally if there is a
     * backslash behind it, and the backslash will be removed. Backslashs behind
     * quotes will be treated normally if there is a backslash behind it.
     * 
     * <pre>
     * <code>
     * splitUserInput("ab cde \"fgh ijk\""); // returns three items: [ab, cde, fgh ijk]
     * splitUserInput("\\\"a b\\\""); // returns two items: ["a, b"]
     * </code>
     * </pre>
     * 
     * @param input must be already trimed with {@link #trimInputBeginning()}
     */
    protected static String[] splitUserInput(String input) {
        ErrorMessages.requireNonNullParam(input, "input");
        input = input.trim();
        ArrayList<String> output = new ArrayList<>();
        boolean inQuote = false;

        int beginIndex = 0;
        char[] charArray = input.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (isCharQuotationMark(c) && (i == 0 || charArray[i - 1] != '\\')) {
                inQuote = !inQuote;
            }

            if ((Character.isWhitespace(c) && !inQuote) || i == charArray.length - 1) {
                output.add(input.substring(beginIndex, i + 1).trim());
                beginIndex = i + 1;
            }
        }

        return takeOutBackslashAndQuote(output).toArray(new String[output.size()]);
    }

    /**
     * Removes backslashes that is behind a quotation mark. Removes quotation marks
     * that does not have a blackslash behind it.
     */
    private static ArrayList<String> takeOutBackslashAndQuote(ArrayList<String> input) {
        ErrorMessages.requireNonNullParam(input, "input");
        for (int i = 0; i < input.size(); i++) {
            char[] charArr = input.get(i).toCharArray();
            StringBuilder str = new StringBuilder();
            for (int k = 0; k < charArr.length; k++) {
                if (!((k != charArr.length - 1 && ((isCharQuotationMark(charArr[k + 1]) && charArr[k] == '\\')))
                        || (isCharQuotationMark(charArr[k]) && (k == 0 || charArr[k - 1] != '\\')))) {
                    str.append(charArr[k]);
                }
            }
            input.set(i, str.toString());
        }

        return input;
    }

    /**
     * Calling this method would mean users must have the roles supplied to this
     * method to invoke the command.
     * 
     * @param roles is the roles to require
     */
    protected void requireRoles(Set<Role> roles) {
        requiredRoles.addAll(roles);
    }

    /**
     * @return whitelisted roles
     */
    protected Set<Role> getRequiredRoles() {
        return requiredRoles;
    }

    /**
     * @param roles is the roles to blacklist
     */
    protected void blacklistRoles(Set<Role> roles) {
        blacklistedRoles.addAll(roles);
    }

    /**
     * @return blacklisted roles
     */
    protected Set<Role> getBlacklistedRoles() {
        return blacklistedRoles;
    }

    /**
     * 
     * @param member is the user to be checked on
     * @return true if {@code member} contains all required roles and does not
     *         contain any blacklisted roles
     */
    protected boolean checkUserRolePermission(Member member) {
        List<Role> userRoles = member.getRoles();
        for (Role role : getBlacklistedRoles()) {
            if (userRoles.contains(role)) {
                return false;
            }
        }
        return userRoles.containsAll(getRequiredRoles());
    }

    /**
     * Runs the returned consumer of {@link #getOnRolePermissionFail()} and throws
     * an exception if the user does not met expected permissions.
     * 
     * @param event is the event of the message
     * 
     * @throws PermissionException if the user does not met expected permissions
     */
    protected void enforceUserRolePermission(MessageReceivedEvent event) {
        if (!checkUserRolePermission(event.getMember())) {
            getOnRolePermissionFail().accept(event);
            throw new PermissionException("Member does not meet requirements to invoke the command.");
        }
    }

    /**
     * 
     * @param onRolePermissionFail is the lambda function to be called when a user
     *                             does not have the permission to invoke the
     *                             command
     */
    protected void setOnRolePermissionFail(Consumer<MessageReceivedEvent> onRolePermissionFail) {
        ErrorMessages.requireNonNullParam(onRolePermissionFail, "onRolePermissionFail");
        this.onRolePermissionFail = onRolePermissionFail;
    }

    /**
     * @return the lambda function called when a user does not have the permission
     *         to invoke the command
     */
    protected Consumer<MessageReceivedEvent> getOnRolePermissionFail() {
        return onRolePermissionFail;
    }

    /**
     * @return an {@link net.dv8tion.jda.api.EmbedBuilder EmbedBuilder} with an
     *         specified title and description.
     */
    protected EmbedBuilder getEmbedSimpleError(String title, String description) {
        return new EmbedBuilder().setTitle(title).setColor(BotConfiguration.getErrorColor())
                .setDescription(description);
    }

    /**
     * @param requiredRoles    is the roles needed to invoke the command
     * @param blacklistedRoles is the roles blacklisted from invoking the command
     * @return an embed message error that describes the roles needed or blacklisted
     *         from invoking the command
     */
    protected EmbedBuilder getEmbedPermissionError(Set<Role> requiredRoles, Set<Role> blacklistedRoles) {
        String description = "";
        if (requiredRoles != null && !requiredRoles.isEmpty()) {
            description += "The role"
                    + (requiredRoles.size() == 1 ? "" : "s") + ": " + String.join(", ", requiredRoles.stream()
                            .map(role -> MarkdownUtil.monospace(role.getName())).collect(Collectors.toSet()))
                    + " is required to invoke the command. ";
        }
        if (blacklistedRoles != null && !blacklistedRoles.isEmpty()) {
            description += "The role"
                    + (requiredRoles.size() == 1 ? "" : "s") + ": " + String
                            .join(", ",
                                    blacklistedRoles.stream().map(role -> MarkdownUtil.monospace(role.getName()))
                                            .collect(Collectors.toSet()))
                    + " is blacklisted from invoking the command.";
        }
        if (description.isBlank()) {
            description = "Unidentified permission error.";
        }
        return getEmbedSimpleError("Role Permission Error", description);
    }

    /**
     * @return an {@link net.dv8tion.jda.api.EmbedBuilder EmbedBuilder} that alerts
     *         the user that input have invalid syntax or parameters.
     * 
     * @apiNote it is recommended to use the default methods that those cases, such
     *          as {@link #getEmbedMissingArguments()}
     * 
     * @see #getEmbedMissingArguments()
     * @see #getEmbedInvalidParameterTypes()
     */
    protected EmbedBuilder getEmbedInvalidParameterError(String errorName) {
        ErrorMessages.requireNonNullParam(errorName, "errorName");
        EmbedBuilder builder = new EmbedBuilder().setTitle(errorName).setColor(BotConfiguration.getErrorColor());

        if (getParent() != null)
            builder.addField("Syntax", MarkdownUtil.monospace(
                    BotConfiguration.getBotPrefix() + getParent().getPrefix() + " " + getPrefix() + " " + getSyntax()),
                    false);
        else
            builder.addField("Syntax",
                    MarkdownUtil.monospace(BotConfiguration.getBotPrefix() + getPrefix() + " " + getSyntax()), false);

        if (getParent() != null && getEmbedInfo() != null) {
            builder.setDescription("Run "
                    + MarkdownUtil.monospace(
                            BotConfiguration.getBotPrefix() + getParent().getPrefix() + " help " + getPrefix())
                    + " to see a better description of the command.");
        }

        return builder;
    }

    /**
     * @return an {@link net.dv8tion.jda.api.EmbedBuilder EmbedBuilder} that alerts
     *         the user that input have missing arguments
     * 
     * @apiNote this is the equivalent of the return value of
     *          {@link #getEmbedInvalidParameterError(String)} with "Missing
     *          Argument(s) Error" as the errorName parameter
     */
    protected EmbedBuilder getEmbedMissingArguments() {
        return getEmbedInvalidParameterError("Missing Argument(s) Error");
    }

    /**
     * @return an {@link net.dv8tion.jda.api.EmbedBuilder EmbedBuilder} that alerts
     *         the user that input have invalid parameter types
     * 
     * @apiNote this is the equivalent of the return value of
     *          {@link #getEmbedInvalidParameterError(String)} with "Invalid
     *          Parameter Type(s)" as the errorName parameter
     */
    protected EmbedBuilder getEmbedInvalidParameterTypes() {
        return getEmbedInvalidParameterError("Invalid Parameter Type(s)");
    }
}