package com.github.raybipse.framework;

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
        // Check for null values
        if (getName() == null)
            throw new InvalidReturnTypeException("\"getName()\" cannot return null.");
        if (getPrefix() == null)
            throw new InvalidReturnTypeException("\"getPrefix()\" cannot return null.");
        if (getChildren() == null)
            throw new InvalidReturnTypeException("\"getChildren()\" cannot return null.");
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
     * @return an array of commands the group directly inherits. Return an empty array is there is none.
     * Do not return null.
     */
    public abstract Command[] getChildren();

    /**
     * @return the syntax of the command group. The prefix cannot be null.
     */
    public abstract String getPrefix();

    /**
     * A command that gives information about for the {@link CommandGroup} and its children.
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
                builder = new EmbedBuilder().setTitle("Command Group: " + getParent().getName());

                if (getParent().getChildren().length == 0) {
                    builder.appendDescription("This command group contains no commands.");
                } else {
                    StringBuilder stringBuilder = new StringBuilder("This command group contains the following commands: ");
                    for (Command children : getParent().getChildren()) {
                        stringBuilder.append(children.getName() + ", ");
                    }
                    builder.appendDescription(stringBuilder.substring(0, stringBuilder.length()-1));
                }
            } else if (arguments.length == 0 && getParent() == null) { // Shows the help command's info itself
                builder = getEmbedInfo();
            } else { // Shows the command of the command group's children that the first arg specified
                for (Command children : getParent().getChildren()) {
                    if (children.getName().equalsIgnoreCase(arguments[0])) {
                        builder = children.getEmbedInfo();
                        if (builder == null) {
                            builder = new EmbedBuilder()
                                .setDescription("Information about command \"" + arguments[0] + "\" is hidden.")
                                .setColor(BotConfiguration.getErrorColor());
                        }
                    }
                }
                if (builder == null) {
                    builder = new EmbedBuilder()
                        .setDescription("Command \"" + arguments[0] + "\" not found.")
                        .setColor(BotConfiguration.getErrorColor());
                }
            }

            event.getChannel().sendMessage(builder.build()).queue();
        }

    }
}