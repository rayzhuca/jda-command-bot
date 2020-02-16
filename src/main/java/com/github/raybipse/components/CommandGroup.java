package com.github.raybipse.components;

import com.github.raybipse.core.BotConfiguration;
import com.github.raybipse.internal.ErrorMessages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * An entity that groups {@link Command} into a group. Children of the command
 * group can only be invoked if the {@link CommandGroup}'s prefix is added
 * before the command prefix.
 * 
 * A command group can sometimes be referred as the parent of the commands
 * returned in {@link #getChildren()}.
 * 
 * @author RayBipse
 */
public abstract class CommandGroup {

    protected CommandGroup() {
        ErrorMessages.requireNonNullReturn(getName(), "getName");
        ErrorMessages.requireNonNullReturn(getPrefix(), "getPrefix");
        ErrorMessages.requireNonNullReturn(getChildren(), "getChildren");
    }

    /**
     * @return the name of the command group. The name cannot be null.
     */
    public abstract String getName();

    /**
     * @return the description of the command group. Return null if there is none.
     */
    public abstract String getDescription();

    /**
     * @return an array of commands the group directly inherits. Return an empty
     *         array is there is none. Do not return null.
     */
    public abstract Command[] getChildren();

    /**
     * @return the syntax of the command group. The prefix cannot be null.
     */
    public abstract String getPrefix();

    /**
     * A command that gives information about for the {@link CommandGroup} and its
     * children.
     * 
     * @author RayBipse
     */
    public class Help extends Command {

        @Override
        public String getName() {
            return "Help";
        }

        @Override
        public String getDescription() {
            return "Gives information about the specified command.";
        }

        @Override
        public String getPrefix() {
            return "help";
        }

        @Override
        public String[] getExamples() {
            return new String[] { "help" };
        }

        @Override
        public String getSyntax() {
            return "[command]";
        }

        @Override
        public CommandGroup getParent() {
            return CommandGroup.this;
        }

        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            String messageContent = event.getMessage().getContentDisplay();
            if (event.getAuthor().isBot())
                return;
            if (!getInputValidity(messageContent))
                return;

            messageContent = trimInputBeginning(messageContent);
            String[] arguments = splitUserInput(messageContent);

            EmbedBuilder builder = null;

            if (arguments.length == 0 && getParent() != null) { // Shows a list of commands the command group has
                builder = new EmbedBuilder().setTitle("Command Group: \"" + getParent().getName() + "\"").setColor(BotConfiguration.getPromptColor());
                builder.setDescription(getParent().getDescription());
                builder.addField("Prefix", getParent().getPrefix(), false);

                if (getChildren().length == 0) {
                    builder.addField("Commands", "This command group contains no commands.", false);
                } else {
                    String[] allCommandPrefixes = new String[getChildren().length];
                    for (int i = 0; i < getChildren().length; i++) {
                        allCommandPrefixes[i] = getChildren()[i].getPrefix();
                    }
                    builder.addField("Commands", "``"+String.join("``, ``", allCommandPrefixes)+"``", false);
                }
            } else if (arguments.length == 0 && getParent() == null) { // Shows the help command's info itself
                builder = getEmbedInfo();
            } else { // Shows the command of the command group's children that the first arg
                     // specified
                for (Command children : getChildren()) {
                    if (children.getPrefix().equals(arguments[0])) {
                        builder = children.getEmbedInfo();
                        if (builder == null) {
                            builder = new EmbedBuilder()
                                    .setDescription("Information about command \"" + arguments[0] + "\" is hidden.")
                                    .setColor(BotConfiguration.getErrorColor());
                        }
                    }
                }
                if (builder == null) {
                    builder = new EmbedBuilder().setDescription("Command \"" + arguments[0] + "\" not found.")
                            .setColor(BotConfiguration.getErrorColor());
                }
            }

            event.getChannel().sendMessage(builder.build()).queue();
        }

    }
}